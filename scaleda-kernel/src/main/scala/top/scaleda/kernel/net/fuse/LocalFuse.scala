package top.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseUtils.printTextToFile
import kernel.utils.{KernelLogger, OS}

import jnr.ffi.Pointer
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Statvfs, Timespec}
import ru.serce.jnrfuse.{ErrorCodes, FuseFillDir, FuseStubFS}

import java.io.{File, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributes
import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.io.Source
import scala.sys.process._

/** Map path to another path through Fuse
  * @param sourcePath source data path
  */
class LocalFuse(sourcePath: String) extends FuseStubFS {
  private val logger = LoggerFactory.getLogger(getClass)
  require(!OS.isWindows)

  protected def getFile(path: String): File = new File(sourcePath, path)

  override def getattr(path: String, stat: FileStat): Int = {
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    var mode = FuseUtils.fileAttrsUnixToInt(file)
    if (Files.isSymbolicLink(file.toPath)) {
      mode = (mode & 0xfff) | (0xa << 12)
    }
    val p     = file.toPath
    val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
    // stat.st_nlink.set(1)
    val size = attrs.size()
    stat.st_size.set(size)
    stat.st_mode.set(mode)
    stat.st_atim.tv_sec.set(attrs.lastAccessTime().to(TimeUnit.SECONDS))
    stat.st_mtim.tv_sec.set(attrs.lastModifiedTime().to(TimeUnit.SECONDS))
    logger.debug(
      s"getattr($path), file: ${file.getAbsolutePath}, mode: ${Integer
        .toOctalString(mode)}, size: ${size}"
    )
    0
  }

  override def readlink(path: String, buf: Pointer, size: Long): Int = {
    logger.debug(s"readlink(path=$path)")
    val file = getFile(path)
    if (!Files.isSymbolicLink(file.toPath)) return -ErrorCodes.ENOENT
    // val res = Files.readSymbolicLink(file.toPath).toFile.getAbsolutePath
    val run   = s"readlink ${file.getAbsolutePath}"
    val array = mutable.ArrayBuffer[String]()
    val r = run ! ProcessLogger(
      stdout => array.addOne(stdout),
      _ => {}
    )
    val res = array.mkString("")
    val len = math.min(res.length, size.toInt)
    buf.put(0, res.getBytes(), 0, len)
    buf.putByte(len, 0)
    0
  }

  override def mknod(path: String, mode: Long, rdev: Long): Int = {
    logger.debug("mknod")
    super.mknod(path, mode, rdev)
  }

  override def mkdir(path: String, mode: Long): Int = {
    logger.debug(s"mkdir(path=$path, mode=${Integer.toOctalString(mode.toInt)})")
    val file = getFile(path)
    if (file.exists() || (file.exists() && file.isFile))
      return -ErrorCodes.ENOENT
    s"mkdir ${file.getAbsolutePath}".!
    chmod(path, mode)
  }

  override def unlink(path: String): Int = {
    logger.debug(s"unlink(path=$path)")
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    if (file.exists() && file.isDirectory) return -ErrorCodes.EISDIR
    file.delete()
    0
  }

  override def rmdir(path: String): Int = {
    val file = getFile(path)
    if (!file.exists() || (file.exists() && file.isFile))
      return -ErrorCodes.ENOENT
    file.delete()
    0
  }

  override def symlink(oldpath: String, newpath: String): Int = {
    logger.debug(s"symlink(old=$oldpath, new=$newpath)")
    // oldpath is stored as string
    val fileOld = new File(oldpath)
    val fileNew = getFile(newpath)
    Files.createSymbolicLink(fileNew.toPath, fileOld.toPath)
    0
  }

  override def rename(oldpath: String, newpath: String): Int = {
    val fileOld = getFile(oldpath)
    val fileNew = getFile(newpath)
    s"mv ${fileOld.getAbsolutePath} ${fileNew.getAbsolutePath}".!
    0
  }

  override def link(oldpath: String, newpath: String) = {
    logger.debug("link")
    super.link(oldpath, newpath)
  }

  override def chmod(path: String, mode: Long) = {
    val file = getFile(path)
    val run =
      s"chmod ${Integer.toOctalString(mode.toInt & 0xfff)} ${file.getAbsolutePath}"
    logger.debug(
      s"chmod(path=$path, mode=${Integer.toOctalString(mode.toInt)}), run: $run"
    )
    val r = run.!
    if (r == 0) 0 else -ErrorCodes.ENOENT
  }

  override def chown(path: String, uid: Long, gid: Long) = {
    logger.debug("chown")
    super.chown(path, uid, gid)
  }

  override def truncate(path: String, size: Long) = {
    logger.debug(s"truncate(path=$path, size=$size)")
    super.truncate(path, size)
  }

  override def open(path: String, fi: FuseFileInfo): Int = {
    logger.debug(s"open(path=$path)")
    super.open(path, fi)
  }

  override def read(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    if (file.isDirectory) return -ErrorCodes.EISDIR
    logger.debug(s"read(path=$path, size=$size, offset=$offset)")
    val rf = new RandomAccessFile(file, "r")
    rf.seek(offset)
    val data = new Array[Byte](size.toInt)
    // warning: this func bloking thread
    rf.read(data, 0, size.toInt)
    // val stream = new FileInputStream(file)
    // stream.skip(offset)
    // val data = stream.readNBytes(size.toInt)
    // logger.debug(s"read: got data ${data.length} bytes")
    buf.put(0, data, 0, data.length)
    data.length
  }

  override def write(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    logger.debug(s"write(path=$path, size=$size, offset=$offset)")
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    if (file.isDirectory) return -ErrorCodes.EISDIR
    try {
      val rf = new RandomAccessFile(file, "rw")
      rf.seek(offset)
      val data = new Array[Byte](size.toInt)
      buf.get(0, data, 0, size.toInt)
      rf.write(data)
      rf.close()
      size.toInt
    } catch {
      case e: Throwable =>
        logger.error(s"ERROR when write: $e")
        throw e
    }
  }

  override def statfs(path: String, stbuf: Statvfs) = {
    logger.debug("statfs")
    super.statfs(path, stbuf)
  }

  override def flush(path: String, fi: FuseFileInfo) = {
    logger.debug("flush")
    super.flush(path, fi)
  }

  override def release(path: String, fi: FuseFileInfo) = {
    logger.debug("release")
    super.release(path, fi)
  }

  override def fsync(path: String, isdatasync: Int, fi: FuseFileInfo) = {
    logger.debug("fsync")
    super.fsync(path, isdatasync, fi)
  }

  override def setxattr(
      path: String,
      name: String,
      value: Pointer,
      size: Long,
      flags: Int
  ): Int = {
    logger.debug(s"setxattr(path=$path, name=$name, size=$size)")
    // 0
    super.setxattr(path, name, value, size, flags)
  }

  override def getxattr(
      path: String,
      name: String,
      value: Pointer,
      size: Long
  ): Int = {
    val file = getFile(path)
    logger.debug(
      s"getxattr(path=$path, name=$name, size=$size) file: ${file.getAbsoluteFile}"
    )
    if (!file.exists()) return -ErrorCodes.ENOENT
    super.getxattr(path, name, value, size)
  }

  override def listxattr(path: String, list: Pointer, size: Long) = {
    logger.debug("listxattr")
    super.listxattr(path, list, size)
  }

  override def removexattr(path: String, name: String) = {
    logger.debug("removexattr")
    super.removexattr(path, name)
  }

  override def opendir(path: String, fi: FuseFileInfo) = {
    logger.debug("opendir")
    super.opendir(path, fi)
  }

  override def readdir(
      path: String,
      buf: Pointer,
      filter: FuseFillDir,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val file = getFile(path)
    if (!file.exists() || !file.isDirectory) return -ErrorCodes.ENOENT
    var offsetNow = offset
    def applyFilter(filename: String): Int = {
      val nameBuffer = ByteBuffer.allocate(filename.length + 1)
      // nameBuffer.put(filename.getBytes)
      nameBuffer.put(filename.getBytes, 0, filename.length)
      offsetNow += 1
      filter.apply(buf, nameBuffer, null, offsetNow)
      0
    }
    logger.debug(
      s"readdir(path=$path, offset=$offset), file=${file.getAbsolutePath}"
    )
    val list = file.listFiles()
    logger.debug(s"listed files: ${list.mkString(", ")}")
    if (offsetNow == 0) applyFilter(".")
    if (offsetNow == 1) applyFilter("..")
    if (list.length + 2 == offsetNow) return 0
    if (list.length + 2 < offsetNow) return -ErrorCodes.ENOENT
    list
      .slice(offsetNow.toInt - 2, list.length)
      .headOption
      .map(f => {
        logger.debug(
          s"readdir: putting file ${f.getAbsolutePath}, name: ${f.getName}"
        )
        applyFilter(f.getName)
      })
      .getOrElse(-ErrorCodes.ENOENT)
  }

  override def releasedir(path: String, fi: FuseFileInfo) = {
    logger.debug("releasedir")
    super.releasedir(path, fi)
  }

  override def fsyncdir(path: String, fi: FuseFileInfo) = {
    logger.debug("fsyncdir")
    super.fsyncdir(path, fi)
  }

  override def init(conn: Pointer): Pointer = {
    logger.debug("init")
    null
  }

  override def destroy(initResult: Pointer): Unit = {
    logger.debug("destroy")
  }

  override def access(path: String, mask: Int) = {
    logger.debug("access")
    super.access(path, mask)
  }

  override def create(path: String, mode: Long, fi: FuseFileInfo): Int = {
    logger.debug(
      s"create(path=$path, mode=${Integer.toOctalString(mode.toInt)})"
    )
    val file = getFile(path)
    // s"touch ${file.getAbsolutePath}".!
    if (file.exists() && file.isDirectory) return -ErrorCodes.EISDIR
    if (!file.createNewFile()) return -ErrorCodes.EIO
    chmod(path, mode)
  }

  override def ftruncate(path: String, size: Long, fi: FuseFileInfo) = {
    logger.debug("ftruncate")
    super.ftruncate(path, size, fi)
  }

  override def fgetattr(path: String, stbuf: FileStat, fi: FuseFileInfo) = {
    logger.debug("fgetattr")
    super.fgetattr(path, stbuf, fi)
  }

  override def utimens(path: String, timespec: Array[Timespec]): Int = {
    logger.debug(s"utimens(path=$path, timespec: ${timespec.mkString(", ")})")
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    s"touch ${file.getAbsolutePath}".!
    0
  }
}

object LocalFuse {
  // !! NEVER RUN THIS !!
  // !! YOUR JVM WILL CRASH AND CANNOT RESTART !!
  def _main(args: Array[String]): Unit = {
    val source = "/tmp/mnt-source"
    val dest   = "/tmp/mnt"
    s"mkdir -p $source".!
    val content = "file content"
    printTextToFile(content, new File(source, "file.txt"))
    s"rm -rf $dest".!
    s"mkdir -p $dest".!
    val fs = new LocalFuse(source)
    try {
      FuseUtils.mountFs(fs, dest, blocking = false)
      s"ls -lahi $dest".!
      // s"cat $dest/file.txt".!
      assert(
        Source
          .fromFile(new File(dest, "file.txt"))
          .getLines()
          .mkString("\n") == content
      )
      printTextToFile(content, new File(dest, "file2.txt"))
      assert(
        Source
          .fromFile(new File(dest, "file2.txt"))
          .getLines()
          .mkString("\n") == content
      )
      // Thread.sleep(10000000)
      val code =
        """#include <stdio.h>
          |int main() {
          |  puts("hello!");
          |  return 0;
          |}
          |""".stripMargin
      val codeFile = new File(dest, "code.c")
      printTextToFile(code, codeFile)
      s"gcc ${codeFile.getAbsolutePath} -o c".!
      val executableFile = new File(codeFile.getParentFile, "c")
      s"${executableFile.getAbsolutePath}".!
    } finally {
      fs.umount()
    }
    KernelLogger.debug("test finished")
  }
}
