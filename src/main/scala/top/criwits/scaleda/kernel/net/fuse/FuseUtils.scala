package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.{EmptyReq, IntReply, PathRequest}
import kernel.utils.{KernelFileUtils, KernelLogger, OS}

import ru.serce.jnrfuse.{FuseException, FuseStubFS}

import java.io.{File, PrintWriter}
import java.nio.file.attribute.{PosixFileAttributes, PosixFilePermissions}
import java.nio.file.{Files, Paths}
import scala.language.implicitConversions
import scala.sys.process._

object FuseUtils {
  val debug = false

  /** Mount a filesystem, will retry if failed
    * @param fs filesystem
    * @param mountPoint mount point
    * @param blocking will block current thread until umount
    */
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
        KernelLogger.info("creating dirs with returns:", file.mkdirs())
      if (OS.isWindows) {
        if (file.exists()) {
          if (file.isFile) file.delete()
          else KernelFileUtils.deleteDirectory(file.toPath)
        } else {
          file.mkdirs()
          KernelFileUtils.deleteDirectory(file.toPath)
        }
      }
      def doMount(): Unit = {
        val options = Array("-o", "allow_other", "-o", "fsname=scaleda-fs")
        KernelLogger.info(
          s"doMount(path=$path, blocking=$blocking, debug=$debug), target path exists: ${path.toFile.exists()}"
        )
        fs.mount(path, blocking, debug, options)
      }
      try {
        doMount()
      } catch {
        case e: FuseException =>
          KernelLogger.warn("retrying mount:", e)
          if (!OS.isWindows) {
            try {
              s"""umount \"$mountPoint\"""".!
            } catch {
              case e: Throwable => KernelLogger.warn("cannot umount:", e)
            }
          } else fs.umount()
          doMount()
      }
    } catch {
      case e: InterruptedException =>
        KernelLogger.warn("mount interrupted cause:", e)
    } finally {
      // fs.umount()
      // KernelLogger.info(s"umount done")
    }
  }

  def fileAttrsUnixString(file: File): String = {
    val path  = file.toPath
    val attrs = Files.readAttributes(path, classOf[PosixFileAttributes])
    val perms = attrs.permissions()
    PosixFilePermissions.toString(perms)
  }

  def fileAttrsToInt(file: File, str: String): Int = {
    def bitToInt(c: Char): Int = if (c == '-') 0 else 1
    def groupToInt(group: String): Int =
      (0 until 3).map(i => bitToInt(group.charAt(i)) << (2 - i)).sum
    (0 until 3)
      .map(_ * 3)
      .map(i => groupToInt(str.slice(i, i + 3)) << (6 - i))
      .sum | ((if (file.isDirectory) 4 else 8) << 12)
  }

  def fileAttrsUnixToInt(file: File): Int = {
    val str = fileAttrsUnixString(file)
    fileAttrsToInt(file, str)
  }

  def printTextToFile(content: String, file: File): Unit = {
    if (!file.exists()) {
      file.getParentFile.mkdirs()
      // FileUtils.touch(file)
      s"touch ${file.getAbsoluteFile}".!
    }
    val printer = new PrintWriter(file)
    printer.print(content)
    printer.close()
  }

  def loadLibraries(): Unit = {
    if (OS.isWindows) {
      // System.setProperty("jnrfuse.winfsp.path", "D:\\Programs\\scaleda\\src\\main\\resources\\bin\\winfsp_x64.dll")
    }
  }

  object Converts {
    implicit def convert(a: Unit): EmptyReq = EmptyReq()

    implicit def convert(a: Int): IntReply = IntReply.of(a)

    implicit def convert(a: PathRequest): String = a.path
  }
}
