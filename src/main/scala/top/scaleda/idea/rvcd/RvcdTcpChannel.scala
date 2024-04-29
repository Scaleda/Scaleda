package top.scaleda
package idea.rvcd

import idea.utils.ScaledaIdeaLogger

import java.net.Socket

case class FrameBuffer(width: Int, height: Int, data: Array[Byte])

class RvcdTcpChannel(socket: Socket) {
  def requestFrame(): Option[FrameBuffer] = {
    val in     = socket.getInputStream
    val buffer = new Array[Byte](4)
    var cnt    = 0;
    var r      = -1;
    do {
      r = in.read(buffer, cnt, 4 - cnt)
      cnt += r
    } while (r > 0 && cnt < 4)
    if (cnt != 4) {
      // throw new RuntimeException(s"Failed to read frame header, read ${cnt - r} bytes, r = $r")
      // ScaledaIdeaLogger.warn(s"Failed to read frame header, read ${cnt - r} bytes, r = $r")
      return None
    }
    // ScaledaIdeaLogger.info(s"Received frame header: ${buffer.map("%02X".format(_)).mkString(", ")}")
    // uint16_t width, height
    val width  = ((buffer(0) & 0xff) << 8) | (buffer(1) & 0xff)
    val height = ((buffer(2) & 0xff) << 8) | (buffer(3) & 0xff)
    // ScaledaIdeaLogger.info(s"Frame size: $width x $height")
    if (width <= 0 || height <= 0) {
      throw new RuntimeException(s"Invalid frame size: $width x $height")
    }
    val data_length = width * height * 2
    val data        = new Array[Byte](data_length)
    cnt = 0;
    r = -1;
    do {
      r = in.read(data, cnt, data_length - cnt)
      cnt += r
    } while (r > 0 && cnt < data_length)
    if (cnt != data_length) {
      // throw new RuntimeException(s"Failed to read frame data, read ${cnt - r} bytes, r = $r")
      // ScaledaIdeaLogger.warn(s"Failed to read frame data, read ${cnt - r} bytes, r = $r")
      return None
    }
    Some(FrameBuffer(width, height, data))
  }
}

object RvcdTcpChannel {
  def apply(): (RvcdTcpChannel, () => Unit) = {
    val socket  = new Socket("127.0.0.1", 5444)
    val channel = new RvcdTcpChannel(socket)
    val shutdownHook = () => {
      ScaledaIdeaLogger.warn("Shutting down RvcdTcpChannel")
      socket.close()
    }
    (channel, shutdownHook)
  }
}
