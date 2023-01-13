package top.criwits.scaleda
package kernel.net.fuse

import kernel.utils.KernelLogger

import ru.serce.jnrfuse.{FuseException, FuseStubFS}

import java.io.File
import java.nio.file.Paths
import scala.sys.process._

object FuseUtils {
  def mountFs(
      fs: FuseStubFS,
      mountPoint: String,
      blocking: Boolean = true
  ): Unit = {
    try {
      KernelLogger.warn(s"mounting to $mountPoint")
      val path = Paths.get(mountPoint)
      val file = new File(mountPoint)
      if (!file.exists())
        KernelLogger.warn("creating dirs with returns:", file.mkdirs())
      try {
        fs.mount(path, blocking)
      } catch {
        case _: FuseException => {
          s"umount $mountPoint".!
          fs.mount(path, blocking)
        }
      }
    } catch {
      case e: InterruptedException =>
        KernelLogger.warn("mount interrupted cause:", e)
    } finally {
      fs.umount()
      KernelLogger.info(s"umount done")
    }
  }
}
