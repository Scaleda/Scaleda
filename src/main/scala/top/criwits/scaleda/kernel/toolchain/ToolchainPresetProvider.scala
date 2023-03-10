package top.criwits.scaleda
package kernel.toolchain

import idea.runner.ScaledaRuntimeInfo

import top.criwits.scaleda.kernel.net.remote.RemoteInfo

trait ToolchainPresetProvider {
  def handlePreset(rt: ScaledaRuntimeInfo, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntimeInfo]
}
