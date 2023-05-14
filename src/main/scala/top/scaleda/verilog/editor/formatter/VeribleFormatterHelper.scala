package top.scaleda
package verilog.editor.formatter

import top.scaleda.kernel.utils.{OS, Paths}

import java.io.File

object VeribleFormatterHelper {
  private val VERIBLE = "verible"
  private val WIN64 = "win64"
  private val LINUX64 = "lin64"
  private val FORMATTER = "verible-verilog-format"

  def allVeribleAssets = Seq(
    VERIBLE + "/" + WIN64 + "/" + FORMATTER + ".exe",
    VERIBLE + "/" + LINUX64 + "/" + FORMATTER,
  )

  private val path = VERIBLE + "/" + (OS.getOSType match {
    case OS.Windows => WIN64 // TODO: 32 bit
    case OS.Unix => LINUX64
    case _ => "none" // FIXME
  }) + "/" + FORMATTER + (if (OS.isWindows) ".exe" else "")

  val veribleFormatter: File = new File(Paths.getBinaryDir, path)
}
