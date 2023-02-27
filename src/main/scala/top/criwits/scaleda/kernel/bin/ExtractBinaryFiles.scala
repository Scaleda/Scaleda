package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, Paths}

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import scala.collection.mutable.ArrayBuffer

object ExtractBinaryFiles {
  private val targetDirectory = new File(Paths.getConfigDir, "bin")
  private val binaryList      = Array("rvcd", "rvcd.exe")
  private val resourcePath    = "bin/"
  def run(): Seq[String] = {
    val copiedList = ArrayBuffer[String]()
    if (!targetDirectory.exists()) targetDirectory.mkdirs();
    for (binary <- binaryList) {
      val resource = resourcePath + binary
      try {
        val target = new File(targetDirectory, binary)
        // TODO: check working in jar
        val resourceUrl = getClass.getClassLoader.getResource(resource)
        val f           = new File(resourceUrl.getFile)
        if (!(f.exists() && f.isFile && f.length() > 0))
          KernelLogger.error("error archive file:", f)
        if (target.length() != f.length()) {
          Files.copy(f.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
          copiedList += binary
        }
      } catch {
        // case _e: NullPointerException => {}
        case e: Throwable             => KernelLogger.warn("Cannot extract binary file", binary, ": ", e)
      }
    }
    if (copiedList.nonEmpty) KernelLogger.info("copied binaries:", copiedList.mkString(", "))
    copiedList.toSeq
  }
}

// object ExtractTest extends App {
//   ExtractBinaryFiles.run()
// }
