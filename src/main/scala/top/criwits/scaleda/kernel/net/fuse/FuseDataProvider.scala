package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs._

import com.google.protobuf.ByteString
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.ErrorCodes

import java.io.{File, RandomAccessFile}
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributes
import java.util.concurrent.TimeUnit
import scala.async.Async.{async, await}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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
    EmptyReq()
  }

  override def getattr(request: PathRequest): Future[GetAttrReply] = async {
    logger.info(s"getattr(${request.path})")
    val file = getFile(request.path)
    if (!file.exists()) GetAttrReply(-ErrorCodes.ENOENT)
    else {
      var mode = FuseUtils.fileAttrsUnixToInt(file)
      if (Files.isSymbolicLink(file.toPath)) {
        mode = (mode & 0xfff) | (0xa << 12)
      }
      val p     = file.toPath
      val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
      GetAttrReply(
        mode = mode,
        size = attrs.size(),
        aTime = attrs.lastAccessTime().to(TimeUnit.SECONDS),
        mTime = attrs.lastModifiedTime().to(TimeUnit.SECONDS)
      )
    }
  }

  override def readlink(request: PathRequest): Future[StringTupleReply] =
    async {
      val path = request.path
      val file = getFile(path)
      if (!Files.isSymbolicLink(file.toPath))
        StringTupleReply(-ErrorCodes.ENOENT)
      else {
        // val res = Files.readSymbolicLink(file.toPath).toFile.getAbsolutePath
        val run   = s"readlink ${file.getAbsolutePath}"
        val array = mutable.ArrayBuffer[String]()
        val r = run ! ProcessLogger(
          stdout => array.addOne(stdout),
          _ => {}
        )
        val res = array.mkString("")
        logger.warn(s"readlink: sourcePath=$sourcePath, path=$path, res=$res")
        StringTupleReply(r2 = res)
      }
    }

  override def mkdir(request: PathModeRequest): Future[IntReply] = async {
    val path = request.path
    val file = getFile(path)
    if (file.exists() || (file.exists() && file.isFile))
      IntReply(-ErrorCodes.ENOENT)
    else {
      s"""mkdir "${file.getAbsolutePath}\"""".!
      await(chmod(request))
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

  override def symlink(request: TuplePathRequest): Future[IntReply] = async {
    import request._
    val fileOld = new File(oldpath)
    val fileNew = getFile(newpath)
    Files.createSymbolicLink(fileNew.toPath, fileOld.toPath)
    IntReply()
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
    IntReply({
      val file = getFile(path)
      val run =
        s"""chmod ${Integer.toOctalString(mode.toInt & 0xfff)} \"${file.getAbsolutePath}\""""
      logger.info(
        s"chmod(path=$path, mode=${Integer.toOctalString(mode.toInt)}), run: $run"
      )
      val r = run.!
      if (r == 0) 0 else -ErrorCodes.ENOENT
    })
  }

  override def read(request: ReadRequest): Future[ReadReply] = async {
    import request._
    val file = getFile(path)
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
        logger.warn(s"add $filename to results")
      }

      logger.info(
        s"readdir(path=$path, offset=$offset), file=${file.getAbsolutePath}"
      )
      val list = file.listFiles()
      logger.info(s"listed files: ${list.mkString(", ")}")
      if (offset == 0) applyFilter(".")
      if (offset == 1 || offset == 0) applyFilter("..")
      if (list.length + 2 == offset) ReaddirReply()
      else if (list.length + 2 < offset) ReaddirReply(-ErrorCodes.ENOENT)
      else
        list
          .slice(offset - 2, list.length)
          .headOption
          .map(f => {
            logger.info(
              s"readdir: putting file ${f.getAbsolutePath}, name: ${f.getName}"
            )
            applyFilter(f.getName)
            ReaddirReply(name = results.toSeq)
          })
          .getOrElse(ReaddirReply(-ErrorCodes.ENOENT))
    }
  }

  override def create(request: PathModeRequest): Future[IntReply] = async {
    val file = getFile(request.path)
    // s"touch ${file.getAbsolutePath}".!
    if (file.exists() && file.isDirectory) IntReply(-ErrorCodes.EISDIR)
    else if (!file.createNewFile()) IntReply(-ErrorCodes.EIO)
    else await(chmod(request))
  }
}
