// https://gist.github.com/LamGC/ebd1cdd74dd8532485d6762c029f276d

package top.criwits.scaleda
package kernel

import kernel.net.fuse.{FuseSimple, FuseUtils}
import kernel.utils.Paths

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io._

class SimpleProxyFileSystemTest extends AnyFlatSpec with should.Matchers {
  behavior of "SimpleProxyFileSystem"
  it should "simple run test" in {
    val fs = new FuseSimple(new File(Paths.getServerTemporalDir(), "source"))
    FuseUtils.mountFs(fs, new File(Paths.getServerTemporalDir(), "simple").getAbsolutePath, blocking = true)
  }
}
