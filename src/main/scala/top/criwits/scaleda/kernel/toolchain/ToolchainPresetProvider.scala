package top.criwits.scaleda
package kernel.toolchain

import idea.runner.ScaledaRuntimeInfo

trait ToolchainPresetProvider {
  def handlePreset(rt: ScaledaRuntimeInfo): Option[ScaledaRuntimeInfo]
}
