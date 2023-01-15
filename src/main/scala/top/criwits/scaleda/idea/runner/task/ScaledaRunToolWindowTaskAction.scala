package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.runner.configuration.{ScaledaRunConfigurationFactory, ScaledaRunConfigurationType}
import idea.utils.{MainLogger, Notification}
import idea.windows.tasks.ScaledaRunTaskNode
import kernel.project.config.ProjectConfig
import kernel.toolchain.Toolchain

import com.intellij.execution.Executor
import com.intellij.execution.impl.{RunManagerImpl, RunnerAndConfigurationSettingsImpl}
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree

class ScaledaRunToolWindowTaskAction(
    tree: Tree,
    project: Project,
    runManager: RunManagerImpl
) extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.task.name"),
      ScaledaBundle.message("tasks.action.run.wool.window.task.description"),
      AllIcons.RunConfigurations.TestState.Run
    ) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    ProjectConfig
      .getConfig()
      .foreach(c => {
        val selectedNodes =
          tree.getSelectedNodes(
            classOf[ScaledaRunTaskNode],
            (_: ScaledaRunTaskNode) => true
          )
        selectedNodes.headOption.map(selected => {
          c.taskByName(selected.name)
            .foreach(f => {
              val (target, task) = f
              // check if target and task is valid
              if (
                !Toolchain
                  .profiles()
                  .exists(_.toolchainType == target.toolchain)
              )
                Notification(project).error(
                  "Toolchain not found",
                  s"Cannot find toolchain ${target.toolchain}, check your profile list"
                )
              else {
                // val dataContext = SimpleDataContext.getSimpleContext(
                //   Location.DATA_KEY,
                //   new PsiLocation[_](task),
                //   e.getDataContext
                // )
                // val dataContext = e.getDataContext
                // val context = ConfigurationContext.getFromContext(dataContext, e.getPlace)
                val producer = new ScaledaRunConfigurationFactory(
                  ScaledaRunConfigurationType.instance
                )
                val configuration = new RunnerAndConfigurationSettingsImpl(
                  runManager,
                  producer
                    .createConfigurationFromTargetAndTask(project, target, task)
                )
                // context.getRunManager
                //   .asInstanceOf[RunManagerEx]
                //   .setTemporaryConfiguration(configuration)
                runManager.setTemporaryConfiguration(configuration)
                ExecutionUtil.runConfiguration(
                  configuration,
                  Executor.EXECUTOR_EXTENSION_NAME.getExtensionList.get(0)
                )
              }
            })
        })
      })
  }
}
