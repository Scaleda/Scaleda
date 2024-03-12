package top.scaleda
package kernel.net

import kernel.net.fuse.FuseTransferServer
import kernel.net.fuse.fs.RemoteFuseTransferGrpc
import kernel.net.remote.RunReplyType._
import kernel.net.remote._
import kernel.net.user.{JwtAuthorizationInterceptor, RemoteRegisterLoginImpl}
import kernel.shell.ScaledaRunHandler
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.utils._

import io.grpc.stub.StreamObserver
import org.apache.commons.codec.digest.DigestUtils
import top.scaleda.kernel.utils.EnvironmentUtils.Backup

import java.io.File
import java.time.Instant
import java.util.UUID
import scala.async.Async.async
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

/** Main gRPC server, will receiver commands from client and execute them
  */
object RemoteServer {
  final val DEFAULT_PORT = 20051

  final val AVAILABLE_REMOTE_HOSTS = Seq(
    "a.chiro.work",
    "127.0.0.1",
    "pc.chiro.work"
  )

  val commandThreadPool    = new mutable.HashMap[String, (String, Thread)]()
  val terminatingCommandId = new mutable.HashSet[String]()

  private class RemoteImpl extends RemoteGrpc.Remote {

    override def run(
        request: RunRequest,
        responseObserver: StreamObserver[RunReply]
    ): Unit = {
      KernelLogger.info(
        s"remote run request: pwd=${request.path}, base=${request.projectBase}, " +
          s"commands=${request.commands.map(c => s"""\"$c\"""").mkString(" ")}"
      )
      val user = JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY.get()
      if (user == null) {
        responseObserver.onNext(RunReply(replyType = RUN_REPLY_TYPE_ERR_AUTH))
        return
      }
      // do text replacement
      val username = user.getUsername
      val targetPath = new File(
        Paths.getServerTemporalDir(),
        username + "-" + DigestUtils.sha256Hex(request.runId)
      ).getAbsolutePath.replace('\\', '/')
      val sourcePath = request.projectBase
      val replacer = new ImplicitPathReplace(sourcePath, targetPath) {
        override def doCharReplace(src: String, skipInner: Boolean = false) =
          super.doCharReplace(src.replace('\\', '/'))
      }
      val commandDeps = CommandDeps(
        args = request.commands.map(c => replacer.doCharReplace(c)),
        path = replacer.doCharReplace(request.path),
        envs = request.envs.map(t => (replacer.doCharReplace(t.a), replacer.doCharReplace(t.b)))
      )
      // generate command run id
      val commandId = UUID.randomUUID().toString
      responseObserver.onNext(RunReply(RUN_REPLY_TYPE_COMMAND_ID, strValue = commandId))
      KernelLogger.info(
        s"remote real execute: cd ${"\"" + commandDeps.path + "\""} && " +
          s"${commandDeps.args.map(c => "\"" + c + "\"").mkString(" ")}",
        commandId
      )
      val commandRunThread = new Thread(() => {
        // Note that there's only one command to execute
        CommandRunner.execute(
          Seq(commandDeps),
          new ScaledaRunHandler {

            /** Return true if handler is stopping this process
              *
              * @return terminating
              */
            override def isTerminating = {
              terminatingCommandId.contains(commandId)
            }

            override def onStdout(data: String) = {
              KernelLogger.info("[remote executor stdout]", data)
              responseObserver.onNext(
                new RunReply(RUN_REPLY_TYPE_STDOUT, strValue = replacer.doInvCharReplace(data))
              )
            }

            override def onStderr(data: String) = {
              KernelLogger.warn("[remote executor stderr]", data)
              responseObserver.onNext(
                new RunReply(RUN_REPLY_TYPE_STDERR, strValue = replacer.doInvCharReplace(data))
              )
            }

            override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean) = {
              responseObserver.onNext(
                new RunReply(
                  RUN_REPLY_TYPE_RETURN,
                  intValue = returnValue,
                  finishedAll = finishedAll,
                  meetErrors = meetErrors
                )
              )
            }
          }
        )
        responseObserver.onCompleted()
      })
      commandRunThread.setName(s"remote-run-command-${commandId}")
      cleanStoppedThreadInPool()
      commandThreadPool.put(commandId, (request.runId, commandRunThread))
      commandRunThread.start()
    }

    private def cleanStoppedThreadInPool() = {
      commandThreadPool.filterInPlace((_, t) => t._2.isAlive)
      terminatingCommandId.filterInPlace(commandThreadPool.contains)
    }

    override def getProfiles(request: Empty): Future[ProfilesReply] = async {
      val profiles = Toolchain.profiles().toSeq.map(_.asRemoteProfile)
      KernelLogger.info("remote profiles:", profiles)
      ProfilesReply.of(profiles)
    }

    override def getRemoteInfo(request: Empty): Future[RemoteInfo] = async {
      RemoteInfo(
        os = OS.getOSType match {
          case OS.Windows => RemoteOsType.REMOTE_OS_TYPE_WINDOWS
          case OS.MacOS   => RemoteOsType.REMOTE_OS_TYPE_MACOS
          case OS.Unix    => RemoteOsType.REMOTE_OS_TYPE_LINUX
        },
        tempPrefix = Paths.getServerTemporalDir()
      )
    }

    override def stopCommand(request: StopCommandRequest): Future[StopCommandReply] = async {
      val thread = commandThreadPool.get(request.commandId)
      if (thread.isEmpty) {
        StopCommandReply(ok = false, reason = "No such command")
      } else {
        val t = thread.get._2
        t.interrupt()
        terminatingCommandId.add(request.commandId)
        val timeStart     = Instant.now
        val deadline      = timeStart.plusMillis(request.timeoutMs)
        val enableTimeout = request.timeoutMs != 0
        while (t.isAlive && (enableTimeout && Instant.now.isBefore(deadline))) {
          Thread.sleep(100)
        }
        val ret = if (t.isAlive) {
          StopCommandReply.of(ok = false, "Timeout")
        } else {
          StopCommandReply(ok = true)
        }
        cleanStoppedThreadInPool()
        ret
      }
    }
  }

  /** Start the server, using in kernel, not in IDEA
    * @param port port
    */
  def serve(port: Int = DEFAULT_PORT): Unit = {
    val executionContext = ExecutionContext.global
    val server = RpcPatch.getStartServer(
      Seq(
        RemoteGrpc.bindService(new RemoteImpl, executionContext),
        RemoteRegisterLoginGrpc.bindService(new RemoteRegisterLoginImpl, executionContext),
        RemoteFuseTransferGrpc.bindService(new FuseTransferServer, executionContext)
      ),
      port,
      enableAuthCheck = Backup.env.contains("AUTH_ENABLE")
    )
    server.awaitTermination()
  }
}
