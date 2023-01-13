package top.criwits.scaleda
package kernel.net.fuse

import kernel.utils.OS

import jnr.ffi.Pointer
import jnr.ffi.provider.jffi.ByteBufferMemoryIO
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Timespec}
import ru.serce.jnrfuse.{ErrorCodes, FuseFillDir, FuseStubFS}

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributes
import java.util.concurrent.TimeUnit
import scala.sys.process._

class LocalFuse(sourcePath: String) extends FuseStubFS {
  val logger = LoggerFactory.getLogger(getClass)
  require(!OS.isWindows)

  private def getFile(path: String): File = new File(sourcePath, path)

  override def getattr(path: String, stat: FileStat): Int = {
    val file = getFile(path)
    logger.info(s"getattr($path), file: ${file.getAbsolutePath}")
    if (!file.exists()) return -ErrorCodes.ENOENT
    val mode = FuseUtils.fileAttrsUnixToInt(file)
    val p = file.toPath
    val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
    // do not support symbol link now
    stat.st_nlink.set(1)
    stat.st_size.set(attrs.size())
    stat.st_mode.set(mode)
    stat.st_atim.tv_sec.set(attrs.lastAccessTime().to(TimeUnit.SECONDS))
    stat.st_mtim.tv_sec.set(attrs.lastModifiedTime().to(TimeUnit.SECONDS))
    0
  }

  // override def readlink(path: String, buf: Pointer, size: Long) =
  //   super.readlink(path, buf, size)

  override def mknod(path: String, mode: Long, rdev: Long): Int = {
    logger.info(s"mknod(path=$path, mode=${Integer.toOctalString(mode.toInt)})")
    val file = getFile(path)
    s"touch ${file.getAbsolutePath}".!
    chmod(path, mode)
  }

  override def mkdir(path: String, mode: Long): Int = {
    logger.info(s"mkdir(path=$path, mode=${Integer.toOctalString(mode.toInt)})")
    val file = getFile(path)
    if (file.exists() || (file.exists() && file.isFile))
      return -ErrorCodes.ENOENT
    s"mkdir ${file.getAbsolutePath}".!
    chmod(path, mode)
  }

  // override def unlink(path: String) = super.unlink(path)

  override def rmdir(path: String): Int = {
    val file = getFile(path)
    if (!file.exists() || (file.exists() && file.isFile))
      return -ErrorCodes.ENOENT
    s"rm -rf ${file.getAbsolutePath}".!
    0
  }

  // override def symlink(oldpath: String, newpath: String) =
  //   super.symlink(oldpath, newpath)

  override def rename(oldpath: String, newpath: String): Int = {
    val fileOld = getFile(oldpath)
    val fileNew = getFile(newpath)
    s"mv ${fileOld.getAbsolutePath} ${fileNew.getAbsolutePath}".!
    0
  }

  // override def link(oldpath: String, newpath: String) =
  //   super.link(oldpath, newpath)

  override def chmod(path: String, mode: Long) = {
    val file = getFile(path)
    val r = s"chmod ${Integer.toOctalString(mode.toInt & 0x1ff)}".!
    if (r == 0) 0 else -ErrorCodes.ENOENT
  }

  // override def chown(path: String, uid: Long, gid: Long) =
  //   super.chown(path, uid, gid)

  // override def truncate(path: String, size: Long) = super.truncate(path, size)

  // override def open(path: String, fi: FuseFileInfo): Unit = {
  //   val file = getFile(path)
  // }

  override def read(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    val stream = new FileInputStream(file)
    stream.skip(offset)
    val data = stream.readNBytes(size.toInt)
    buf.put(0, data, 0, 0)
    0
  }

  override def write(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    val data = new Array[Byte](size.toInt)
    buf.get(0, data, 0, 0)
    val stream = new FileOutputStream(file)
    stream.write(data, offset.toInt, size.toInt)
    0
  }

  // override def statfs(path: String, stbuf: Statvfs) = super.statfs(path, stbuf)

  // override def flush(path: String, fi: FuseFileInfo) = super.flush(path, fi)
  //
  // override def release(path: String, fi: FuseFileInfo) = super.release(path, fi)

  // override def fsync(path: String, isdatasync: Int, fi: FuseFileInfo) =
  //   super.fsync(path, isdatasync, fi)

  // override def setxattr(
  //     path: String,
  //     name: String,
  //     value: Pointer,
  //     size: Long,
  //     flags: Int
  // ) = super.setxattr(path, name, value, size, flags)
  //
  // override def getxattr(
  //     path: String,
  //     name: String,
  //     value: Pointer,
  //     size: Long
  // ) = super.getxattr(path, name, value, size)

  // override def listxattr(path: String, list: Pointer, size: Long) =
  //   super.listxattr(path, list, size)
  //
  // override def removexattr(path: String, name: String) =
  //   super.removexattr(path, name)

  // override def opendir(path: String, fi: FuseFileInfo) = super.opendir(path, fi)

  override def readdir(
      path: String,
      buf: Pointer,
      filter: FuseFillDir,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    logger.info(s"readdir(path=$path, offset=$offset)")
    val file = getFile(path)
    if (!file.exists() || !file.isFile) return -ErrorCodes.ENOENT
    val list = file.listFiles()
    if (list.length <= offset) return -ErrorCodes.ENOENT
    val byteBuffer =
      ByteBuffer.allocate(list.map(_.getName).max.length * list.length)
    val buffer = new ByteBufferMemoryIO(buf.getRuntime, byteBuffer)
    for (i <- 0 until list.length) {
      if (i >= offset) {
        val f = list(i)
        val nameBuffer = ByteBuffer.allocate(f.getName.length + 1)
        nameBuffer.put(0, f.getName.getBytes, 0, f.getName.length)
        filter.apply(
          buffer,
          nameBuffer,
          Pointer.newIntPointer(buf.getRuntime, 0),
          offset
        )
      }
    }
    0
  }

  // override def releasedir(path: String, fi: FuseFileInfo) =
  //   super.releasedir(path, fi)
  //
  // override def fsyncdir(path: String, fi: FuseFileInfo) =
  //   super.fsyncdir(path, fi)

  override def init(conn: Pointer): Pointer = {
    logger.info("init")
    null
  }

  override def destroy(initResult: Pointer): Unit = {
    logger.info("destroy")
  }

  // override def access(path: String, mask: Int) = super.access(path, mask)

  // override def create(path: String, mode: Long, fi: FuseFileInfo) =
  //   super.create(path, mode, fi)
  //
  // override def ftruncate(path: String, size: Long, fi: FuseFileInfo) =
  //   super.ftruncate(path, size, fi)
  //
  // override def fgetattr(path: String, stbuf: FileStat, fi: FuseFileInfo) =
  //   super.fgetattr(path, stbuf, fi)

  override def utimens(path: String, timespec: Array[Timespec]): Int = {
    logger.info(s"utimens(path=$path, timespec: ${timespec.mkString(", ")})")
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    s"touch ${file.getAbsolutePath}".!
    0
  }
}
