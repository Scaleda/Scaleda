package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.project.config.{ProjectConfig, TaskConfig}

import com.intellij.openapi.project.Project

import javax.swing.JPanel

class EditTaskDialogWrapper(project: Project)
    extends EditDialogWrapper[TaskConfig](
      project,
      ScaledaBundle.message("windows.edit.task.title")
    ) {
  var inner: EditTaskDialog = _

  private def reInit: EditTaskDialog = {
    if (inner == null) inner = new EditTaskDialog(TaskConfig())
    inner
  }

  def checkData(targetName: String, task: TaskConfig): Option[String] = {
    if (task.name.isBlank)
      return Some(ScaledaBundle.message("windows.edit.task.error.name", task.name))

    if (
      !ProjectConfig
        .getConfig()
        .exists(c => c.targets.exists(_.name == targetName))
    )
      return Some(ScaledaBundle.message("windows.edit.task.error.target", targetName))

    None
  }

  override def doOKAction() = {
    val task = reInit.getData
    checkData(reInit.getTargetName, task) match {
      case None => {
        ProjectConfig.insertOrReplaceTask(reInit.getTargetName, task)
        super.doOKAction()
      }
      case Some(msg) => MainLogger.error("Cannot create/edit task:", msg)
    }
  }

  override def getMainPanel: JPanel = reInit.getMainPanel

  override def getData = TaskConfig()
}
