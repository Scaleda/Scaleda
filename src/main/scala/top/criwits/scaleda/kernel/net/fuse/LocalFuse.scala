package top.criwits.scaleda
package kernel.net.fuse

import kernel.utils.OS

import jnr.ffi.Pointer
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo, Statvfs}
import ru.serce.jnrfuse.{FuseFillDir, FuseStubFS}

import java.io.File
import java.nio.file.Files

class LocalFuse extends FuseStubFS {
  require(!OS.isWindows)
  override def getattr(path: String, stat: FileStat) = {
    val f = new File(path)
    val p = Files.getAttribute(f.toPath, "")
    0
  }

  override def readlink(path: String, buf: Pointer, size: Long) =
    super.readlink(path, buf, size)

  override def mknod(path: String, mode: Long, rdev: Long) =
    super.mknod(path, mode, rdev)

  override def mkdir(path: String, mode: Long) = super.mkdir(path, mode)

  override def unlink(path: String) = super.unlink(path)

  override def rmdir(path: String) = super.rmdir(path)

  override def symlink(oldpath: String, newpath: String) =
    super.symlink(oldpath, newpath)

  override def rename(oldpath: String, newpath: String) =
    super.rename(oldpath, newpath)

  override def link(oldpath: String, newpath: String) =
    super.link(oldpath, newpath)

  override def chmod(path: String, mode: Long) = super.chmod(path, mode)

  override def chown(path: String, uid: Long, gid: Long) =
    super.chown(path, uid, gid)

  override def truncate(path: String, size: Long) = super.truncate(path, size)

  override def open(path: String, fi: FuseFileInfo) = super.open(path, fi)

  override def read(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ) = super.read(path, buf, size, offset, fi)

  override def write(
      path: String,
      buf: Pointer,
      size: Long,
      offset: Long,
      fi: FuseFileInfo
  ) = super.write(path, buf, size, offset, fi)

  override def statfs(path: String, stbuf: Statvfs) = super.statfs(path, stbuf)

  override def flush(path: String, fi: FuseFileInfo) = super.flush(path, fi)

  override def release(path: String, fi: FuseFileInfo) = super.release(path, fi)

  override def fsync(path: String, isdatasync: Int, fi: FuseFileInfo) =
    super.fsync(path, isdatasync, fi)

  override def setxattr(
      path: String,
      name: String,
      value: Pointer,
      size: Long,
      flags: Int
  ) = super.setxattr(path, name, value, size, flags)

  override def getxattr(
      path: String,
      name: String,
      value: Pointer,
      size: Long
  ) = super.getxattr(path, name, value, size)

  override def listxattr(path: String, list: Pointer, size: Long) =
    super.listxattr(path, list, size)

  override def removexattr(path: String, name: String) =
    super.removexattr(path, name)

  override def opendir(path: String, fi: FuseFileInfo) = super.opendir(path, fi)

  override def readdir(
      path: String,
      buf: Pointer,
      filter: FuseFillDir,
      offset: Long,
      fi: FuseFileInfo
  ) = super.readdir(path, buf, filter, offset, fi)

  override def releasedir(path: String, fi: FuseFileInfo) =
    super.releasedir(path, fi)

  override def fsyncdir(path: String, fi: FuseFileInfo) =
    super.fsyncdir(path, fi)

  override def init(conn: Pointer) = super.init(conn)

  override def destroy(initResult: Pointer) = super.destroy(initResult)

  override def access(path: String, mask: Int) = super.access(path, mask)

  override def create(path: String, mode: Long, fi: FuseFileInfo) =
    super.create(path, mode, fi)

  override def ftruncate(path: String, size: Long, fi: FuseFileInfo) =
    super.ftruncate(path, size, fi)

  override def fgetattr(path: String, stbuf: FileStat, fi: FuseFileInfo) =
    super.fgetattr(path, stbuf, fi)
}
