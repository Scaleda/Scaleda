package top.scaleda
package idea.windows.bottomPanel.netviewer

import idea.rvcd.{FrameBuffer, Rvcd, RvcdFrameBufferChannel, RvcdService}
import idea.utils.ScaledaIdeaLogger
import kernel.shell.ScaledaRunKernelHandler
import kernel.shell.command.{CommandDeps, CommandRunner}

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.ui.SimpleToolWindowPanel
import io.grpc.ManagedChannel
import rvcd.rvcd.{EventType, RvcdInputEvent, RvcdRpcGrpc}

import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.image.{BufferedImage, DataBufferUShort}
import java.nio.ByteBuffer
import javax.swing.{JPanel, Timer}
import scala.collection.mutable

class NetViewerPanel extends SimpleToolWindowPanel(false, true) with Disposable {

  private def initRpcHandler: (Option[RvcdRpcGrpc.RvcdRpcBlockingStub], Option[() => ManagedChannel]) = {
    val (client, shutdown) = Rvcd()
    (Some(client), Some(shutdown))
  }

  private def initFrameBufferHandler: (Option[RvcdFrameBufferChannel], Option[() => Unit]) = {
    try {
      val (client, shutdown) = RvcdFrameBufferChannel.unix()
      // val (client, shutdown) = RvcdFrameBufferChannel.tcp()
      (Some(client), Some(shutdown))
    } catch {
      case e: java.net.SocketException =>
        ScaledaIdeaLogger.info("Failed to connect to Rvcd server: " + e.getMessage)
        (None, None)
    }
  }

  private var (fbClient, fbShutdown)   = initFrameBufferHandler
  private var (rpcClient, rpcShutdown) = initRpcHandler
  private var resized                  = false

  private val eventQueue = mutable.Queue.empty[RvcdInputEvent]

  private def enqueueEvent(event: RvcdInputEvent): Unit = {
    eventQueue.synchronized {
      eventQueue.enqueue(event)
    }
  }

  private def sendEventsRpc(): Unit = {
    if (eventQueue.nonEmpty) {
      eventQueue.synchronized {
        try {
          rpcClient.foreach(_.inputEvent(eventQueue.dequeue()))
        } catch {
          case e: io.grpc.StatusRuntimeException =>
            ScaledaIdeaLogger.warn("Failed to send event", e)
            rpcClient = None
        }
      }
    }
  }

  private def sendEventsSocket(): Unit = {
    if (eventQueue.nonEmpty) {
      eventQueue.synchronized {
        try {
          fbClient.foreach(_.inputEvent(eventQueue.dequeue()))
        } catch {
          case e: Exception =>
            ScaledaIdeaLogger.warn("Failed to send event", e)
            fbClient = None
        }
      }
    }
  }

  private val sendEventsThread = new Thread(() => {
    try {
      while (true) {
        // sendEventsRpc()
        sendEventsSocket()
        Thread.sleep(1)
      }
    } catch {
      case _: InterruptedException =>
    }
  })
  // sendEventsThread.start()

  private val timer = new Timer(1, _ => canvas.repaint())
  timer.start()

  private var lastFrame: Option[FrameBuffer] = None

  private val canvas = new JPanel() {
    override def paintComponent(g: Graphics): Unit = {
      // request frame
      fbClient match {
        case Some(c) =>
          if (!resized) {
            resized = true
            val sz = this.getSize
            enqueueEvent(RvcdInputEvent().withType(EventType.EVENT_TYPE_RESIZE).withX(sz.width).withY(sz.height))
          }
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
            val frame      = lastFrame.get
            val img        = new BufferedImage(frame.width, frame.height, BufferedImage.TYPE_USHORT_565_RGB)
            val byteBuffer = ByteBuffer.wrap(frame.data)
            val imgBuffer  = img.getRaster.getDataBuffer.asInstanceOf[DataBufferUShort]
            // copy from byte buffer to short buffer
            val shortBuffer = imgBuffer.getData
            byteBuffer.asShortBuffer().get(shortBuffer)

            // test argb
            // val pixelsLen = frame.width * frame.height
            // println(s"Frame size: ${frame.width} x ${frame.height}, pixels: $pixelsLen, data len: ${frame.data.length}")
            // // val img = new BufferedImage(
            // //   frame.width,
            // //   frame.height,
            // //   BufferedImage.TYPE_4BYTE_ABGR_PRE,
            // //   new ComponentColorModel(
            // //     8,
            // //     pixelsLen,
            // //     frame.data,
            // //     0,
            // //     true
            // //   )
            // // )
            // val buffer = ByteBuffer.wrap(frame.data)
            // // val bufferInt = buffer.asIntBuffer()
            // // val dataBuffer = new DataBufferByte(frame.data, frame.data.length)
            // // val array      = bufferInt.array()
            // // val array = Array[Int](pixelsLen)
            // // buffer.asIntBuffer().get(array)
            // // val dataBuffer = new DataBufferInt(array, array.length)
            // val colorModel = ColorModel.getRGBdefault.asInstanceOf[DirectColorModel]
            // // val raster = Raster.createPackedRaster(
            // //   dataBuffer,
            // //   frame.width,
            // //   frame.height,
            // //   frame.width,
            // //   colorModel.getMasks,
            // //   new Point(0, 0)
            // // )
            // val raster = colorModel.createCompatibleWritableRaster(frame.width, frame.height)
            // buffer.asIntBuffer().get(raster.getDataBuffer.asInstanceOf[DataBufferInt].getData())
            // val img = new BufferedImage(colorModel, raster, false, null)
            g.drawImage(img, 0, 0, null)
          } catch {
            case e: Exception =>
              // e.printStackTrace()
              ScaledaIdeaLogger.warn("Error requesting frame", e)
              fbClient = None
          }

          if (!sendEventsThread.isAlive)
            sendEventsSocket()
        // sendEventsRpc()
        case _ =>
      }
    }
  }

  private def reConnect(): Unit = {
    if (fbClient.isEmpty) {
      val (c, s) = initFrameBufferHandler
      fbClient = c
      fbShutdown = s
      resized = false
    }
    if (rpcClient.isEmpty) {
      val (c, s) = initRpcHandler
      rpcClient = c
      rpcShutdown = s
    }
  }

  addFocusListener(new java.awt.event.FocusAdapter() {
    override def focusGained(e: java.awt.event.FocusEvent): Unit = {
      reConnect()
    }
  })
  canvas.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsAdapter() {
    override def ancestorResized(e: java.awt.event.HierarchyEvent): Unit = {
      val size = canvas.getSize
      // println(s"canvas resized: $size")
      if (size.width > 0 && size.height > 0) {
        val width  = size.width
        val height = size.height
        val event  = RvcdInputEvent().withType(EventType.EVENT_TYPE_RESIZE).withX(width).withY(height)
        enqueueEvent(event)
      }
    }
  })
  // canvas.addContainerListener()
  private def sendMouseEvent(e: java.awt.event.MouseEvent, eventType: EventType, data: Int): Unit = {
    val event = RvcdInputEvent().withType(eventType).withX(e.getX).withY(e.getY).withButton(e.getButton).withData(data)
    enqueueEvent(event)
  }
  canvas.addMouseListener(new java.awt.event.MouseAdapter() {
    override def mousePressed(e: java.awt.event.MouseEvent): Unit = {
      sendMouseEvent(e, EventType.EVENT_TYPE_CLICK, 1)
    }
    override def mouseReleased(e: java.awt.event.MouseEvent): Unit = {
      sendMouseEvent(e, EventType.EVENT_TYPE_CLICK, 0)
    }
  })
  canvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
    override def mouseMoved(e: java.awt.event.MouseEvent): Unit = {
      val event = RvcdInputEvent().withType(EventType.EVENT_TYPE_POINTER_MOVEMENT).withX(e.getX).withY(e.getY)
      enqueueEvent(event)
    }

    override def mouseDragged(e: MouseEvent): Unit = mouseMoved(e)
  })
  canvas.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
    override def mouseWheelMoved(e: java.awt.event.MouseWheelEvent): Unit = {
      if ((e.getModifiersEx & java.awt.event.InputEvent.CTRL_DOWN_MASK) != 0) {
        var data  = 1000
        val scale = 0.91
        if (e.getPreciseWheelRotation > 0.01) {
          data = (1000.0 * scale).toInt
        } else if (e.getPreciseWheelRotation < 0.01) {
          data = (1000.0 / scale).toInt
        }
        val event = RvcdInputEvent()
          .withType(EventType.EVENT_TYPE_ZOOM)
          .withData(data)
        enqueueEvent(event)
      } else {
        var x = 0.0
        var y = 0.0
        if ((e.getModifiersEx & java.awt.event.InputEvent.SHIFT_DOWN_MASK) != 0) {
          x = e.getPreciseWheelRotation * -50.0
        } else {
          y = e.getPreciseWheelRotation * -50.0
        }
        // println(s"Mouse wheel moved: x=$x, y=$y, e=$e")
        val event = RvcdInputEvent()
          .withType(EventType.EVENT_TYPE_WHEEL)
          .withX(x.toInt)
          .withY(y.toInt)
        enqueueEvent(event)
      }
    }
  })
  setContent(canvas)

  private val launchRvcdRunnable: Runnable = () => {
    CommandRunner.execute(
      Seq(
        CommandDeps(args = Seq(RvcdService.rvcdFile.getAbsolutePath, "--hidden"), description = "Start RVCD Instance")
      ),
      ScaledaRunKernelHandler
    )
  }
  private var launchRvcdThread: Option[Thread] = None

  private var windowVisible = true

  val group = new DefaultActionGroup()
  group.add(new AnAction(AllIcons.Actions.Execute) {
    override def actionPerformed(e: AnActionEvent): Unit = {
      if (launchRvcdThread.exists(_.isAlive)) {
        // stop
        launchRvcdThread.foreach(_.interrupt())
        launchRvcdThread = None
        fbShutdown.foreach(_())
        fbShutdown = None
        fbClient = None
      } else {
        launchRvcdThread = Some({
          val t = new Thread(launchRvcdRunnable)
          t.start()
          t
        })
        windowVisible = false
        new Thread(() => {
          Thread.sleep(500)
          reConnect()
        }).start()
      }
    }
    override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT

    override def update(e: AnActionEvent): Unit = {
      if (launchRvcdThread.exists(_.isAlive)) {
        e.getPresentation.setIcon(AllIcons.Actions.Cancel)
      } else {
        e.getPresentation.setIcon(AllIcons.Actions.Execute)
        e.getPresentation.setEnabled(fbClient.isEmpty)
      }
    }
  })
  group.add(new AnAction(AllIcons.Ide.Link) {
    override def actionPerformed(e: AnActionEvent): Unit = {
      reConnect()
    }
    override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT
    override def update(e: AnActionEvent): Unit = {
      e.getPresentation.setEnabled(fbClient.isEmpty || rpcClient.isEmpty)
    }
  })
  group.add(new AnAction(AllIcons.Actions.ToggleVisibility) {
    override def actionPerformed(e: AnActionEvent): Unit = {
      windowVisible = !windowVisible
      fbClient.foreach(
        _.inputEvent(RvcdInputEvent().withType(EventType.EVENT_TYPE_VISIBLE).withData(if (windowVisible) 1 else 0))
      )
    }
    override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.BGT
    override def update(e: AnActionEvent): Unit = {
      e.getPresentation.setEnabled(fbClient.isDefined)
      e.getPresentation.setPerformGroup(windowVisible)
    }
  })
  val toolbar: ActionToolbar = {
    ActionManager
      .getInstance()
      .createActionToolbar(
        "NetView Toolbar",
        group,
        false
      )
  }

  setToolbar(toolbar.getComponent)
  toolbar.setTargetComponent(this)

  override def dispose(): Unit = {
    fbShutdown.foreach(_())
    rpcShutdown.foreach(_())
    timer.stop()

    if (sendEventsThread.isAlive)
      sendEventsThread.interrupt()

    if (launchRvcdThread.exists(_.isAlive))
      launchRvcdThread.foreach(_.interrupt())
    launchRvcdThread = None
  }
}
