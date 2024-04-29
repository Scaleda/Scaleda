package top.scaleda
package idea.windows.bottomPanel.netviewer

import idea.rvcd.{FrameBuffer, Rvcd, RvcdFrameBufferChannel}
import idea.utils.ScaledaIdeaLogger

import com.intellij.openapi.Disposable
import com.intellij.openapi.ui.SimpleToolWindowPanel
import io.grpc.ManagedChannel
import rvcd.rvcd.{EventType, RvcdInputEvent, RvcdRpcGrpc}

import java.awt.image.{BufferedImage, DataBufferUShort}
import java.awt.{Color, Graphics}
import java.nio.ByteBuffer
import javax.swing.{JPanel, Timer}

class NetViewerPanel extends SimpleToolWindowPanel(false, true) with Disposable {

  private def initRpcHandler: (Option[RvcdRpcGrpc.RvcdRpcBlockingStub], Option[() => ManagedChannel]) = {
    val (client, shutdown) = Rvcd()
    (Some(client), Some(shutdown))
  }

  private def initFrameBufferHandler: (Option[RvcdFrameBufferChannel], Option[() => Unit]) = {
    try {
      val (client, shutdown) = RvcdFrameBufferChannel.unix()
      (Some(client), Some(shutdown))
    } catch {
      case e: java.net.ConnectException =>
        ScaledaIdeaLogger.info("Failed to connect to Rvcd server: " + e.getMessage)
        (None, None)
    }
  }

  private var (fbClient, fbShutdown)   = initFrameBufferHandler
  private var (rpcClient, rpcShutdown) = initRpcHandler

  private val timer = new Timer(8, _ => canvas.repaint())
  timer.start()

  private var lastFrame: Option[FrameBuffer] = None

  private val canvas = new JPanel() {
    override def paintComponent(g: Graphics): Unit = {
      // request frame
      fbClient match {
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
              val imgBuffer  = img.getRaster.getDataBuffer.asInstanceOf[DataBufferUShort]
              // manually copy
              // for (i <- 0 until imgBuffer.getSize) {
              //   imgBuffer.setElem(i, byteBuffer.getShort())
              // }
              // copy from byte buffer to short buffer
              val shortBuffer = imgBuffer.getData
              byteBuffer.asShortBuffer().get(shortBuffer)
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
              fbClient = None
          }
        case _ =>
      }
    }
  }
  addFocusListener(new java.awt.event.FocusAdapter() {
    override def focusGained(e: java.awt.event.FocusEvent): Unit = {
      if (fbClient.isEmpty) {
        val (c, s) = initFrameBufferHandler
        fbClient = c
        fbShutdown = s
      }
      if (rpcClient.isEmpty) {
        val (c, s) = initRpcHandler
        rpcClient = c
        rpcShutdown = s
      }
    }
  })
  canvas.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsAdapter() {
    override def ancestorResized(e: java.awt.event.HierarchyEvent): Unit = {
      val size = canvas.getSize
      // println(s"canvas resized: $size")
      if (size.width > 0 && size.height > 0) {
        val width  = size.width
        val height = size.height
        try {
          rpcClient.foreach(
            _.inputEvent(RvcdInputEvent().withType(EventType.EVENT_TYPE_RESIZE).withX(width).withY(height))
          )
        } catch {
          case e: io.grpc.StatusRuntimeException =>
            ScaledaIdeaLogger.warn("Failed to send resize event", e)
            rpcClient = None
        }
      }
    }
  })
  // canvas.addContainerListener()
  canvas.addMouseListener(new java.awt.event.MouseAdapter() {
    override def mouseClicked(e: java.awt.event.MouseEvent): Unit = {
      println(s"mouseClicked: $e")
      try {
        rpcClient.foreach(
          _.inputEvent(
            RvcdInputEvent().withType(EventType.EVENT_TYPE_CLICK).withX(e.getX).withY(e.getY).withButton(e.getButton)
          )
        )
      } catch {
        case e: io.grpc.StatusRuntimeException =>
          ScaledaIdeaLogger.warn("Failed to send click event", e)
          rpcClient = None
      }
    }
  })
  setContent(canvas)

  override def dispose(): Unit = {
    fbShutdown.foreach(_())
    rpcShutdown.foreach(_())
    timer.stop()
  }
}
