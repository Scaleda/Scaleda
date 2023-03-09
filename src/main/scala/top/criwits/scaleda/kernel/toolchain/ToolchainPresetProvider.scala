package top.criwits.scaleda
package kernel.toolchain

import kernel.project.config.TaskConfig

trait ToolchainPresetProvider {
  def handlePreset(task: TaskConfig): TaskConfig
}
