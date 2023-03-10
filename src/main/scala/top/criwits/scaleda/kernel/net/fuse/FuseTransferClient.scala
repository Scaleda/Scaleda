package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.RemoteFuseTransferGrpc
import kernel.net.{RemoteServer, RpcPatch}
import kernel.utils.serialise.BinarySerializeHelper

import com.google.protobuf.ByteString

object FuseTransferClient {
  private final val DEFAULT_PORT = RemoteServer.DEFAULT_PORT
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    RpcPatch.getClient(RemoteFuseTransferGrpc.stub, host, port, enableAuthProvide = true)
  def asStream(dataProvider: FuseDataProvider, host: String = "127.0.0.1", port: Int = DEFAULT_PORT) = {
    val (client, shutdown) = FuseTransferClient(host, port)
    val observer           = new FuseTransferClientObserver(dataProvider)
    val stream             = client.visit(observer)
    observer.setTx(stream)
    (client, stream, observer, shutdown)
  }
  def requestMessageInto[T](msg: ByteString): T =
    BinarySerializeHelper.fromGrpcBytes(msg).asInstanceOf[T]
}
