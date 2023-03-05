package top.criwits.scaleda
package kernel

import kernel.net.fuse.{FuseRpcServer, FuseUtils, LocalFuse, ServerSideFuse}
import kernel.utils.{KernelLogger, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import ru.serce.jnrfuse.FuseStubFS

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.{BasicFileAttributes, PosixFileAttributes, PosixFilePermissions}
import scala.jdk.CollectionConverters._
import scala.sys.process._

class EmptyFuse extends FuseStubFS

class FuseTester extends AnyFlatSpec with should.Matchers {
  behavior of "Fuse"

  def testPath = OS.getOSType match {
    case OS.Windows => "J:\\"
    case _          => "/tmp/mnth"
  }

  it should "test EmptyFuse" in {
    val path = testPath
    val t = new Thread(() => FuseUtils.mountFs(new EmptyFuse, path))
    t.setDaemon(true)
    t.start()
    Thread.sleep(500)
    if (!OS.isWindows) {
      ("mount" #| s"grep \"$path\"").!
      s"ls $path".!
    }
    t.interrupt()
    Thread.sleep(300)
    KernelLogger.info("all done")
  }
  it should "test file attrs read" in {
    val f = new File("/tmp/mnth")
    if (!f.exists()) f.mkdirs()
    val p = f.toPath
    val attrs = Files.readAttributes(p, classOf[BasicFileAttributes])
    println("-- Some BasicFileAttributes --")
    printf("creationTime     = %s%n", attrs.creationTime)
    printf("lastAccessTime   = %s%n", attrs.lastAccessTime)
    printf("lastModifiedTime = %s%n", attrs.lastModifiedTime)
    printf("size             = %s%n", attrs.size)
    printf("directory        = %s%n", attrs.isDirectory)
    if (!OS.isWindows) {
      val attrs = Files.readAttributes(p, classOf[PosixFileAttributes])
      val perms = attrs.permissions().asScala
      PosixFilePermissions.asFileAttribute(perms.asJava)
      println(perms.map(i => s"${i.name()}: ${i.ordinal()}").mkString(", "))
      println(PosixFilePermissions.toString(perms.asJava))
    }
  }
  it should "test unix file attr to int" in {
    if (!OS.isWindows) {
      val path = "/tmp/test-file"
      val file = new File(path)
      if (!file.exists()) file.mkdirs()
      s"chmod 756 $path".!
      println(s"attr before ${FuseUtils.fileAttrsUnixString(file)}: ${Integer.toOctalString(FuseUtils.fileAttrsUnixToInt(file))}")
      s"chmod 777 $path".!
      println(s"attr after ${FuseUtils.fileAttrsUnixString(file)}: ${Integer.toOctalString(FuseUtils.fileAttrsUnixToInt(file))}")
      s"chmod 755 $path".!
    } else {
      // TODO: test Fuse on Windows platform
    }
  }
  // will not run this by default,
  // cause it detects file to stop testing
  ignore should "test local fs" in {
    if (!OS.isWindows) {
      val source = "/tmp/mnt-source"
      val dest = "/tmp/mnt"
      val fs = new LocalFuse(source)
      FuseUtils.mountFs(fs, dest, blocking = false)
      // Thread.sleep(1000 * 60)
      // touch source/exit to exit
      val exitFile = new File(source, "exit")
      while (!exitFile.exists()) Thread.sleep(100)
      exitFile.delete()
      fs.umount()
      KernelLogger.info("done")
    }
  }
  it should "test remote fs" in {
    val source = "/tmp/mnt-source"
    val dest = "/tmp/mnt"
    val t = new Thread(() => FuseRpcServer.start(source))
    t.setDaemon(true)
    t.start()
    val fs = new ServerSideFuse
    FuseUtils.mountFs(fs, dest, blocking = false)
    try {
      val exitFile = new File(source, "exit")
      while (!exitFile.exists()) Thread.sleep(100)
      exitFile.delete()
    } finally {
      fs.umount()
    }
  }
}
