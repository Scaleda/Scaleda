package top.criwits.scaleda
package idea.runner.task.edit.task

import idea.ScaledaBundle
import idea.runner.task.edit.EditDialogWrapper
import idea.utils.MainLogger
import kernel.project.config.{ProjectConfig, TargetConfig, TaskConfig}

import com.intellij.openapi.project.Project

import javax.swing.JPanel

class EditTaskDialogWrapper(target: TargetConfig, project: Project, config: Option[TaskConfig])
    extends EditDialogWrapper[TaskConfig](
      project,
      ScaledaBundle.message("windows.edit.task.title")
    ) {

  private val targetName = target.name

  var inner: EditTaskDialog = _

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
    val task = inner.getData
    checkData(targetName, task) match {
      case None => {
        ProjectConfig.insertOrReplaceTask(targetName, task) // FIXME!!!!!!
        super.doOKAction()
      }
      case Some(msg) => MainLogger.error("Cannot create/edit task:", msg)
    }
  }

  override def createMainPanel: JPanel = {
    inner = new EditTaskDialog(target, config, this)
    updateOKStatus()
    inner.getMainPanel
  }


  def updateOKStatus(): Unit = {
    setOKActionEnabled(inner != null && inner.validateConfig)
  }
//  override def getData = TaskConfig()

//  override def initPanel(): Unit = ???
}
