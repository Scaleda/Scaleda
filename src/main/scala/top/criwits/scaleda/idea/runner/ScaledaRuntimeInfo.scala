package top.criwits.scaleda
package idea.runner

import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig}
import kernel.toolchain.ToolchainProfile
import kernel.toolchain.executor.Executor

import java.io.File

case class ScaledaRuntimeInfo(
    id: String,
    target: TargetConfig,
    task: TaskConfig,
    profile: ToolchainProfile,
    executor: Executor,
    workingDir: File = new File(ProjectConfig.projectBase.get)
)
