package top.scaleda
package idea.rvcd

import idea.utils.ScaledaIdeaLogger

import rvcd.rvcd.RvcdInputEvent

import java.net._
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

case class FrameBuffer(width: Int, height: Int, data: Array[Byte])

class RvcdFrameBufferChannel(socket: SocketChannel) {
  def requestFrame(bytesPerPixel: Int = 2): Option[FrameBuffer] = {
    val buffer = new Array[Byte](4)
    val buf    = ByteBuffer.wrap(buffer)
    var cnt    = 0
    var r      = -1;
    do {
      r = socket.read(buf)
      if (r >= 0) cnt += r
    } while (r >= 0 && cnt < 4)
    if (cnt != 4) {
      val msg = s"Failed to read frame header, read $cnt bytes"
      throw new RuntimeException(msg)
      // ScaledaIdeaLogger.warn(msg)
      // return None
    }
    // ScaledaIdeaLogger.info(s"Received frame header: ${buffer.map("%02X".format(_)).mkString(", ")}")
    // uint16_t width, height
    val width  = ((buffer(0) & 0xff) << 8) | (buffer(1) & 0xff)
    val height = ((buffer(2) & 0xff) << 8) | (buffer(3) & 0xff)
    // ScaledaIdeaLogger.info(s"Frame size: $width x $height")
    if (width <= 0 || height <= 0 || width > 8192 || height > 8192) {
      throw new RuntimeException(s"Invalid frame size: $width x $height")
    }
    val len  = width * height * bytesPerPixel
    val data = new Array[Byte](len)
    val buf2 = ByteBuffer.wrap(data)
    cnt = 0
    r = -1;
    do {
      r = socket.read(buf2)
      if (r >= 0) cnt += r
    } while (r >= 0 && cnt < len)
    if (cnt != len) {
      val msg = "Failed to read frame data, read 0x%x bytes, expected 0x%x bytes".format(cnt, len)
      throw new RuntimeException(msg)
      // ScaledaIdeaLogger.warn()
      // return None
    }
    Some(FrameBuffer(width, height, data))
  }

  def inputEvent(event: RvcdInputEvent): Unit = {
    // val buffer = ByteBuffer.allocate(8)
    // buffer.putInt(event.x)
    // buffer.putInt(event.y)
    // buffer.flip()
    // socket.write(buffer)
    val data = event.toByteArray
    // println(s"Writing input event: ${data.map("%02X".format(_)).mkString(", ")}")
    val len     = data.length
    val lenData = ByteBuffer.allocate(4)
    lenData.putInt(len)
    lenData.flip()
    val buffer = ByteBuffer.wrap(data)

    // buffer.put(data)
    // buffer.reset()
    var n = 0;
    var r = -1;
    do {
      r = socket.write(lenData)
      if (r >= 0) n += r
    } while (r >= 0 && n < 4)
    if (n != 4) {
      val msg = s"Failed to write input event length, wrote $n bytes"
      throw new RuntimeException(msg)
      // ScaledaIdeaLogger.warn(msg)
    }
    n = 0
    r = -1
    do {
      r = socket.write(buffer)
      if (r >= 0) n += r
    } while (r >= 0 && n < len)
    if (n != len) {
      val msg = s"Failed to write input event, wrote $n bytes"
      throw new RuntimeException(msg)
      // ScaledaIdeaLogger.warn(msg)
    }
  }
}

object RvcdFrameBufferChannel {
  private def connectChannel(socketChannel: SocketChannel, socketAddress: SocketAddress): Unit = {
    socketChannel.setOption[Integer](StandardSocketOptions.SO_RCVBUF, 4 * 1024 * 1024)
    socketChannel.setOption[Integer](StandardSocketOptions.SO_SNDBUF, 4 * 1024 * 1024 / 8)
    // socketChannel.configureBlocking(true)  // blocking mode: r cannot be 0
    socketChannel.configureBlocking(false)
    socketChannel.connect(socketAddress)
  }
  private def createTcpSocketChannel() = {
    val socketAddr = new InetSocketAddress("127.0.0.1", 5444)
    val socketChannel = SocketChannel
      .open(StandardProtocolFamily.INET);
    connectChannel(socketChannel, socketAddr)
    socketChannel
  }
  private def createUnixSocketChannel() = {
    val socketPath = "/tmp/rvcd.socket"
    val socketAddr = UnixDomainSocketAddress.of(socketPath)
    val socketChannel = SocketChannel
      .open(StandardProtocolFamily.UNIX);
    connectChannel(socketChannel, socketAddr)
    socketChannel
  }
  def unix(): (RvcdFrameBufferChannel, () => Unit) = {
    val socketChannel = createUnixSocketChannel()
    val channel       = new RvcdFrameBufferChannel(socketChannel)
    val shutdownHook = () => {
      ScaledaIdeaLogger.warn("Shutting down RvcdChannel UnixSocket")
      socketChannel.close()
    }
    (channel, shutdownHook)
  }
  def tcp(): (RvcdFrameBufferChannel, () => Unit) = {
    val socketChannel = createTcpSocketChannel()
    val channel       = new RvcdFrameBufferChannel(socketChannel)
    val shutdownHook = () => {
      ScaledaIdeaLogger.warn("Shutting down RvcdChannel TcpSocket")
      socketChannel.close()
    }
    (channel, shutdownHook)
  }
}
