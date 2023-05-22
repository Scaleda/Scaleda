package top.scaleda
package kernel.toolchain

import top.scaleda.kernel.net.remote.RemoteInfo
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

trait ToolchainPresetProvider {

  /** Execute in `runTask`, from runtime to new runtime
    * @param rt runtime
    * @param remoteInfo remote information
    * @return
    */
  def handlePreset(rtOld: ScaledaRuntime, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntime]
}
