package top.criwits.scaleda
package kernel

import kernel.utils.{KernelLogger, OS, Paths}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PathsTester extends AnyFlatSpec with should.Matchers {
  behavior of "Paths"
  it should "test get executable absolute path" in {
    OS.getOSType match {
      case OS.Windows => {
        KernelLogger.info(Paths.findExecutableOnPath("notepad.exe"))
        KernelLogger.info(Paths.findExecutableOnPath("notepad"))
      }
      case _ => KernelLogger.info(Paths.findExecutableOnPath("bash"))
    }
  }
}
