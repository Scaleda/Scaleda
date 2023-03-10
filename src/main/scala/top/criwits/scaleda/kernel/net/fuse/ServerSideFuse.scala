package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.RemoteFuseGrpc.RemoteFuseBlockingClient
import kernel.net.fuse.fs._

import com.google.protobuf.ByteString
import jnr.ffi.Pointer
import org.slf4j.LoggerFactory
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Timespec}
import ru.serce.jnrfuse.{FuseFillDir, FuseStubFS}

import java.nio.ByteBuffer
import scala.language.existentials

/** Fuse running on remote server,
  * all operations will call scaleda client through gRPC
  */
class ServerSideFuse(stub: RemoteFuseBlockingClient) extends FuseStubFS {
  val logger = LoggerFactory.getLogger(getClass)

  override def getattr(path: String, stat: FileStat): Int = {
    val reply = stub.getattr(PathRequest(path))
    import reply._
    if (r == 0) {
      stat.st_size.set(size)
      stat.st_mode.set(mode)
      stat.st_atim.tv_sec.set(aTime)
      stat.st_mtim.tv_sec.set(mTime)
      stat.st_ctim.tv_sec.set(mTime)
      stat.st_nlink.set(1)
    } else {
      logger.warn(s"getattr failed for $path")
      stat.st_size.set(0)
      stat.st_mode.set(0)
      stat.st_atim.tv_sec.set(0)
      stat.st_mtim.tv_sec.set(0)
      stat.st_ctim.tv_sec.set(0)
      stat.st_nlink.set(0)
    }
    r
  }

  override def readlink(path: String, buf: Pointer, size: Long): Int = {
    val reply = stub.readlink(PathRequest(path))
    if (reply.r1 == 0) {
      val res = reply.r2
      val len = math.min(res.length, size.toInt)
      buf.put(0, res.getBytes(), 0, len)
      buf.putByte(len, 0)
    }
    reply.r1
  }

  override def mkdir(path: String, mode: Long): Int =
    stub.mkdir(PathModeRequest(path = path, mode = mode.toInt)).r

  override def unlink(path: String): Int =
    stub.unlink(PathRequest(path)).r

  override def rmdir(path: String): Int =
    stub.rmdir(PathRequest(path)).r

  override def symlink(oldpath: String, newpath: String): Int =
    stub.symlink(TuplePathRequest(oldpath = oldpath, newpath = newpath)).r

  override def rename(oldpath: String, newpath: String): Int =
    stub.rename(TuplePathRequest(oldpath = oldpath, newpath = newpath)).r

  override def chmod(path: String, mode: Long) =
    stub.chmod(PathModeRequest(path = path, mode = mode.toInt)).r

  override def read(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val reply = stub.read(
      ReadRequest(path = path, size = size.toInt, offset = offset.toInt)
    )
    if (reply.size > 0) {
      val data = reply.data.toByteArray
      buf.put(0, data, 0, data.length)
    }
    reply.size
  }

  override def write(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val data = new Array[Byte](size.toInt)
    buf.get(0, data, 0, size.toInt)
    stub
      .write(
        WriteRequest(
          path = path,
          data = ByteString.copyFrom(data),
          size = size.toInt,
          offset = offset.toInt
        )
      )
      .r
  }

  override def readdir(
      path: String,
      buf: Pointer,
      filter: FuseFillDir,
      offset: Long,
      fi: FuseFileInfo
  ): Int = {
    val reply     = stub.readdir(ReaddirRequest(path = path, offset = offset.toInt))
    var offsetNow = offset
    def applyFilter(filename: String): Int = {
      val nameBuffer = ByteBuffer.allocate(filename.length + 1)
      // nameBuffer.put(filename.getBytes)
      nameBuffer.put(0, filename.getBytes, 0, filename.length)
      offsetNow += 1
      filter.apply(buf, nameBuffer, null, offsetNow)
    }
    if (reply.r == 0)
      reply.name.foreach(name => applyFilter(name))
    reply.r
  }

  override def init(conn: Pointer): Pointer = {
    stub.init(EmptyReq())
    null
  }

  override def destroy(initResult: Pointer): Unit = {
    stub.destroy(EmptyReq())
  }

  override def create(path: String, mode: Long, fi: FuseFileInfo): Int =
    stub.create(PathModeRequest(path = path, mode = mode.toInt)).r

  override def utimens(path: String, timespec: Array[Timespec]): Int = 0
}
