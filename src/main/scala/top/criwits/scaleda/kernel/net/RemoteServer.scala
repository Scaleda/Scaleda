package top.criwits.scaleda
package kernel.net

import kernel.net.fuse.FuseTransferServer
import kernel.net.fuse.fs.RemoteFuseTransferGrpc
import kernel.net.remote.RunReplyType.{RUN_REPLY_TYPE_RETURN, RUN_REPLY_TYPE_STDERR, RUN_REPLY_TYPE_STDOUT}
import kernel.net.remote._
import kernel.net.user.{JwtAuthorizationInterceptor, RemoteRegisterLoginImpl}
import kernel.shell.ScaledaRunHandler
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.utils.{EnvironmentUtils, KernelLogger, OS, Paths}

import io.grpc.stub.StreamObserver

import java.io.File
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

/** Main gRPC server, will receiver commands from client and execute them
  */
object RemoteServer {
  final val DEFAULT_PORT = 20051

  final val AVAILABLE_REMOTE_HOSTS = Seq(
    "127.0.0.1",
    "pc.chiro.work"
  )

  private class RemoteImpl extends RemoteGrpc.Remote {
    override def run(
        request: RunRequest,
        responseObserver: StreamObserver[RunReply]
    ): Unit = {
      val user = JwtAuthorizationInterceptor.USERNAME_CONTEXT_KEY.get()
      val commandDeps = if (user != null) {
        // do text replacement
        val username                       = user.getUsername
        val targetPath                     = new File(Paths.getServerTemporalDir(), username).getAbsolutePath
        val sourcePath                     = request.path
        def doReplace(src: String): String = src.replaceAll(sourcePath, targetPath)
        CommandDeps(
          args = request.commands.map(doReplace),
          path = targetPath,
          envs = request.envs.map(t => (doReplace(t.a), doReplace(t.b)))
        )
      } else {
        CommandDeps(
          args = request.commands,
          path = request.path,
          envs = request.envs.map(t => (t.a, t.b))
        )
      }
      // Note that there's only one command to execute
      CommandRunner.execute(
        Seq(commandDeps),
        new ScaledaRunHandler {
          override def onStdout(data: String) = {
            KernelLogger.info("[remote executor stdout]", data)
            responseObserver.onNext(
              new RunReply(RUN_REPLY_TYPE_STDOUT, strValue = data)
            )
          }

          override def onStderr(data: String) = {
            KernelLogger.warn("[remote executor stderr]", data)
            responseObserver.onNext(
              new RunReply(RUN_REPLY_TYPE_STDERR, strValue = data)
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
    }

    override def getProfiles(request: Empty): Future[ProfilesReply] = async {
      val profiles = Toolchain.profiles().toSeq.map(_.asRemoteProfile)
      KernelLogger.info("remote profiles:", profiles)
      ProfilesReply.of(profiles)
    }

    override def getRemoteInfo(request: Empty): Future[RemoteInfo] = async {
      RemoteInfo(os = OS.getOSType match {
        case OS.Windows => RemoteOsType.REMOTE_OS_TYPE_WINDOWS
        case OS.MacOS   => RemoteOsType.REMOTE_OS_TYPE_MACOS
        case OS.Unix    => RemoteOsType.REMOTE_OS_TYPE_LINUX
      })
    }
  }

  def serve(port: Int = DEFAULT_PORT): Unit = {
    val executionContext = ExecutionContext.global
    val server = RpcPatch.getStartServer(
      Seq(
        RemoteGrpc.bindService(new RemoteImpl, executionContext),
        RemoteRegisterLoginGrpc.bindService(new RemoteRegisterLoginImpl, executionContext),
        RemoteFuseTransferGrpc.bindService(new FuseTransferServer, executionContext)
      ),
      port,
      enableAuthCheck = EnvironmentUtils.Backup.env.contains("AUTH_ENABLE")
    )
    server.awaitTermination()
  }
}
