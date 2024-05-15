package top.scaleda
package kernel.utils

object ScaledaClean {
  def run(): Unit = {
    // remove ~/.scaleda
    val globalConfig = Paths.getGlobalConfigDir
    if (globalConfig.exists()) {
      // may delete a file called ~/.scaleda
      globalConfig.delete()
      if (globalConfig.exists())
        // recursively delete this path
        KernelFileUtils.deleteDirectory(globalConfig.toPath)
    }
    require(!globalConfig.exists(), "Delete global config failed")
    // TODO: remove .sim, .synth?
  }
}
