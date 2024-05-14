package top.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile
import top.scaleda.kernel.utils.KernelFileUtils

import java.io.File

class Executor(
    val profile: ToolchainProfile,
    val workingDir: File = new File("."),
    val topModule: String = "",
    val constraints: Seq[File] = Seq(),
    val constraintsDir: Seq[File] = Seq()
) {

  /** Collect constraints from dirs
    * @param suffixing filter
    * @return
    */
  def collectConstraintFiles(suffixing: Set[String]): Executor = {
    val newConstraints = constraintsDir
      .map(dir => {
        if (dir.exists() && dir.isDirectory)
          KernelFileUtils.scanDirectory(directory = dir, suffixing = suffixing)
        else Seq()
      })
      .reduceOption(_ ++ _)
      .getOrElse(Seq())
    // this.copy(constraints = newConstraints, constraintsDir = Seq())
    new Executor(profile, workingDir, topModule, constraints ++ newConstraints, Seq())
  }
}
