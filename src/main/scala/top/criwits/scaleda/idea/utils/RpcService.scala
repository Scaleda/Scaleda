package top.criwits.scaleda
package idea.utils

import kernel.net.RpcPatch
import kernel.utils.Paths

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.LocalFilePath
import io.grpc.Server
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

import java.io.{File, IOException}
import java.util.concurrent.LinkedBlockingQueue
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

class ScaledaRpcServerImpl(project: () => Project) extends ScaledaRpcGrpc.ScaledaRpc {
  override def ping(request: ScaledaEmpty): Future[ScaledaEmpty] = async {
    MainLogger.info("grpc ping")
    ScaledaEmpty.of()
  }

  override def gotoSource(request: ScaledaGotoSource): Future[ScaledaEmpty] = async {
    MainLogger.info(s"grpc gotoSource($request)")
    // Warn: line number starts from 1
    RpcService.pushGotoInfo(RpcService.RpcGotoInfo(request.file, math.max(0, request.line - 1), request.column))
    ScaledaEmpty.of()
  }
}

class RpcService extends Disposable {
  import RpcService.server
  private final val DEFAULT_PORT = 4151

  private var myProject: Option[Project] = None

  def setProject(project: Project): Unit = myProject = Some(project)

  var stop = false
  // var server: Option[Server] = None
  val thread = new Thread(
    () => {
      while (!stop) {
        val sourcePath       = Paths.pwd.getAbsolutePath
        val executionContext = ExecutionContext.global
        // if a server instance was created before, reuse it
        require(myProject.nonEmpty)
        implicit val project = myProject.get
        // server = IdeaManifestManager.getObject[Server](getClass.getName)
        if (server.isEmpty) {
          try {
            // TODO: Add all IDEA grpc services here
            RpcService.synchronized {
              server = Some(
                RpcPatch.getStartServer(
                  Seq(
                    // IDEA interaction
                    ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl(() => myProject.get), executionContext)
                    // Fuse as data provider: use transfer now
                    // RemoteFuseGrpc.bindService(
                    //   new FuseDataProvider(sourcePath),
                    //   executionContext
                    // )
                  ),
                  DEFAULT_PORT
                )
              )
            }
            // IdeaManifestManager.putObject(getClass.getName, server.get)
            server.get.awaitTermination()
          } catch {
            case _: InterruptedException =>
              MainLogger.info("gRPC Service stopped, ", if (stop) "will not restart" else "will restart")
            case e: IOException =>
              MainLogger.warn("gRPC Service io err:", e, "will exit")
              stop = true
            case _e: Throwable =>
              MainLogger.warn("trying", _e)
              _e.printStackTrace()
              Thread.sleep(3000)
          }
        } else {
          MainLogger.debug("gRPC Service has started, waiting...")
          Thread.sleep(1500)
        }
      }
    },
    "rpc-server-service"
  )
  thread.start()

  override def dispose() = {
    stop = true
    RpcService.synchronized {
      server.foreach(s => s.shutdown())
      server = None
    }
    thread.interrupt()
  }
}

object RpcService {
  var server: Option[Server] = None
  case class RpcGotoInfo(filepath: String, line: Int, column: Int)
  private val gotoInfoQueue           = new LinkedBlockingQueue[RpcGotoInfo]()
  def pushGotoInfo(info: RpcGotoInfo) = gotoInfoQueue.put(info)

  def startRpcGotoHandler(project: Project): Thread = {
    val thread = new Thread(() => {
      try {
        while (true) {
          val info        = gotoInfoQueue.take()
          var virtualFile = new LocalFilePath(info.filepath, false).getVirtualFile
          if (virtualFile == null)
            virtualFile = new LocalFilePath(
              new File(project.getBasePath, info.filepath).getAbsolutePath,
              false
            ).getVirtualFile
          if (virtualFile == null) {
            // TODO: i18n
            Notification(project = project).warn("File not found:", info.filepath)
          } else
            runInEdt {
              val descriptor = new OpenFileDescriptor(project, virtualFile, info.line, info.column)
              descriptor.navigate(true)
            }
        }
      } catch {
        case e: InterruptedException =>
          MainLogger.warn(Thread.currentThread().getName, "exit normally")
      }
    })
    thread.setName("rpc-goto-handler")
    thread.setDaemon(true)
    thread.start()
    thread
  }
}
