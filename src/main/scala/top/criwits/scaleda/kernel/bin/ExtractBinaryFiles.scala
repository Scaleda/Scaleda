package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, Paths}

import java.io.File
import java.nio.file.{Files, StandardCopyOption}

object ExtractBinaryFiles {
  private val targetDirectory = new File(Paths.getConfigDir, "bin")
  private val binaryList      = Array("rvcd", "rvcd.exe")
  private val resourcePath    = "bin/"
  def run(): Unit = {
    if (!targetDirectory.exists()) targetDirectory.mkdirs();
    for (binary <- binaryList) {
      val resource = resourcePath + binary
      try {
        val target = new File(targetDirectory, binary)
        // TODO: check working in jar
        val resourceUrl = getClass.getClassLoader.getResource(resource)
        val f = new File(resourceUrl.getFile)
        if (target.length() != f.length())
          Files.copy(f.toPath, target.toPath, StandardCopyOption.REPLACE_EXISTING)
      } catch {
        case _e: NullPointerException => {}
        case e: Throwable             => KernelLogger.warn("Cannot extract binary file", binary, ": ", e)
      }
    }
  }
}

// object ExtractTest extends App {
//   ExtractBinaryFiles.run()
// }
