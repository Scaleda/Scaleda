package top.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseUtils.Converts._
import kernel.net.fuse.fs._

import com.google.protobuf.ByteString
import jnr.ffi.{Memory, Pointer}
import ru.serce.jnrfuse.struct.{FileStat, Statvfs}

import java.io.{ByteArrayInputStream, File}
import java.nio.ByteBuffer
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

object FuseSimpleDataProviderWrapper {
  def appendPath(path: String, appendStr: String) = {
    val trimPath      = path.trim
    var pathSeparator = File.separator
    if (trimPath.endsWith("/") || trimPath.endsWith("\\")) pathSeparator = ""
    path + pathSeparator + appendStr
  }
}

class FuseSimpleDataProviderWrapper(source: FuseSimple) extends RemoteFuseGrpc.RemoteFuse {
  override def init(request: EmptyReq): Future[EmptyReq] = async {}

  override def destroy(request: EmptyReq): Future[EmptyReq] = async {}

  override def getattr(request: PathRequest): Future[GetAttrReply] = async {
    import request._
    val buf      = Memory.allocate(jnr.ffi.Runtime.getSystemRuntime, 2048)
    val fileStat = FileStat.of(buf)
    val r        = source.getattr(path, fileStat)
    GetAttrReply.of(
      r,
      size = fileStat.st_size.longValue(),
      mode = fileStat.st_mode.intValue(),
      aTime = fileStat.st_atim.tv_sec.get(),
      mTime = fileStat.st_mtim.tv_sec.get(),
      cTime = fileStat.st_ctim.tv_sec.get(),
      bTime = fileStat.st_birthtime.tv_sec.get(),
      uid = fileStat.st_uid.get(),
      gid = fileStat.st_gid.get()
    )
  }

  override def mkdir(request: PathModeRequest): Future[IntReply] = async {
    source.mkdir(request.path, request.mode)
  }

  override def unlink(request: PathRequest): Future[IntReply] = async {
    source.unlink(request)
  }

  override def rmdir(request: PathRequest): Future[IntReply] = async {
    source.rmdir(request)
  }

  // override def symlink(request: TuplePathRequest): Future[IntReply] = ???

  override def rename(request: TuplePathRequest): Future[IntReply] = async {
    source.rename(request.oldpath, request.newpath)
  }

  override def chmod(request: PathModeRequest): Future[IntReply] = async { 0 }

  override def read(request: ReadRequest): Future[ReadReply] = async {
    val buf  = Memory.allocate(jnr.ffi.Runtime.getSystemRuntime, request.size)
    val sz   = source.read(request.path, buf, request.size, request.offset, null)
    val data = new Array[Byte](sz)
    buf.get(0, data, 0, sz)
    val in = new ByteArrayInputStream(data)
    ReadReply.of(sz, ByteString.readFrom(in))
  }

  override def write(request: WriteRequest): Future[IntReply] = async {
    val buf = ByteBuffer.wrap(request.data.toByteArray)
    source.write(request.path, Pointer.wrap(jnr.ffi.Runtime.getSystemRuntime, buf), request.size, request.offset, null)
  }

  override def readdir(request: ReaddirRequest): Future[ReaddirReply] = async {
    ReaddirReply(entries = source.simpleReadDir(request.path, offset = request.offset))
  }

  override def create(request: PathModeRequest): Future[IntReply] = async {
    source.create(request.path, request.mode, null)
  }

  override def truncate(request: PathOffsetRequest): Future[IntReply] = async {
    source.truncate(request.path, request.offset)
  }

  override def `open`(request: PathRequest): Future[IntReply] = async {
    source.open(request.path, null)
  }

  override def release(request: PathRequest): Future[IntReply] = async {
    source.release(request.path, null)
  }

  override def statfs(request: PathRequest): Future[StatfsReply] = async {
    val buf     = Memory.allocate(jnr.ffi.Runtime.getSystemRuntime, 512)
    val statvfs = Statvfs.of(buf)
    source.statfs(request.path, statvfs)
    StatfsReply.of(blocks = statvfs.f_blocks.get(), frsize = statvfs.f_frsize.intValue(), bfree = statvfs.f_bfree.get())
  }
}
