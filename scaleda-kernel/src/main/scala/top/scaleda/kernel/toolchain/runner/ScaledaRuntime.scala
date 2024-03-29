package top.scaleda
package kernel.toolchain.runner

import kernel.project.ProjectManifest
import kernel.project.config.{TargetConfig, TaskConfig}
import kernel.toolchain.ToolchainProfile
import kernel.toolchain.executor.Executor

import java.io.File

/** An immutable runtime in scaleda run
  * @param id id of this run
  * @param target target config
  * @param task task config
  * @param profile profile config
  * @param executor executor
  * @param projectBase pwd
  * @param context immutable context data
  */
case class ScaledaRuntime(
    id: String,
    target: TargetConfig,
    task: TaskConfig,
    profile: ToolchainProfile,
    executor: Executor,
    projectBase: File, // = new File(ProjectConfig.projectBase.get),
    extraEnvs: Map[String, String] = Map(),
    // used in preset and message handler
    context: Map[String, Any] = Map(),
    // mark, only run preset once
    stage: ScaledaRunStage.Value = ScaledaRunStage.Prepare,
    manifest: ProjectManifest
)

object ScaledaRunStage extends Enumeration {
  val Prepare, PresetDone, Running = Value
}
