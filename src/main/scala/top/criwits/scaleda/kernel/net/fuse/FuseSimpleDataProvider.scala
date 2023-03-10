package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs._

import com.google.protobuf.ByteString
import jnr.ffi.{Memory, Pointer}
import ru.serce.jnrfuse.struct.FileStat

import java.io.{ByteArrayInputStream, File}
import java.nio.ByteBuffer
import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

object FuseSimpleDataProvider {
  def appendPath(path: String, appendStr: String) = {
    val trimPath      = path.trim
    var pathSeparator = File.separator
    if (trimPath.endsWith("/") || trimPath.endsWith("\\")) pathSeparator = ""
    path + pathSeparator + appendStr
  }
}

class FuseSimpleDataProvider(source: FuseSimple) extends RemoteFuseGrpc.RemoteFuse {
  private implicit def convert(a: Unit): EmptyReq      = EmptyReq()
  private implicit def convert(a: Int): IntReply       = IntReply.of(a)
  private implicit def convert(a: PathRequest): String = a.path

  override def init(request: EmptyReq): Future[EmptyReq] = async {}

  override def destroy(request: EmptyReq): Future[EmptyReq] = async {}

  override def getattr(request: PathRequest): Future[GetAttrReply] = async {
    import request._
    val buf      = Memory.allocate(jnr.ffi.Runtime.getSystemRuntime, 2048)
    val fileStat = FileStat.of(buf)
    val r        = source.getattr(path, fileStat)
    // TODO: timee
    GetAttrReply.of(r, fileStat.st_size.longValue(), fileStat.st_mode.intValue(), 0, 0)
  }

  override def readlink(request: PathRequest): Future[StringTupleReply] = ???

  override def mkdir(request: PathModeRequest): Future[IntReply] = async {
    source.mkdir(request.path, request.mode)
  }

  override def unlink(request: PathRequest): Future[IntReply] = async {
    source.unlink(request)
  }

  override def rmdir(request: PathRequest): Future[IntReply] = async {
    source.rmdir(request)
  }

  override def symlink(request: TuplePathRequest): Future[IntReply] = ???

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
    ReaddirReply.of(0, source.simpleReadDir(request.path))
  }

  override def create(request: PathModeRequest): Future[IntReply] = async {
    source.create(request.path, request.mode, null)
  }
}
