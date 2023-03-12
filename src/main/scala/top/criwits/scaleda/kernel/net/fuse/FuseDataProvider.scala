package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseUtils.Converts._
import kernel.net.fuse.fs._
import kernel.utils.OS

import com.google.protobuf.ByteString
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.ErrorCodes
import ru.serce.jnrfuse.struct.FileStat

import java.io.{File, FileOutputStream, IOException, RandomAccessFile}
import java.nio.file.Files
import java.nio.file.attribute.{DosFileAttributes, PosixFileAttributes}
import java.util.concurrent.TimeUnit
import scala.async.Async.async
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.sys.process._

/** gRPC data provider for filesystem sync.<br/>
  * This thread starts in Scaleda Client side, but actually is a gRPC server.
  * @param sourcePath source data path
  */
class FuseDataProvider(sourcePath: String) extends RemoteFuseGrpc.RemoteFuse {
  val logger = LoggerFactory.getLogger(getClass)
  // val logger = KernelLogger
  // logger.setLevel(LogLevel.Debug)

  private def getFile(p: String): File = new File(sourcePath, p)

  override def init(request: EmptyReq): Future[EmptyReq] = async {
    logger.info("init")
    EmptyReq()
  }

  override def destroy(request: EmptyReq): Future[EmptyReq] = async {
    logger.info("destroy")
  }

  private def getAttrLocal(file: File): GetAttrReply = {
    if (!file.exists()) GetAttrReply(-ErrorCodes.ENOENT)
    else {
      val p = file.toPath
      val attrs =
        if (OS.isWindows)
          Files.readAttributes(p, classOf[DosFileAttributes])
        else
          Files.readAttributes(p, classOf[PosixFileAttributes])
      // 0777 | ...
      // var mode = FuseUtils.fileAttrsToInt(file, "rwxrwxrwx")
      // var mode = FuseUtils.fileAttrsUnixToInt(file)
      var mode =
        if (OS.isWindows)
          // FuseUtils.fileAttrsToInt(file, "rwxrwxrwx")
          0x1ff | (if (attrs.isDirectory) FileStat.S_IFDIR else FileStat.S_IFREG)
        else
          FuseUtils.fileAttrsUnixToInt(file)
      if (Files.isSymbolicLink(file.toPath)) {
        mode = (mode & 0xfff) | (0xa << 12)
      }
      GetAttrReply(
        mode = mode,
        // size = attrs.size(),
        size = if (attrs.isDirectory) 0 else file.length(),
        cTime = attrs.creationTime().to(TimeUnit.SECONDS),
        aTime = attrs.lastAccessTime().to(TimeUnit.SECONDS),
        mTime = attrs.lastModifiedTime().to(TimeUnit.SECONDS)
      )
    }
  }

  private def getAttrInner(request: PathRequest): GetAttrReply = {
    val file = getFile(request.path)
    logger.info(s"getattr(${request.path}) <-> $file")
    getAttrLocal(file)
  }

  override def getattr(request: PathRequest): Future[GetAttrReply] = async { getAttrInner(request) }

  override def mkdir(request: PathModeRequest): Future[IntReply] = async {
    val path = request.path
    val file = getFile(path)
    if (file.exists()) IntReply(-ErrorCodes.EEXIST)
    else if (file.exists() || (file.exists() && file.isFile))
      IntReply(-ErrorCodes.ENOENT)
    else {
      // s"""mkdir "${file.getAbsolutePath}\"""".!
      if (file.mkdirs()) 0 else -ErrorCodes.EIO()
      // await(chmod(request))
    }
  }

  override def unlink(request: PathRequest): Future[IntReply] = async {
    val file = getFile(request.path)
    if (!file.exists()) IntReply(-ErrorCodes.ENOENT)
    else if (file.exists() && file.isDirectory) IntReply(-ErrorCodes.EISDIR)
    else {
      file.delete()
      IntReply()
    }
  }

  override def rmdir(request: PathRequest): Future[IntReply] = async {
    val file = getFile(request.path)
    IntReply({
      if (!file.exists() || (file.exists() && file.isFile)) {
        -ErrorCodes.ENOENT
      } else {
        file.delete()
        0
      }
    })
  }

  override def rename(request: TuplePathRequest): Future[IntReply] = async {
    import request._
    IntReply({
      val fileOld = getFile(oldpath)
      val fileNew = getFile(newpath)
      if (fileOld.renameTo(fileNew)) 0
      else -ErrorCodes.EIO
    })
  }

  override def chmod(request: PathModeRequest): Future[IntReply] = async {
    import request._
    val file = getFile(path)
    if (OS.isWindows) {
      IntReply(if (file.exists()) 0 else -ErrorCodes.ENOENT)
    } else {
      IntReply({
        val run =
          s"""chmod ${Integer.toOctalString(mode.toInt & 0xfff)} \"${file.getAbsolutePath}\""""
        logger.info(
          s"chmod(path=$path, mode=${Integer.toOctalString(mode.toInt)}), run: $run"
        )
        val r = run.!
        if (r == 0) 0 else -ErrorCodes.ENOENT
      })
    }
  }

  override def read(request: ReadRequest): Future[ReadReply] = async {
    import request._
    val file = getFile(path)
    logger.info(s"reading $path <-> $file")
    if (!file.exists()) ReadReply(-ErrorCodes.ENOENT)
    else if (file.isDirectory) ReadReply(-ErrorCodes.EISDIR)
    else {
      logger.info(s"read(path=$path, size=$size, offset=$offset)")
      val rf = new RandomAccessFile(file, "r")
      rf.seek(offset)
      val data = new Array[Byte](size)
      // warning: this func blocks thread
      rf.read(data, 0, size)
      ReadReply(size = size, data = ByteString.copyFrom(data))
    }
  }

  override def write(request: WriteRequest): Future[IntReply] = async {
    import request._
    logger.info(s"write(path=$path, size=$size, offset=$offset)")
    val file = getFile(path)
    IntReply({
      if (!file.exists()) -ErrorCodes.ENOENT
      else if (file.isDirectory) -ErrorCodes.EISDIR
      else {
        val rf = new RandomAccessFile(file, "rw")
        rf.seek(offset)
        rf.write(data.toByteArray)
        rf.close()
        size
      }
    })
  }

  override def readdir(request: ReaddirRequest): Future[ReaddirReply] = async {
    import request._
    val file = getFile(path)
    if (!file.exists() || !file.isDirectory) {
      logger.warn(s"file exists? ${file.exists()}, file is dir? ${file.isDirectory}")
      ReaddirReply(-ErrorCodes.ENOENT)
    } else {
      val results = ArrayBuffer[String]()

      def applyFilter(filename: String): Unit = {
        results.addOne(filename)
        logger.info(s"add $filename to results")
      }

      logger.info(
        s"readdir(path=$path, offset=$offset), file=${file.getAbsolutePath}"
      )
      val list = file.listFiles()
      logger.info(s"listed files: ${list.mkString(", ")}")
      ReaddirReply(entries =
        list
          .map(file => {
            file.getName -> getAttrLocal(file)
          })
          .toMap
      )
    }
  }

  override def create(request: PathModeRequest): Future[IntReply] = async {
    val file = getFile(request.path)
    // s"touch ${file.getAbsolutePath}".!
    if (file.exists()) IntReply(-ErrorCodes.EEXIST)
    else if (file.exists() && file.isDirectory) IntReply(-ErrorCodes.EISDIR)
    else if (!file.createNewFile()) IntReply(-ErrorCodes.EIO)
    // else await(chmod(request))
    else 0
  }

  override def truncate(request: PathOffsetRequest): Future[IntReply] = async {
    val file = getFile(request.path)
    if (!file.exists) -ErrorCodes.ENOENT
    else if (!file.isFile) -ErrorCodes.EISDIR
    else {
      // val fileInput = new FileInputStream(file).getChannel
      try {
        // fileInput.position(offset)
        // fileInput.truncate(0)
        // 0
        val _ = new FileOutputStream(file)
        0
      } catch {
        case e: IOException =>
          logger.warn(s"error when truncate! $e retry with delete/create")
          if (!file.delete()) -ErrorCodes.EIO()
          else if (!file.createNewFile()) -ErrorCodes.EIO()
          else 0
      } // finally if (fileInput != null) fileInput.close()
    }
  }

  override def `open`(request: PathRequest): Future[IntReply] = async { 0 }

  override def release(request: PathRequest): Future[IntReply] = async { 0 }

  override def statfs(request: PathRequest): Future[StatfsReply] = async {
    StatfsReply.of(blocks = 1024 * 1024, frsize = 1024, bfree = 1024 * 1024)
  }
}
