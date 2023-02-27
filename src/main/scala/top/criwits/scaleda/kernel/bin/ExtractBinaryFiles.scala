package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, OS, Paths}

import java.io.{File, FileOutputStream}
import scala.collection.mutable.ArrayBuffer

object ExtractBinaryFiles {
  private val targetDirectory = new File(Paths.getConfigDir, "bin")
  private val binaryList      = Array("rvcd", "rvcd.exe")
  private val resourcePath    = "bin/"
  def run(force: Boolean = false): Seq[String] = {
    val copiedList = ArrayBuffer[String]()
    if (!targetDirectory.exists()) targetDirectory.mkdirs();
    for (binary <- binaryList) {
      val resource = resourcePath + binary
      try {
        val target         = new File(targetDirectory, binary)
        val resourceStream = getClass.getClassLoader.getResourceAsStream(resource)
        if (force || !target.exists()) {
          resourceStream.transferTo(new FileOutputStream(target))
          if (!OS.isWindows) {
            import sys.process._
            s"chmod +x ${target.getAbsolutePath}".!
          }
          copiedList += binary
        }
      } catch {
        case _e: NullPointerException => {}
        case e: Throwable =>
          KernelLogger.warn("Cannot extract binary file", binary, ": ", e)
          e.printStackTrace()
      }
    }
    if (copiedList.nonEmpty) KernelLogger.info("copied binaries:", copiedList.mkString(", "))
    copiedList.toSeq
  }
}

// object ExtractTest extends App {
//   ExtractBinaryFiles.run()
// }
