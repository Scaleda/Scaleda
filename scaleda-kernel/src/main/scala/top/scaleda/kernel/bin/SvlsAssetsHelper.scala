package top.scaleda.kernel.bin

import top.scaleda.kernel.utils.{OS, Paths}

import java.io.File

object SvlsAssetsHelper {
  val executable: File = new File(new File(Paths.getBinaryDir, "svls"), "svls" + (if (OS.isWindows) ".exe" else ""))
  val allAssets: Seq[String] = Seq(
    "svls/svls",
    "svls/svls.exe"
  )
}
