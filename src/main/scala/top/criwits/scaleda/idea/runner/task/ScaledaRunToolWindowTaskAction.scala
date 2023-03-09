package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.runner.configuration.{
  ScaledaRunConfigurationFactory,
  ScaledaRunConfigurationType
}
import idea.windows.tasks.ScaledaRunTaskNode
import kernel.project.config.ProjectConfig

import com.intellij.execution.Executor
import com.intellij.execution.impl.{
  RunManagerImpl,
  RunnerAndConfigurationSettingsImpl
}
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree

/**
 * Action: run task in right panel
 *
 * @param tree Tree in the right panel, used to detect whether task is picked
 * @param project the project
 * @param runManager IJ [[RunManager]]
 */
class ScaledaRunToolWindowTaskAction(
    tree: Tree,
    project: Project,
    runManager: RunManagerImpl
) extends AnAction(
      ScaledaBundle.message("tasks.action.run.tool.window.task.name"),
      ScaledaBundle.message("tasks.action.run.wool.window.task.description"),
      AllIcons.RunConfigurations.TestState.Run
    ) {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val selectedNodes = tree.getSelectedNodes(classOf[ScaledaRunTaskNode], (_: ScaledaRunTaskNode) => true)
    presentation.setEnabled(selectedNodes.nonEmpty)
  }

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
          c.taskByTaskTargetName(selected.name, selected.parent.get.name) // FIXME: can get????
            .foreach(f => {
              val (target, task) = f
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
            })
        })
      })
  }
}
