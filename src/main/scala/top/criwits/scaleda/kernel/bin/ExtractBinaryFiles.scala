package top.criwits.scaleda
package kernel.bin

import kernel.utils.{KernelLogger, OS, Paths}

import java.io.{File, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import scala.collection.mutable.ArrayBuffer

object ExtractBinaryFiles {
  private val ZIP_BUFFER_SIZE = 1024

  private val targetDirectory = Paths.getBinaryDir
  private val binaryList = Array("rvcd", "rvcd.exe")
  private val resourceFile = "bin/assets.zip"

  def isInstalled: Boolean = {
    binaryList.map(binary => new File(targetDirectory, binary).exists()).reduce(_ && _)
  }

  def run(): Unit = {
    val copiedList = ArrayBuffer[String]()
    if (!targetDirectory.exists()) targetDirectory.mkdirs();
    val resourceStream = getClass.getClassLoader.getResourceAsStream(resourceFile)
    val zipInputStream = new ZipInputStream(resourceStream)
    var zipEntry = zipInputStream.getNextEntry

    val buffer = new Array[Byte](ZIP_BUFFER_SIZE)

    while (zipEntry != null) {
      val fileName = zipEntry.getName
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
      zipInputStream.closeEntry()

      // chmod for *nix
      if (!OS.isWindows && binaryList.contains(fileName)) {
        import sys.process._
        s"chmod +x ${newFile.getAbsolutePath}".!
      }
      zipEntry = zipInputStream.getNextEntry
    }
    zipInputStream.closeEntry()
    zipInputStream.close()
    resourceStream.close()
  }
}

//    for (binary <- binaryList) {
//      val resource = resourcePath + binary
//      try {
//        val target         = new File(targetDirectory, binary)
//        val resourceStream =
//        if (force || !target.exists()) {
//          resourceStream.transferTo(new FileOutputStream(target))
//          if (!OS.isWindows) {
//
//          }
//          copiedList += binary
//        }
//      } catch {
//        case _e: NullPointerException => {}
//        case e: Throwable =>
//          KernelLogger.warn("Cannot extract binary file", binary, ": ", e)
//          e.printStackTrace()
//      }
//    }
//    if (copiedList.nonEmpty) KernelLogger.info("copied binaries:", copiedList.mkString(", "))
//    copiedList.toSeq
//  }
//}

// object ExtractTest extends App {
//   ExtractBinaryFiles.run()
// }
