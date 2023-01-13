package top.criwits.scaleda
package kernel

import kernel.net.fuse.FuseUtils
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
    }
  }
  it should "test local fs" in {}
}
