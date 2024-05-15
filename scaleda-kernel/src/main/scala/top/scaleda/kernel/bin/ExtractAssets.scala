package top.scaleda
package kernel.bin

import top.scaleda.kernel.utils.{KernelLogger, OS, Paths}
import top.scaleda.verilog.formatter.VeribleAssetsHelper

import java.io.{File, FileOutputStream}
import java.nio.file.Path
import java.util.zip.ZipInputStream
import scala.io.Source

object ExtractAssets {
  private val ASSET_VERSION   = 5 // Update me when newer assets are loaded
  private val SMALL_FILE_SIZE = 10240

  private val targetBinaryDirectory = Paths.getBinaryDir
  private val binaryList =
    Seq("rvcd", "rvcd.exe") ++
      Seq("surfer", "surfer.exe") ++
      VeribleAssetsHelper.allVeribleAssets

  def isInstalled: Boolean = {
    // check if version file
    val versionFile = new File(targetBinaryDirectory, "version")
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
    binaryList.forall(binary => {
      val file = Path.of(targetBinaryDirectory.getAbsolutePath, binary.split('/'): _*).toFile
      file.exists() && file.isFile && file.canExecute
    })
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

  private def setBinariesExecutable(): Unit = {
    binaryList.foreach(binary => {
      val file = Path.of(targetBinaryDirectory.getAbsolutePath, binary.split('/'): _*).toFile
      if (file.exists() && !file.canExecute) file.setExecutable(true)
    })
  }

  private def installBinaries(): Unit = {
    if (!targetBinaryDirectory.exists()) targetBinaryDirectory.mkdirs()
    installFilesFromResource("bin", targetBinaryDirectory, binaryList.toSeq :+ "version")
  }

  def run(): Boolean = {
    KernelLogger.info(f"Extracting assets, which version is $ASSET_VERSION")
    try {
      installTexts()
      installBinaries()
      setBinariesExecutable()
      true
    } catch {
      case e: Exception =>
        KernelLogger.error("Failed to extract assets", e)
        false
    }
  }
}
