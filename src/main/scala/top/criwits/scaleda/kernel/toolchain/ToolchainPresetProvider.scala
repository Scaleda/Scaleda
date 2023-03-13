package top.criwits.scaleda
package kernel.toolchain

import idea.runner.ScaledaRuntimeInfo
import kernel.net.remote.RemoteInfo

trait ToolchainPresetProvider {

  /** Execute in `runTask`, from runtime to new runtime
    * @param rt runtime
    * @param remoteInfo remote information
    * @return
    */
  def handlePreset(rtOld: ScaledaRuntimeInfo, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntimeInfo]
}
