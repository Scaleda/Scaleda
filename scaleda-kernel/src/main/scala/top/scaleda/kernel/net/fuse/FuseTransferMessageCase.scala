package top.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.FuseTransferMessage
import kernel.utils.serialise.BinarySerializeHelper

case class FuseTransferMessageCase(
    id: Long,
    identifier: String,
    function: String,
    data: Any,
    error: Option[Throwable] = None
) {
  def toMessage: FuseTransferMessage =
    FuseTransferMessage.of(id, function, BinarySerializeHelper.fromAny(data))
}

object FuseTransferMessageCase {
  private var lastMsgId: Long = 2

  def newMsgId: Long = {
    synchronized {
      val v = lastMsgId
      lastMsgId = lastMsgId + 1
      return v
    }
  }
}
