package top.scaleda
package verilog.formatter

import kernel.utils.{OS, Paths}

import java.io.File

object VeribleAssetsHelper {
  private val VERIBLE = "verible"
  private val WIN64 = "win64"
  private val LINUX64 = "lin64"
  private val FORMATTER = "verible-verilog-format"
  private val LSP = "verible-verilog-ls"

  def allVeribleAssets: Seq[String] = Seq(
    VERIBLE + "/" + WIN64 + "/" + FORMATTER + ".exe",
    VERIBLE + "/" + LINUX64 + "/" + FORMATTER,
    VERIBLE + "/" + WIN64 + "/" + LSP + ".exe",
    VERIBLE + "/" + LINUX64 + "/" + LSP
  )

  private val path = VERIBLE + "/" + (OS.getOSType match {
    case OS.Windows => WIN64 // TODO: 32 bit
    case OS.Unix => LINUX64
    case _ => "none" // FIXME
  }) + "/" + FORMATTER + (if (OS.isWindows) ".exe" else "")

  val veribleFormatter: File = new File(Paths.getBinaryDir, path)
}
