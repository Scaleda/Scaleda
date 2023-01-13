package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs._

import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.ErrorCodes

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributes
import java.util.concurrent.TimeUnit
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FuseDataProvider(sourcePath: String) extends RemoteFuseGrpc.RemoteFuse {
  val logger = LoggerFactory.getLogger(getClass)

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
    val file = getFile(request.path)
    if (!file.exists()) GetAttrReply(-ErrorCodes.ENOENT)
    else {
      var mode = FuseUtils.fileAttrsUnixToInt(file)
      if (Files.isSymbolicLink(file.toPath)) {
        mode = (mode & 0xfff) | (0xa << 12)
      }
      val p = file.toPath
      val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
      GetAttrReply(
        mode = mode,
        size = attrs.size(),
        aTime = attrs.lastAccessTime().to(TimeUnit.SECONDS),
        mTime = attrs.lastModifiedTime().to(TimeUnit.SECONDS)
      )
    }
  }

  override def readlink(request: PathRequest): Future[StringTupleReply] = async {
    // Files.readSymbolicLink()
    StringTupleReply()
  }

  /** rpc mknod(EmptyRequest) returns (IntReply) {}
    */
  override def mkdir(request: PathModeRequest): Future[IntReply] = ???

  override def unlink(request: PathRequest): Future[IntReply] = ???

  override def rmdir(request: PathRequest): Future[IntReply] = ???

  override def symlink(request: TuplePathRequest): Future[IntReply] = ???

  override def rename(request: TuplePathRequest): Future[IntReply] = ???

  /** rpc link(TuplePathRequest) returns (IntReply) {}
    */
  override def chmod(request: PathModeRequest): Future[IntReply] = ???

  /** rpc chown() returns (IntReply) {}
    */
  override def truncate(request: PathSizeRequest): Future[IntReply] = ???

  /** rpc open() returns (IntReply) {}
    */
  override def read(request: ReadRequest): Future[ReadReply] = ???

  override def write(request: WriteRequest): Future[IntReply] = ???

  override def readdir(request: ReaddirRequest): Future[ReaddirReply] = ???

  override def create(request: PathModeRequest): Future[IntReply] = ???
}
