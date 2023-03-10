// package top.criwits.scaleda
// package kernel.net.fuse
//
// import jnr.ffi.types.{mode_t, off_t, size_t}
// import jnr.ffi.{Platform, Pointer}
// import org.slf4j.LoggerFactory
// import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Statvfs}
// import ru.serce.jnrfuse.{ErrorCodes, FuseFillDir, FuseStubFS}
// import top.criwits.scaleda.kernel.utils.OS
//
// import java.io.{File, FileInputStream, FileOutputStream, IOException}
// import java.nio.ByteBuffer
// import java.nio.channels.FileChannel
// import java.nio.file.{OpenOption, StandardOpenOption}
// import java.util
//
// object FuseSimpleServer {
//   def appendPath(path: String, appendStr: String) = {
//     val trimPath      = path.trim
//     var pathSeparator = File.separator
//     if (trimPath.endsWith("/") || trimPath.endsWith("\\")) pathSeparator = ""
//     path + pathSeparator + appendStr
//   }
// }
//
// class FuseSimpleServer(private val rootDirectory: File) extends FuseStubFS {
//   final private val logger        = LoggerFactory.getLogger(this.getClass.getSimpleName + "@" + this.hashCode)
//   final private val openMap       = new util.HashMap[File, FileChannel]
//   final private val channelStatus = new util.HashMap[FileChannel, OpenOption]
//
//   override def create(path: String, @mode_t mode: Long, fi: FuseFileInfo): Int = {
//     logger.info("正在创建文件, filePath: {}, mode: {}", path, mode)
//     if (getPath(path).exists) {
//       // 文件存在的情况下返回 EEXIST(文件已存在) 错误码
//       return -ErrorCodes.EEXIST
//     }
//     // 获取父级 File 并检查其是否为目录
//     // 其实这里应该还要检查是否在映射的目录下的，不过假设请求没问题，就忽略了
//     val parent = getParentPath(path)
//     if (parent.isDirectory)
//       try
//       // 当没有问题且不需要返回什么的时候就返回 0，代表操作成功
//       return if (getPath(path).createNewFile) 0
//       else -ErrorCodes.EIO
//       catch {
//         case e: IOException =>
//           logger.error("创建文件失败", e)
//           // 当读写发生错误时的万能代码（如果能在 ErrorCodes 里找到对应代码就最好不过，没找到可使用 EIO 代替）
//           return -ErrorCodes.EIO
//       }
//     // 未找到指定文件或目录则返回 ENOENT(No such file or directory) 错误码
//     -ErrorCodes.ENOENT
//   }
//
//   override def getattr(path: String, stat: FileStat): Int = {
//     logger.info("正在获取属性, path: {}", path)
//     val p = getPath(path)
//     if (p.exists) {
//       if (p.isDirectory) {
//         // FileStat.S_IFDIR 为目录类型，还有更多类型可翻阅 FileStat 代码（明明写了注释却不写 Javadoc 格式真的难受 :( ）
//         // 0777 指定了访问权限（Windows 似乎可以参考 Linux 的那套）
//         stat.st_mode.set(FileStat.S_IFDIR | 0x1ff)
//       } else if (p.isFile) {
//         stat.st_mode.set(FileStat.S_IFREG | 0x1ff)
//         stat.st_size.set(p.length)
//       }
//       // 不是很明白用来做什么，但必须设置
//       stat.st_uid.set(getContext.uid.get)
//       stat.st_gid.set(getContext.gid.get)
//       logger.warn(s"getattr($path): mode=${Integer.toOctalString(stat.st_mode.intValue())} size=${stat.st_size
//         .longValue()} uid=${stat.st_uid.get} gid=${stat.st_gid.get}")
//       return 0
//     }
//     -ErrorCodes.ENOENT
//   }
//
//   private def getParentPath(path: String) = getPath(path).getParentFile
//
//   private def getPath(path: String) = new File(rootDirectory, path)
//
//   override def mkdir(path: String, @mode_t mode: Long): Int = {
//     logger.info("正在创建目录, path: {}, mode: {}", path, mode)
//     if (getPath(path).exists) {
//       // 如果已存在
//       return -ErrorCodes.EEXIST
//     }
//     val parent = getParentPath(path)
//     if (parent.isDirectory) {
//       // 如果成功返回 0，否则返回 EIO
//       return if (getPath(path).mkdir) 0
//       else -ErrorCodes.EIO
//     }
//     -ErrorCodes.ENOENT
//   }
//
//   override def read(path: String, buf: Pointer, @size_t size: Long, @off_t offset: Long, fi: FuseFileInfo): Int = {
//     logger.info("正在读取文件, path: {}, size: {}, offset: {}", path, size, offset)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (p.isDirectory) return -ErrorCodes.EISDIR
//     try {
//       // 这里对 FileChannel 做了缓存，不做缓存的情况下速度慢到极点（1MB/s），但是万万不可在 open 里创建（ FileChannel 分了打开方式）
//       var fileInput = openMap.get(p)
//       // 检查打开类型是否为 Read，不是就换掉。
//       if (fileInput == null || !(channelStatus.get(fileInput) == StandardOpenOption.READ)) {
//         release(path, fi)
//         val newChannel = new FileInputStream(p).getChannel
//         openMap.put(p, newChannel)
//         channelStatus.put(newChannel, StandardOpenOption.READ)
//         fileInput = newChannel
//       }
//       val bytesToRead = Math.min(p.length - offset, size).toInt
//       val bytesRead   = ByteBuffer.allocate(bytesToRead)
//       this.synchronized {
//         fileInput.position(offset.toInt)
//         fileInput.read(bytesRead)
//         buf.put(0, bytesRead.array, 0, bytesToRead)
//       }
//       // 注意，这里需要返回读取的字节数！
//       bytesToRead
//     } catch {
//       case e: IOException =>
//         e.printStackTrace()
//         -ErrorCodes.EIO
//     }
//   }
//
//   override def readdir(path: String, buf: Pointer, filter: FuseFillDir, @off_t offset: Long, fi: FuseFileInfo): Int = {
//     logger.info("正在读取目录列表, path: {}, offset: {}", path, offset)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isDirectory) return -ErrorCodes.ENOTDIR
//     // 似乎是必须加入的？照顾一下 Linux 的似乎。
//     filter.apply(buf, ".", null, 0)
//     filter.apply(buf, "..", null, 0)
//     val files = p.listFiles
//     if (files != null) for (file <- files) {
//       // 创建一个新的文件元数据结构对象
//       val fileStat = FileStat.of(buf)
//       // 注意：传递给 getattr 的路径需要是子项路径
//       // 如果无法读取指定子项的属性，就跳过
//       if (getattr(FuseSimple.appendPath(path, file.getName), fileStat) == 0) {
//         // 加入一个项目，参数 1 只需要传递方法传入的 buf 即可，参数 4 也只需要传递 0 就好了
//         // 参数 2 传入子项文件名（不含全路径），参数 3 传入通过 getattr 获取的文件元数据结构即可
//         filter.apply(buf, file.getName, fileStat, 0)
//       }
//     }
//     // 本方法只需要返回 0 代表成功
//     0
//   }
//
//   override def statfs(path: String, stbuf: Statvfs) = {
//     logger.info("正在获取文件系统属性, path: {}", path)
//     // 仅限 Windows
//     if (OS.isWindows) {
//       // 官方注释：
//       // statfs needs to be implemented on Windows in order to allow for copying
//       // data from other devices because winfsp calculates the volume size based
//       // on the statvfs call.
//       // see https://github.com/billziss-gh/winfsp/blob/14e6b402fe3360fdebcc78868de8df27622b565f/src/dll/fuse/fuse_intf.c#L654
//       if ("/" == path) {
//         // 设定块数量
//         stbuf.f_blocks.set(1024 * 1024) // total data blocks in file system
//
//         // 设定块大小
//         stbuf.f_frsize.set(1024) // fs block size
//
//         // 剩余可用块
//         stbuf.f_bfree.set(1024 * 1024) // free blocks in fs
//
//       }
//     }
//     // 虽然调用父类方法，但实际上也只是返回 0 而已
//     super.statfs(path, stbuf)
//   }
//
//   override def rename(path: String, newName: String): Int = {
//     logger.info("正在更改文件名, path: {}, newName: {}", path, newName)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     val newParent = getParentPath(newName)
//     if (newParent == null) return -ErrorCodes.ENOENT
//     if (!newParent.isDirectory) return -ErrorCodes.ENOTDIR
//     if (p.renameTo(getPath(newName))) 0
//     else -ErrorCodes.EIO
//   }
//
//   override def rmdir(path: String): Int = {
//     logger.info("正在删除目录, path: {}", path)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isDirectory) return -ErrorCodes.ENOTDIR
//     val list = p.list
//     if (list != null && list.nonEmpty) {
//       // 这里用于检查目录是否为空，如果不为空则返回 ENOTEMPTY(Directory not empty)
//       return -ErrorCodes.ENOTEMPTY
//     }
//     if (p.delete) 0
//     else -ErrorCodes.EIO
//   }
//
//   override def truncate(path: String, offset: Long): Int = {
//     logger.info("正在清空文件数据, path: {}, offset: {}", path, offset)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isFile) return -ErrorCodes.EISDIR
//
//     val fileInput = new FileInputStream(p).getChannel
//     try {
//       fileInput.position(offset)
//       fileInput.truncate(0)
//       0
//     } catch {
//       case e: IOException =>
//         logger.error("清空文件数据时发生异常", e)
//         -ErrorCodes.EIO
//     } finally if (fileInput != null) fileInput.close()
//
//   }
//
//   override def unlink(path: String): Int = {
//     logger.info("正在删除文件, path: {}", path)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (p.delete) 0
//     else -ErrorCodes.EIO
//   }
//
//   override def open(path: String, fi: FuseFileInfo): Int = {
//     logger.info("正在打开文件, path: {}", path)
//     // 建议该方法仅检查是否存在和是否为文件即可，不适宜打开 FileChannel
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isFile) return -ErrorCodes.EISDIR
//     0
//   }
//
//   override def release(path: String, fi: FuseFileInfo): Int = {
//     logger.info("正在释放文件, path: {}", path)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isFile) return -ErrorCodes.EISDIR
//     // 这里将打开的 Channel 关闭即可，如果没开，就不管它
//     if (openMap.containsKey(p))
//       try openMap.get(p).close()
//       catch {
//         case e: IOException =>
//           logger.error("释放文件时发生异常", e)
//           return -ErrorCodes.EIO
//       } finally openMap.remove(p)
//     0
//   }
//
//   override def write(path: String, buf: Pointer, @size_t size: Long, @off_t offset: Long, fi: FuseFileInfo): Int = {
//     logger.info("正在写入文件, path: {}, size: {}, offset: {}", path, size, offset)
//     val p = getPath(path)
//     if (!p.exists) return -ErrorCodes.ENOENT
//     if (!p.isFile) return -ErrorCodes.EISDIR
//     try {
//       // 同 read 方法一样
//       var fileOutput = openMap.get(p)
//       if (fileOutput == null || !(channelStatus.get(fileOutput) == StandardOpenOption.WRITE)) {
//         release(path, null)
//         val newChannel = new FileOutputStream(p).getChannel
//         openMap.put(p, newChannel)
//         channelStatus.put(newChannel, StandardOpenOption.WRITE)
//         fileOutput = newChannel
//       }
//       val maxWriteIndex = (offset + size).toInt
//       val bytesToWrite  = new Array[Byte](size.toInt)
//       this.synchronized {
//         if (maxWriteIndex > p.length) fileOutput.truncate(maxWriteIndex)
//         buf.get(0, bytesToWrite, 0, size.toInt)
//         fileOutput.position(offset.toInt)
//         fileOutput.write(ByteBuffer.wrap(bytesToWrite))
//       }
//       size.toInt
//     } catch {
//       case e: IOException =>
//         e.printStackTrace()
//         -ErrorCodes.EIO
//     }
//   }
// }
