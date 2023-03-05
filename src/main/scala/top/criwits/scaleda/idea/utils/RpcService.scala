package top.criwits.scaleda
package idea.utils

import kernel.utils.KernelLogger
import verilog.VerilogPSIFileRoot

import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.LocalFilePath
import com.intellij.psi.PsiManager
import com.intellij.util.messages.Topic
import io.grpc.{ManagedChannelBuilder, Server, ServerBuilder}
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

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

      project().getMessageBus.syncPublisher(RpcService.TOPIC).navigate(project(), request)

      // val reqService: NavigationRequest = NavigationService.instance().sourceNavigationRequest(new LocalFilePath(request.file, false).getVirtualFile, 0)
      // KernelLogger.warn(s"reqService: $reqService")

      // new NavigatorWithinProject(project(), new java.util.HashMap[String, String](), locationToOffset)
      // SymbolNavigationService.getInstance().psiFileNavigationTarget(psi)
      // SymbolNavigationService.getInstance().psiElementNavigationTarget(psi)
    }
    ScaledaEmpty.of()
  }
}

class RpcService extends Disposable {
  private final val DEFAULT_PORT = 4151

  private var myProject: Project = _

  def setProject(project: Project): Unit = myProject = project

  var stop                   = false
  var server: Option[Server] = None
  new Thread(() => {
    while (!stop) {
      try {
        val provider = RpcPatch.getDefaultServerProvider
        val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
        method.setAccessible(true)
        val builder = method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
        builder.addService(
          ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl(() => myProject), ExecutionContext.global)
        )
        MainLogger.info("scaleda grpc server serve at port", DEFAULT_PORT)
        server = Some(builder.build().start())
        server.get.awaitTermination()
      } catch {
        case _e: Throwable =>
          MainLogger.warn("trying", _e)
          _e.printStackTrace()
          Thread.sleep(3000)
      }
    }
  }).start()

  override def dispose() = {
    stop = true
    server.foreach(s => s.shutdown())
    server = None
  }
}

// object RpcServiceTest extends App {
//   private final val DEFAULT_PORT = 4151
//   val loader                     = getClass.getClassLoader
//   KernelLogger.info(s"loader: $loader")
//   private val usePatch = false
//   val builder = if (usePatch) {
//     val provider = RpcPatch.getDefaultServerProvider
//     val method   = provider.getClass.getDeclaredMethod("builderForPort", Integer.TYPE)
//     method.setAccessible(true)
//     method.invoke(provider, DEFAULT_PORT).asInstanceOf[ServerBuilder[_]]
//   } else {
//     ServerBuilder.forPort(DEFAULT_PORT)
//   }
//   builder.addService(ScaledaRpcGrpc.bindService(new ScaledaRpcServerImpl(() => null), ExecutionContext.global))
//   KernelLogger.info("before start()")
//   val server = builder.build().start()
//   val serverThread = new Thread(() => {
//     server.awaitTermination()
//   })
//   serverThread.start()
//
//   private val clientBuilder = ManagedChannelBuilder.forAddress("127.0.0.1", DEFAULT_PORT)
//   clientBuilder.usePlaintext()
//   val channel = clientBuilder.build()
//   val stub    = ScaledaRpcGrpc.blockingStub(channel)
//   stub.ping(ScaledaEmpty.of())
//   channel.shutdownNow()
//
//   server.shutdownNow()
//   KernelLogger.info("done")
// }

object RpcService {
  class RpcGotoTopic {
    def navigate(project: Project, request: ScaledaGotoSource) = {
      KernelLogger.info(s"RpcGotoTopic(request=$request)")
      val psi = PsiManager
        .getInstance(project)
        .findFile(new LocalFilePath(request.file, false).getVirtualFile)
        .asInstanceOf[VerilogPSIFileRoot]
      NavigationUtil.activateFileWithPsiElement(psi, true)
      psi.navigate(true)
    }
  }

  final val TOPIC: Topic[RpcGotoTopic] = Topic.create("Scaleda RPC", classOf[RpcGotoTopic])
}

// object RpcServiceClientTest extends App {
//   private final val DEFAULT_PORT = 4151
//   private val clientBuilder      = ManagedChannelBuilder.forAddress("127.0.0.1", DEFAULT_PORT)
//   clientBuilder.usePlaintext()
//   val channel = clientBuilder.build()
//   val stub    = ScaledaRpcGrpc.blockingStub(channel)
//   stub.ping(ScaledaEmpty.of())
//   channel.shutdownNow()
//   KernelLogger.info("done")
// }
