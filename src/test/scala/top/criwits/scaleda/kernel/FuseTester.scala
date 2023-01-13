package top.criwits.scaleda
package kernel

import kernel.utils.{KernelLogger, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import ru.serce.jnrfuse.{FuseException, FuseStubFS}

import java.io.File
import java.nio.file.Paths
import sys.process._

class EmptyFuse extends FuseStubFS
class HelloFuse extends FuseStubFS

class FuseTester extends AnyFlatSpec with should.Matchers {
  behavior of "Fuse"

  def mountFs(
      fs: FuseStubFS,
      mountPoint: String,
      blocking: Boolean = true
  ): Unit = {
    try {
      KernelLogger.warn(s"mounting to $mountPoint")
      val path = Paths.get(mountPoint)
      val file = new File(mountPoint)
      if (!file.exists()) KernelLogger.warn("creating dirs with returns:", file.mkdirs())
      try {
        fs.mount(path, blocking)
      } catch {
        case _: FuseException => {
          s"umount $mountPoint".!
          fs.mount(path, blocking)
        }
      }
    } catch {
      case e: InterruptedException => KernelLogger.warn("mount interrupted cause:", e)
    } finally {
      fs.umount()
      KernelLogger.info(s"umount done")
    }
  }

  def testPath = OS.getOSType match {
    case OS.Windows => "J:\\"
    case _          => "/tmp/mnth"
  }

  it should "test EmptyFuse" in {
    val path = testPath
    val t = new Thread(() => mountFs(new EmptyFuse, path))
    t.setDaemon(true)
    t.start()
    Thread.sleep(1000)
    s"ls $path".!
    t.interrupt()
    Thread.sleep(300)
    KernelLogger.info("all done")
  }
  it should "test mount and unmount" in {}
}
