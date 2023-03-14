package top.criwits.scaleda
package idea.utils

import kernel.net.RpcPatch
import kernel.utils.{KernelLogger, Paths}

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.LocalFilePath
import io.grpc.Server
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

import java.util.concurrent.LinkedBlockingQueue
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

class ScaledaRpcServerImpl(project: () => Project) extends ScaledaRpcGrpc.ScaledaRpc {
  override def ping(request: ScaledaEmpty): Future[ScaledaEmpty] = async {
    KernelLogger.info("grpc ping")
    ScaledaEmpty.of()
  }

  override def gotoSource(request: ScaledaGotoSource): Future[ScaledaEmpty] = async {
    KernelLogger.info(s"grpc gotoSource($request)")
    // TODO: follows not work. do navigation
    inReadAction {
      // val psi = PsiManager
      //   .getInstance(project())
      //   .findFile(new LocalFilePath(request.file, false).getVirtualFile)
      //   .asInstanceOf[VerilogPSIFileRoot]
      // KernelLogger.warn(s"psi: $psi, canNavigate: ${psi.canNavigate}, canNavigateToSource: ${psi.canNavigateToSource}")

      // NavigationUtil.activateFileWithPsiElement(psi, true)
      // PsiNavigationSupport.getInstance
      //   .createNavigatable(project(), new LocalFilePath(request.file, false).getVirtualFile, 0)
      //   .navigate(true)
      // psi.navigate(true)

      // val descriptor = new OpenFileDescriptor(project(), new LocalFilePath(request.file, false).getVirtualFile, 0)
      // KernelLogger.info("descriptor", descriptor)
      // descriptor.navigate(true)

      // project().getMessageBus.syncPublisher(RpcService.TOPIC).navigate(project(), request)

      // val reqService: NavigationRequest = NavigationService.instance().sourceNavigationRequest(new LocalFilePath(request.file, false).getVirtualFile, 0)
      // KernelLogger.warn(s"reqService: $reqService")

      // new NavigatorWithinProject(project(), new java.util.HashMap[String, String](), locationToOffset)
      // SymbolNavigationService.getInstance().psiFileNavigationTarget(psi)
      // SymbolNavigationService.getInstance().psiElementNavigationTarget(psi)
    }
    RpcService.pushGotoInfo(RpcService.RpcGotoInfo(request.file, request.line, request.column))
    ScaledaEmpty.of()
  }
}

class RpcService extends Disposable {
  private final val DEFAULT_PORT = 4151

  private var myProject: Project = _

  def setProject(project: Project): Unit = myProject = project

  var stop                   = false
  var server: Option[Server] = None
  val thread = new Thread(
    () => {
      while (!stop) {
        val sourcePath       = Paths.pwd.getAbsolutePath
        val executionContext = ExecutionContext.global
        try {
          // TODO: Add all IDEA grpc services here
          server = Some(
            RpcPatch.getStartServer(
              Seq(
                // IDEA interaction
                ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl(() => myProject), executionContext)
                // Fuse as data provider
                // RemoteFuseGrpc.bindService(
                //   new FuseDataProvider(sourcePath),
                //   executionContext
                // )
              ),
              DEFAULT_PORT
            )
          )
          server.get.awaitTermination()
        } catch {
          case _: InterruptedException =>
            KernelLogger.warn("gRPC Service stopped")
          case _e: Throwable =>
            MainLogger.warn("trying", _e)
            _e.printStackTrace()
            Thread.sleep(3000)
        }
      }
    },
    "rpc-server-service"
  )
  thread.start()

  override def dispose() = {
    stop = true
    server.foreach(s => s.shutdown())
    server = None
    thread.interrupt()
  }
}

object RpcService {
  case class RpcGotoInfo(filepath: String, line: Int, column: Int)
  private val gotoInfoQueue           = new LinkedBlockingQueue[RpcGotoInfo]()
  def pushGotoInfo(info: RpcGotoInfo) = gotoInfoQueue.put(info)

  def startRpcGotoHandler(project: Project): Thread = {
    val thread = new Thread(() => {
      try {
        while (true) {
          val info = gotoInfoQueue.take()
          runInEdt {
            val descriptor = new OpenFileDescriptor(
              project,
              new LocalFilePath(info.filepath, false).getVirtualFile,
              info.line,
              info.column
            )
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

  // class RpcGotoTopic {
  //   def navigate(project: Project, request: ScaledaGotoSource) = {
  //     KernelLogger.info(s"RpcGotoTopic(request=$request)")
  //     val psi = PsiManager
  //       .getInstance(project)
  //       .findFile(new LocalFilePath(request.file, false).getVirtualFile)
  //       .asInstanceOf[VerilogPSIFileRoot]
  //     NavigationUtil.activateFileWithPsiElement(psi, true)
  //     psi.navigate(true)
  //   }
  // }
  //
  // final val TOPIC: Topic[RpcGotoTopic] = Topic.create("Scaleda RPC", classOf[RpcGotoTopic])
}
