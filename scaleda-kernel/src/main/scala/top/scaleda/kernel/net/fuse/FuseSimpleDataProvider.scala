package top.scaleda
package kernel.net.fuse

import kernel.net.fuse.FuseUtils.Converts._
import kernel.net.fuse.fs._
import kernel.utils.{KernelLogger, OS}

import com.google.protobuf.ByteString
import ru.serce.jnrfuse.ErrorCodes
import ru.serce.jnrfuse.struct.FileStat

import java.io.{File, FileInputStream, FileOutputStream, IOException}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.{OpenOption, StandardOpenOption}
import scala.async.Async.{async, await}
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

class FuseSimpleDataProvider(rootDirectory: File) extends RemoteFuseGrpc.RemoteFuse {
  final private val logger = KernelLogger

  private def appendPath(path: String, appendStr: String) = {
    val trimPath      = path.trim
    var pathSeparator = File.separator
    if (trimPath.endsWith("/") || trimPath.endsWith("\\")) pathSeparator = ""
    path + pathSeparator + appendStr
  }

  final private val openMap       = new mutable.HashMap[File, FileChannel]
  final private val channelStatus = new mutable.HashMap[FileChannel, OpenOption]

  private def getParentPath(path: String) = getPath(path).getParentFile

  private def getPath(path: String) = new File(rootDirectory, path)

  override def init(request: EmptyReq): Future[EmptyReq] = async {}

  override def destroy(request: EmptyReq): Future[EmptyReq] = async {}

  private def getAttrInner(request: PathRequest): GetAttrReply = {
    import request._
    logger.info("正在获取属性, path: {}", path)
    val p     = getPath(path)
    var reply = GetAttrReply()
    if (p.exists) {
      if (p.isDirectory) {
        // FileStat.S_IFDIR 为目录类型，还有更多类型可翻阅 FileStat 代码（明明写了注释却不写 Javadoc 格式真的难受 :( ）
        // 0777 指定了访问权限（Windows 似乎可以参考 Linux 的那套）
        reply = reply.copy(mode = FileStat.S_IFDIR | 0x1ff)
      } else if (p.isFile) {
        reply = reply.copy(mode = FileStat.S_IFREG | 0x1ff, size = p.length())
      }
      // set gid/uid in serverSide
      logger.info(
        s"getattr($path): mode=${Integer.toOctalString(reply.mode)} size=${reply.mode} uid=${reply.uid} gid=${reply.gid}"
      )
      reply
    } else reply.copy(r = -ErrorCodes.ENOENT)
  }

  override def getattr(request: PathRequest): Future[GetAttrReply] = async { getAttrInner(request) }

  override def mkdir(request: PathModeRequest): Future[IntReply] = async {
    import request._
    logger.info("正在创建目录, path: {}, mode: {}", path, mode)
    if (getPath(path).exists) {
      // 如果已存在
      -ErrorCodes.EEXIST
    } else {
      val parent = getParentPath(path)
      if (parent.isDirectory) {
        // 如果成功返回 0，否则返回 EIO
        if (getPath(path).mkdir) 0
        else -ErrorCodes.EIO
      } else {
        -ErrorCodes.ENOENT
      }
    }
  }

  override def unlink(request: PathRequest): Future[IntReply] = async {
    import request._
    logger.info("正在删除文件, path: {}", path)
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    else if (p.delete) 0
    else -ErrorCodes.EIO
  }

  override def rmdir(request: PathRequest): Future[IntReply] = async {
    import request._
    logger.info("正在删除目录, path: {}", path)
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    else if (!p.isDirectory) -ErrorCodes.ENOTDIR
    else {
      val list = p.list
      if (list != null && list.nonEmpty) {
        // 这里用于检查目录是否为空，如果不为空则返回 ENOTEMPTY(Directory not empty)
        -ErrorCodes.ENOTEMPTY
      } else {
        if (p.delete) 0
        else -ErrorCodes.EIO
      }
    }
  }

  override def rename(request: TuplePathRequest): Future[IntReply] = async {
    logger.info("正在更改文件名, path: {}, newName: {}", request.oldpath, request.newpath)
    val p = getPath(request.oldpath)
    if (!p.exists) -ErrorCodes.ENOENT
    else {
      val newParent = getParentPath(request.newpath)
      if (newParent == null) -ErrorCodes.ENOENT
      else if (!newParent.isDirectory) -ErrorCodes.ENOTDIR
      else if (p.renameTo(getPath(request.newpath))) 0
      else -ErrorCodes.EIO
    }
  }

  override def chmod(request: PathModeRequest): Future[IntReply] = async { 0 }

  override def truncate(request: PathOffsetRequest): Future[IntReply] = async {
    import request._
    logger.info("正在清空文件数据, path: {}, offset: {}", path, offset)
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    else if (!p.isFile) -ErrorCodes.EISDIR
    else {
      // val fileInput = new FileInputStream(p).getChannel
      try {
        // fileInput.position(offset)
        // fileInput.truncate(0)
        if (!p.delete()) throw new IOException("cannot detele file")
        else if (!p.createNewFile()) {
          throw new IOException("cannot delete file")
        } else 0
      } catch {
        case e: IOException =>
          logger.error("清空文件数据时发生异常", e)
          -ErrorCodes.EIO
      } //finally if (fileInput != null) fileInput.close()
    }
  }

  override def `open`(request: PathRequest): Future[IntReply] = async {
    import request._
    logger.info("正在打开文件, path: {}", path)
    // 建议该方法仅检查是否存在和是否为文件即可，不适宜打开 FileChannel
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    if (!p.isFile) -ErrorCodes.EISDIR
    else 0
  }

  override def read(request: ReadRequest): Future[ReadReply] = async {
    import request._
    logger.info("正在读取文件, path: {}, size: {}, offset: {}", path, size, offset)
    val p = getPath(path)
    if (!p.exists) ReadReply(-ErrorCodes.ENOENT)
    else if (p.isDirectory) ReadReply(-ErrorCodes.EISDIR)
    else {
      // 这里对 FileChannel 做了缓存，不做缓存的情况下速度慢到极点（1MB/s），但是万万不可在 open 里创建（ FileChannel 分了打开方式）
      var fileInput = openMap.get(p)
      // 检查打开类型是否为 Read，不是就换掉。
      if (
        fileInput.isEmpty || !(fileInput.nonEmpty && channelStatus
          .get(fileInput.get)
          .contains(StandardOpenOption.READ))
      ) {
        await(release(PathRequest.of(path)))
        val newChannel = new FileInputStream(p).getChannel
        openMap.put(p, newChannel)
        channelStatus.put(newChannel, StandardOpenOption.READ)
        fileInput = Some(newChannel)
      }
      val bytesToRead = Math.min(p.length - offset, size).toInt
      val bytesRead   = ByteBuffer.allocate(bytesToRead)
      this.synchronized {
        fileInput.get.position(offset.toInt)
        fileInput.get.read(bytesRead)
      }
      // 注意，这里需要返回读取的字节数！
      ReadReply(bytesToRead, data = ByteString.copyFrom(bytesRead))
    }
  }

  override def write(request: WriteRequest): Future[IntReply] = async {
    import request._
    logger.info("正在写入文件, path: {}, size: {}, offset: {}", path, size, offset)
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    else if (!p.isFile) -ErrorCodes.EISDIR
    else {
      // 同 read 方法一样
      var fileOutput = openMap.get(p)
      if (
        fileOutput.isEmpty || !(fileOutput.nonEmpty && channelStatus
          .get(fileOutput.get)
          .contains(StandardOpenOption.WRITE))
      ) {
        await(release(PathRequest.of(path)))
        val newChannel = new FileOutputStream(p).getChannel
        openMap.put(p, newChannel)
        channelStatus.put(newChannel, StandardOpenOption.WRITE)
        fileOutput = Some(newChannel)
      }
      val maxWriteIndex = (offset + size).toInt
      val bytesToWrite  = new Array[Byte](size.toInt)
      this.synchronized {
        if (maxWriteIndex > p.length) fileOutput.get.truncate(maxWriteIndex)
        fileOutput.get.position(offset.toInt)
        fileOutput.get.write(ByteBuffer.wrap(data.toByteArray))
      }
      size.toInt
    }
  }

  override def readdir(request: ReaddirRequest): Future[ReaddirReply] = async {
    import request._
    logger.info("正在读取目录列表, path: {}, offset: {}", path, offset)
    val p = getPath(path)
    if (!p.exists) ReaddirReply(-ErrorCodes.ENOENT)
    else if (!p.isDirectory) ReaddirReply(-ErrorCodes.ENOTDIR)
    else {
      // add ".", ".." in server side
      val files   = p.listFiles
      val results = new mutable.HashMap[String, GetAttrReply]()
      if (files != null) {
        for (file <- files.slice(offset, files.length)) {
          // 注意：传递给 getattr 的路径需要是子项路径
          // 如果无法读取指定子项的属性，就跳过
          val reply = getAttrInner(PathRequest.of(FuseSimple.appendPath(path, file.getName)))
          if (reply.r == 0) results.put(file.getName, reply)
        }
      }
      // 本方法只需要返回 0 代表成功
      ReaddirReply(entries = results.toMap, enableOffset = true)
    }
  }

  override def create(request: PathModeRequest): Future[IntReply] = async {
    import request._
    logger.info("正在创建文件, filePath: {}, mode: {}", path, mode)
    if (getPath(path).exists) {
      // 文件存在的情况下返回 EEXIST(文件已存在) 错误码
      -ErrorCodes.EEXIST
    } else {
      // 获取父级 File 并检查其是否为目录
      // 其实这里应该还要检查是否在映射的目录下的，不过假设请求没问题，就忽略了
      val parent = getParentPath(path)
      if (parent.isDirectory) {
        try {
          // 当没有问题且不需要返回什么的时候就返回 0，代表操作成功
          if (getPath(path).createNewFile) 0
          else -ErrorCodes.EIO
        } catch {
          case e: IOException =>
            logger.error("创建文件失败", e)
            // 当读写发生错误时的万能代码（如果能在 ErrorCodes 里找到对应代码就最好不过，没找到可使用 EIO 代替）
            -ErrorCodes.EIO
        }
      } else {
        // 未找到指定文件或目录则返回 ENOENT(No such file or directory) 错误码
        -ErrorCodes.ENOENT
      }
    }
  }

  override def statfs(request: PathRequest): Future[StatfsReply] = async {
    import request._
    logger.info("正在获取文件系统属性, path: {}", path)
    // 仅限 Windows
    // statfs needs to be implemented on Windows in order to allow for copying
    // data from other devices because winfsp calculates the volume size based
    // on the statvfs call.
    // see https://github.com/billziss-gh/winfsp/blob/14e6b402fe3360fdebcc78868de8df27622b565f/src/dll/fuse/fuse_intf.c#L654
    if (OS.isWindows && "/" == path) {
      StatfsReply.of(blocks = 1024 * 1024, frsize = 1024, bfree = 1024 * 1024)
    } else {
      StatfsReply()
    }
  }

  override def release(request: PathRequest): Future[IntReply] = async {
    import request._
    logger.info("正在释放文件, path: {}", path)
    val p = getPath(path)
    if (!p.exists) -ErrorCodes.ENOENT
    else if (!p.isFile) -ErrorCodes.EISDIR
    else {
      // 这里将打开的 Channel 关闭即可，如果没开，就不管它
      if (openMap.contains(p)) {
        try {
          openMap(p).close()
          0
        } catch {
          case e: IOException =>
            logger.error("释放文件时发生异常", e)
            -ErrorCodes.EIO
        } finally openMap.remove(p)
      } else 0
    }
  }
}
