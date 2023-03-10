package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, OS, Paths}

import java.io.{File, FileOutputStream}
import java.util.zip.ZipInputStream
import scala.collection.mutable.ArrayBuffer

object ExtractAssets {
  private val ZIP_BUFFER_SIZE = 1024

  private val targetDirectory = Paths.getBinaryDir
  private val binaryList      = Array("rvcd", "rvcd.exe")
  private val resourceFile    = "bin/assets.zip"

  def isInstalled: Boolean = {
    binaryList.map(binary => new File(targetDirectory, binary).exists()).reduce(_ && _)
  }

  def run(): Unit = {
    val copiedList = ArrayBuffer[String]()
    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    val resourceStream = getClass.getClassLoader.getResourceAsStream(resourceFile)
    if (resourceStream == null) {
      KernelLogger.warn("Assets not found! Check your", resourceFile)
      return
    }
    val zipInputStream = new ZipInputStream(resourceStream)
    var zipEntry       = zipInputStream.getNextEntry

    val buffer = new Array[Byte](ZIP_BUFFER_SIZE)

    while (zipEntry != null) {
      val fileName = zipEntry.getName

      if (!zipEntry.isDirectory) {
        val newFile = new File(targetDirectory, fileName)
        KernelLogger.info(s"Extracting asset file $fileName")
        new File(newFile.getParent).mkdirs()
        val fileOutputStream = new FileOutputStream(newFile)
        var len              = 0
        while ({
          len = zipInputStream.read(buffer)
          len > 0
        }) {
          fileOutputStream.write(buffer, 0, len)
        }
        fileOutputStream.close()

        // chmod for *nix
        if (!OS.isWindows && binaryList.contains(fileName)) {
          import sys.process._
          s"""chmod +x \"${newFile.getAbsolutePath}\"""".!
        }
      }
      zipInputStream.closeEntry()

      zipEntry = zipInputStream.getNextEntry
    }

    zipInputStream.closeEntry()
    zipInputStream.close()
    resourceStream.close()
  }
}
