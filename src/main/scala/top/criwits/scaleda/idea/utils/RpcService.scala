package top.criwits.scaleda
package idea.utils

import com.intellij.openapi.Disposable
import io.grpc.{Server, ServerBuilder}
import scaleda.scaleda.{ScaledaEmpty, ScaledaGotoSource, ScaledaRpcGrpc}

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.language.existentials

class RpcService extends Disposable {
  private final val DEFAULT_PORT = 4151

  private class ServerImpl extends ScaledaRpcGrpc.ScaledaRpc {
    override def ping(request: ScaledaEmpty): Future[ScaledaEmpty] = async {
      ScaledaEmpty.of()
    }

    override def gotoSource(request: ScaledaGotoSource): Future[ScaledaEmpty] = async {
      ScaledaEmpty.of()
    }
  }

  val builder = ServerBuilder.forPort(DEFAULT_PORT)
  builder.addService(ScaledaRpcGrpc.bindService(new ServerImpl, ExecutionContext.global))
  var stop                   = false
  var server: Option[Server] = None
  while (!stop) {
    try {
      server = Some(builder.build())
      server.get.start();
      MainLogger.info("scaleda grpc server serve at port", DEFAULT_PORT)
    } catch {
      case _e: Throwable => {
        Thread.sleep(3000)
      }
    }
  }

  override def dispose() = {
    stop = true;
    server.foreach(s => s.shutdown())
    server = None
  }
}
