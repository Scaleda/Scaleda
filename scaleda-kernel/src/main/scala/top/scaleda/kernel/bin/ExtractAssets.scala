package top.scaleda
package kernel.bin

import top.scaleda.kernel.utils.{KernelLogger, OS, Paths}
import top.scaleda.verilog.formatter.VeribleFormatterHelper

import java.io.{File, FileOutputStream}
import java.nio.file.Path
import java.util.zip.ZipInputStream
import scala.io.Source

object ExtractAssets {
  private val ASSET_VERSION   = 5 // Update me when newer assets are loaded
  private val SMALL_FILE_SIZE = 10240

  private val targetDirectory = Paths.getBinaryDir
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
    val version    = oldVersion.mkString.trim.toIntOption.getOrElse(0)
    oldVersion.close()
    if (version < ASSET_VERSION) {
      KernelLogger.info(f"Assets version $version found, packed version is $ASSET_VERSION")
      return false
    }

    // copy or extract files from resource
    installTexts()

    // check integrity
    binaryList.forall(binary => Path.of(targetDirectory.getAbsolutePath, binary.split('/'): _*).toFile.exists())
  }

  private def installFilesFromResource(prefix: String, parent: File, files: Seq[String]): Unit =
    files.foreach(f => {
      val file = new File(parent, f)
      file.getParentFile.mkdirs()
      val resourceStream = getClass.getClassLoader.getResourceAsStream(prefix + "/" + f)
      val data           = resourceStream.readAllBytes()
      // tricky: check if file exists and has the same length
      if (
        data.length < SMALL_FILE_SIZE ||
        !(file.exists() && file.length() == data.length) ||
        (file.exists() && file.length() < SMALL_FILE_SIZE)
      ) {
        val fileOutputStream = new FileOutputStream(file)
        fileOutputStream.write(data)
        fileOutputStream.close()
        resourceStream.close()
      }
    })

  // install resources in resources/install -> .scaleda/*
  private def installTexts(): Unit = {
    val parent = Paths.getGlobalConfigDir
    if (!parent.exists()) parent.mkdirs()
    val files = Seq(
      "scripts/vivado_call.tcl",
      "ip/scaleda_bram/bram2.v.j2",
      "ip/scaleda_bram/scaleda.yml",
      "ip/clocking-wizard/scaleda.yml"
    )
    installFilesFromResource("install", parent, files)
  }

  private def installBinaries(): Unit = {
    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    installFilesFromResource("bin", targetDirectory, binaryList.toSeq :+ "version")
  }

  def run(): Boolean = {
    KernelLogger.info(f"Extracting assets, which version is $ASSET_VERSION")
    try {
      installTexts()
      installBinaries()
      true
    } catch {
      case e: Exception =>
        KernelLogger.error("Failed to extract assets", e)
        false
    }
  }
}
