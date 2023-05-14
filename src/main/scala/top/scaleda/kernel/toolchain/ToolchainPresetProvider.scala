package top.scaleda
package kernel.toolchain

import idea.runner.ScaledaRuntime
import kernel.net.remote.RemoteInfo

trait ToolchainPresetProvider {

  /** Execute in `runTask`, from runtime to new runtime
    * @param rt runtime
    * @param remoteInfo remote information
    * @return
    */
  def handlePreset(rtOld: ScaledaRuntime, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntime]
}
