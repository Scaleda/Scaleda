package top.scaleda
package idea.windows.bottomPanel.netviewer

import idea.rvcd.{FrameBuffer, RvcdTcpChannel}
import idea.utils.ScaledaIdeaLogger

import com.intellij.openapi.Disposable
import com.intellij.openapi.ui.SimpleToolWindowPanel

import java.awt.image.{BufferedImage, DataBufferUShort}
import java.awt.{Color, Graphics}
import java.nio.ByteBuffer
import javax.swing.{JPanel, Timer}

class NetViewerPanel extends SimpleToolWindowPanel(false, true) with Disposable {

  // private def initHandler: (Option[RvcdRpcGrpc.RvcdRpcBlockingStub], Option[() => ManagedChannel]) = {
  //   val (client, shutdown) = Rvcd()
  //   (Some(client), Some(shutdown))
  // }
  private def initHandler: (Option[RvcdTcpChannel], Option[() => Unit]) = {
    try {
      val (client, shutdown) = RvcdTcpChannel()
      (Some(client), Some(shutdown))
    } catch {
      case e: java.net.ConnectException => {
        ScaledaIdeaLogger.warn("Failed to connect to Rvcd server: " + e.getMessage)
        (None, None)
      }
    }
  }

  private var (client, shutdown) = initHandler

  private val timer = new Timer(100, _ => canvas.repaint())
  timer.start()

  private var lastFrame: Option[FrameBuffer] = None

  private val canvas = new JPanel() {
    override def paintComponent(g: Graphics): Unit = {
      // request frame
      client match {
        case Some(c) =>
          try {
            // val frame = c.requestFrame(new RvcdEmpty())
            c.requestFrame() match {
              case Some(frame) =>
                lastFrame = Some(frame)
              case None =>
            }
            if (lastFrame.isEmpty) {
              return
            }
            val frame = lastFrame.get
            val img = if (frame.width > 0 && frame.height > 0 && frame.data.length > 0) {
              val img        = new BufferedImage(frame.width, frame.height, BufferedImage.TYPE_USHORT_565_RGB)
              val byteBuffer = ByteBuffer.wrap(frame.data)
              // ScaledaIdeaLogger.info(
              //   s"frame ${frame.width}x${frame.height} data length: ${frame.data.size()}, " +
              //     s"bytes: ${byteBuffer.array().slice(0, 10).map("%02X".format(_)).mkString(", ")}"
              // )
              val imgBuffer = img.getRaster.getDataBuffer.asInstanceOf[DataBufferUShort]
              // System.arraycopy(shortBuffer.array(), 0, imgBuffer.getData, 0, shortBuffer.array().length)
              // manually copy
              for (i <- 0 until imgBuffer.getSize) {
                imgBuffer.setElem(i, byteBuffer.getShort())
              }
              img
            } else {
              // draw bitmap
              val img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
              val g2d = img.createGraphics()
              g2d.setColor(Color.red)
              g2d.fillRect(0, 0, 100, 100)
              g2d.setColor(Color.green)
              g2d.fillRect(10, 10, 80, 80)
              g2d.setColor(Color.blue)
              g2d.fillRect(20, 20, 60, 60)
              g2d.setColor(Color.yellow)
              g2d.fillRect(30, 30, 40, 40)
              g2d.setColor(Color.orange)
              g2d.fillRect(40, 40, 20, 20)
              g2d.setColor(Color.pink)
              g2d.fillRect(50, 50, 10, 10)
              g2d.dispose()
              img
            }
            g.drawImage(img, 0, 0, null)
            // repaint()
          } catch {
            case e: Exception =>
              ScaledaIdeaLogger.warn("Error requesting frame", e)
              client = None
          }
        case _ =>
      }
    }
  }
  addFocusListener(new java.awt.event.FocusAdapter() {
    override def focusGained(e: java.awt.event.FocusEvent): Unit = {
      if (client.isEmpty) {
        val (c, s) = initHandler
        client = c
        shutdown = s
      }
    }
  })
  setContent(canvas)

  override def dispose(): Unit = {
    shutdown.foreach(_())
    timer.stop()
  }
}
