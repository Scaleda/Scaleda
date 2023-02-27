package top.criwits.scaleda
package kernel.bin

import kernel.utils.Paths

import java.io.File
import java.nio.file.Files

object ExtractBinaryFiles {
  private val targetDirectory = new File(Paths.getConfigDir, "bin")
  private val binaryList      = Array("rvcd", "rvcd.exe")
  private val resourcePath    = "bin/"
  def run(): Unit = {
    for (binary <- binaryList) {
      val resource = resourcePath + binary
      try {
        val target = new File(targetDirectory, binary)
        val inputStream = getClass.getClassLoader.getResourceAsStream(resource)
        Files.copy(inputStream, target.toPath)
      } catch {
        case _e: _ => {}
      }
    }
  }
}
