package top.criwits.scaleda
package kernel

import kernel.net.fuse.FuseUtils
import kernel.utils.{KernelLogger, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import ru.serce.jnrfuse.FuseStubFS

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
  it should "test local fs" in {}
}
