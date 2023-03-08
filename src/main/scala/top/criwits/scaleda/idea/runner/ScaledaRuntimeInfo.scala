package top.criwits.scaleda
package idea.runner

import kernel.project.config.{TargetConfig, TaskConfig}
import kernel.toolchain.ToolchainProfile

case class ScaledaRuntimeInfo(
    id: String,
    target: TargetConfig,
    task: TaskConfig,
    profile: ToolchainProfile
)
