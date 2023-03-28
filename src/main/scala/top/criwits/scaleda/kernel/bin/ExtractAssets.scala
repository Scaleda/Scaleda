package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, OS, Paths}
import verilog.editor.formatter.VeribleFormatterHelper

import java.io.{File, FileOutputStream}
import java.util.zip.ZipInputStream
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object ExtractAssets {
  private val ASSET_VERSION = 1 // Update me when newer assets are loaded
  private val ZIP_BUFFER_SIZE = 1024

  private val targetDirectory = Paths.getBinaryDir
  private val binaryList      = Array("rvcd", "rvcd.exe") ++ VeribleFormatterHelper.allVeribleAssets
  private val resourceFile    = "bin/assets.zip"

  def isInstalled: Boolean = {
    // check if version file
    val versionFile = new File(targetDirectory, "version")
    if (!versionFile.exists()) return false

    // check
    val oldVersion = Source.fromFile(versionFile)
    val version = oldVersion.getLines().mkString("").toInt
    oldVersion.close()
    if (version < ASSET_VERSION) return false

    // check integrity
    binaryList.forall(binary => new File(targetDirectory, binary).exists())
  }

  def run(): Boolean = {
    val copiedList = ArrayBuffer[String]()
    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    val resourceStream = getClass.getClassLoader.getResourceAsStream(resourceFile)
    if (resourceStream == null) {
      KernelLogger.warn("Assets not found! Check your", resourceFile)
      return false
    }

    try { // TODO: is it ok here?
      val zipInputStream = new ZipInputStream(resourceStream)
      var zipEntry = zipInputStream.getNextEntry

      val buffer = new Array[Byte](ZIP_BUFFER_SIZE)

      while (zipEntry != null) {
        val fileName = zipEntry.getName

        if (!zipEntry.isDirectory) {
          val newFile = new File(targetDirectory, fileName)
          KernelLogger.info(s"Extracting asset file $fileName")
          new File(newFile.getParent).mkdirs()
          val fileOutputStream = new FileOutputStream(newFile)
          var len = 0
          while ( {
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

      true
    } catch {
      case _: Throwable => false
    }
  }
}
