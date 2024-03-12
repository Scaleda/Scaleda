package top.scaleda
package kernel.bin

import top.scaleda.kernel.utils.{KernelLogger, OS, Paths}
import top.scaleda.verilog.formatter.VeribleFormatterHelper

import java.io.{File, FileOutputStream}
import java.nio.file.Path
import java.util.zip.ZipInputStream
import scala.io.Source

object ExtractAssets {
  private val ASSET_VERSION   = 2 // Update me when newer assets are loaded
  private val ZIP_BUFFER_SIZE = 1024

  private val targetDirectory = Paths.getBinaryDir
  private val resourceFile    = "bin/assets.zip"
  private val binaryList =
    Array("rvcd", "rvcd.exe") ++
      Array("surfer", "surfer.exe") ++
      VeribleFormatterHelper.allVeribleAssets

  def isInstalled: Boolean = {
    // check if version file
    val versionFile = new File(targetDirectory, "version")
    if (!versionFile.exists()) return false

    // check
    val oldVersion = Source.fromFile(versionFile)
    val version    = oldVersion.getLines().mkString("").toInt
    oldVersion.close()
    KernelLogger.info(f"Assets version ${version} found, packed version is ${ASSET_VERSION}")
    if (version < ASSET_VERSION) return false

    // copy or extract files from resource
    install()

    // check integrity
    binaryList.forall(binary => Path.of(targetDirectory.getAbsolutePath, binary.split('/'): _*).toFile.exists())
  }

  // install resources in resources/install -> .scaleda/*
  def install(): Unit = {
    val parent = Paths.getGlobalConfigDir
    val files = Seq(
      "scripts/vivado_call.tcl",
      "ip/scaleda_bram/bram2.v.j2",
      "ip/scaleda_bram/scaleda.yml",
      "ip/clocking-wizard/scaleda.yml"
    )
    files.foreach(f => {
      val file = new File(parent, f)
      file.getParentFile.mkdirs()
      val resourceStream = getClass.getClassLoader.getResourceAsStream("install/" + f)
      val data           = resourceStream.readAllBytes()
      // tricky: check if file exists and has the same length
      if (!(file.exists() && file.length() == data.length)) {
        val fileOutputStream = new FileOutputStream(file)
        fileOutputStream.write(data)
        fileOutputStream.close()
        resourceStream.close()
      }
    })
  }

  def run(): Boolean = {
    install()
    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    val resourceStream = getClass.getClassLoader.getResourceAsStream(resourceFile)
    if (resourceStream == null) {
      KernelLogger.warn("Assets not found! Check your", resourceFile)
      return false
    }

    try { // TODO: is it ok here?
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

      true
    } catch {
      case _: Throwable => false
    }
  }
}
