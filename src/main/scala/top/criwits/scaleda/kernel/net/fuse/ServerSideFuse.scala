package top.criwits.scaleda
package kernel.net.fuse

import jnr.ffi.Pointer
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo}
import ru.serce.jnrfuse.{ErrorCodes, FuseFillDir}

import java.io.{File, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributes
import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.sys.process._

class ServerSideFuse(sourcePath: String) extends LocalFuse(sourcePath) {
  val logger = LoggerFactory.getLogger(getClass)

  override def getattr(path: String, stat: FileStat): Int = {
    val file = getFile(path)
    if (!file.exists()) return -ErrorCodes.ENOENT
    var mode = FuseUtils.fileAttrsUnixToInt(file)
    if (Files.isSymbolicLink(file.toPath)) {
      mode = (mode & 0xfff) | (0xa << 12)
    }
    val p = file.toPath
    val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
    // stat.st_nlink.set(1)
    val size = attrs.size()
    stat.st_size.set(size)
    stat.st_mode.set(mode)
    stat.st_atim.tv_sec.set(attrs.lastAccessTime().to(TimeUnit.SECONDS))
    stat.st_mtim.tv_sec.set(attrs.lastModifiedTime().to(TimeUnit.SECONDS))
    logger.info(
      s"getattr($path), file: ${file.getAbsolutePath}, mode: ${Integer
        .toOctalString(mode)}, size: ${size}"
    )
    0
  }

  override def readlink(path: String, buf: Pointer, size: Long): Int = {
    logger.info(s"readlink(path=$path)")
    val file = getFile(path)
    if (!Files.isSymbolicLink(file.toPath)) return -ErrorCodes.ENOENT
    val run = s"readlink ${file.getAbsolutePath}"
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

  override def mkdir(path: String, mode: Long): Int = {
    logger.info(s"mkdir(path=$path, mode=${Integer.toOctalString(mode.toInt)})")
    val file = getFile(path)
    if (file.exists() || (file.exists() && file.isFile))
      return -ErrorCodes.ENOENT
    s"mkdir ${file.getAbsolutePath}".!
    chmod(path, mode)
  }

  override def unlink(path: String): Int = {
    logger.info(s"unlink(path=$path)")
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
    logger.info(s"symlink(old=$oldpath, new=$newpath)")
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

  override def chmod(path: String, mode: Long) = {
    val file = getFile(path)
    val run =
      s"chmod ${Integer.toOctalString(mode.toInt & 0xfff)} ${file.getAbsolutePath}"
    logger.info(
      s"chmod(path=$path, mode=${Integer.toOctalString(mode.toInt)}), run: $run"
    )
    val r = run.!
    if (r == 0) 0 else -ErrorCodes.ENOENT
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
    logger.info(s"read(path=$path, size=$size, offset=$offset)")
    val rf = new RandomAccessFile(file, "r")
    rf.seek(offset)
    val data = new Array[Byte](size.toInt)
    // warning: this func bloking thread
    rf.read(data, 0, size.toInt)
    // val stream = new FileInputStream(file)
    // stream.skip(offset)
    // val data = stream.readNBytes(size.toInt)
    // logger.info(s"read: got data ${data.length} bytes")
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
    logger.info(s"write(path=$path, size=$size, offset=$offset)")
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
      nameBuffer.put(0, filename.getBytes, 0, filename.length)
      offsetNow += 1
      filter.apply(buf, nameBuffer, null, offsetNow)
      0
    }
    logger.info(
      s"readdir(path=$path, offset=$offset), file=${file.getAbsolutePath}"
    )
    val list = file.listFiles()
    logger.info(s"listed files: ${list.mkString(", ")}")
    if (offsetNow == 0) applyFilter(".")
    if (offsetNow == 1) applyFilter("..")
    if (list.length + 2 == offsetNow) return 0
    if (list.length + 2 < offsetNow) return -ErrorCodes.ENOENT
    list
      .slice(offsetNow.toInt - 2, list.length)
      .headOption
      .map(f => {
        logger.info(
          s"readdir: putting file ${f.getAbsolutePath}, name: ${f.getName}"
        )
        applyFilter(f.getName)
      })
      .getOrElse(-ErrorCodes.ENOENT)
  }

  override def init(conn: Pointer): Pointer = {
    logger.info("init")
    null
  }

  override def destroy(initResult: Pointer): Unit = {
    logger.info("destroy")
  }

  override def create(path: String, mode: Long, fi: FuseFileInfo): Int = {
    logger.info(
      s"create(path=$path, mode=${Integer.toOctalString(mode.toInt)})"
    )
    val file = getFile(path)
    // s"touch ${file.getAbsolutePath}".!
    if (file.exists() && file.isDirectory) return -ErrorCodes.EISDIR
    if (!file.createNewFile()) return -ErrorCodes.EIO
    chmod(path, mode)
  }
}
