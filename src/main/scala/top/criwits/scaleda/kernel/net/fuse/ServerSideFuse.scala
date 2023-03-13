package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.RemoteFuseGrpc.RemoteFuseBlockingClient
import kernel.net.fuse.fs._
import kernel.utils.{KernelLogger, OS}

import com.google.protobuf.ByteString
import jnr.ffi.Pointer
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Statvfs, Timespec}
import ru.serce.jnrfuse.{FuseFillDir, FuseStubFS}

import java.io.File
import scala.language.existentials

/** Fuse running on remote server,
  * all operations will call scaleda client through gRPC
  */
class ServerSideFuse(stub: RemoteFuseBlockingClient) extends FuseStubFS {
  // val logger = LoggerFactory.getLogger(getClass)
  val logger = KernelLogger

  private def applyAttr(reply: GetAttrReply, stat: FileStat): Unit = {
    import reply._
    if (r == 0) {
      stat.st_mode.set(mode)
      stat.st_size.set(size)
      stat.st_atim.tv_sec.set(aTime)
      stat.st_mtim.tv_sec.set(mTime)
      stat.st_ctim.tv_sec.set(cTime)
      stat.st_birthtime.tv_sec.set(bTime)
      stat.st_uid.set(if (uid != 0) uid else getContext.uid.get)
      stat.st_gid.set(if (gid != 0) uid else getContext.gid.get)
    } else {
      logger.debug(s"getattr failed! r = $r")
    }
  }

  override def getattr(path: String, stat: FileStat): Int = {
    val reply =
      try {
        stub.getattr(PathRequest(path))
      } catch {
        case e: Throwable =>
          KernelLogger.error("error when getattr:", e)
          e.printStackTrace()
          throw e
      }
    applyAttr(reply, stat)
    logger.info(s"getattr($path): ${reply.r} mode=${Integer.toOctalString(stat.st_mode.intValue())} size=${stat.st_size
      .longValue()} uid=${stat.st_uid.get} gid=${stat.st_gid.get}")
    reply.r
  }

  override def mkdir(path: String, mode: Long): Int =
    stub.mkdir(PathModeRequest(path = path, mode = mode.toInt)).r

  override def unlink(path: String): Int =
    stub.unlink(PathRequest(path)).r

  override def rmdir(path: String): Int =
    stub.rmdir(PathRequest(path)).r

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
    logger.info(s"server side readdir(path=$path, offset=$offset)")
    val reply = stub.readdir(ReaddirRequest(path = path, offset = math.max(0, offset.toInt - 2)))
    if (reply.r == 0) {
      if (reply.enableOffset) {
        if (offset == 0) filter.apply(buf, ".", null, 1)
        if (offset == 0 || offset == 1) filter.apply(buf, "..", null, 2)
        val entries    = reply.entries.toSeq
        var offsetNext = offset + 1
        for (i <- entries.indices) {
          val (name, attr) = entries(i)
          if (attr.r == 0) {
            logger.info(s"applying entry $name")
            if (OS.isWindows) {
              // FIXME: buffer on windows may not so big... so slower
              filter.apply(buf, name, null, offsetNext)
            } else {
              val fileStat = FileStat.of(buf)
              applyAttr(attr, fileStat)
              filter.apply(buf, name, fileStat, offsetNext)
            }
            offsetNext += 1
          } else {
            logger.warn(s"getattr failed for $name when reading dir $path")
          }
        }
      } else {
        filter.apply(buf, ".", null, 0)
        filter.apply(buf, "..", null, 0)
        reply.entries.foreach(f => {
          val (name, attr) = f
          if (attr.r == 0) {
            val fileStat = FileStat.of(buf)
            logger.info(s"applying entry $name")
            applyAttr(attr, fileStat)
            filter.apply(buf, name, fileStat, 0)
          }
        })
      }
    }
    logger.debug("readdir done")
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

  override def release(path: String, fi: FuseFileInfo): Int =
    stub.release(PathRequest(path = path)).r

  override def statfs(path: String, stbuf: Statvfs) = {
    logger.info(s"statfs(path=$path)")
    if (OS.isWindows && "/".equals(path)) {
      stbuf.f_blocks.set(1024 * 1024)
      stbuf.f_frsize.set(1024)
      stbuf.f_bfree.set(1024 * 1024)
    }
    0
  }

  override def open(path: String, fi: FuseFileInfo): Int =
    stub.`open`(PathRequest(path = path)).r

  override def truncate(path: String, size: Long): Int =
    stub.truncate(PathOffsetRequest(path = path, offset = size)).r
}
