package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.openapi.project.Project
import top.criwits.scaleda.kernel.project.config.ProjectConfig

class ScaledaRunToolWindowOption(val project: Project) {
  def getRootNode: ScaledaRunRootNode = {
    ProjectConfig
      .getConfig()
      .map(c => {
        new ScaledaRunRootNode(
          c.targets.map(t => new ScaledaRunTargetNode(t)).toList
        )
      })
      .getOrElse(new ScaledaRunRootNode(Seq()))
  }
}
