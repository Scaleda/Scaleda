package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.project.config.{ProjectConfig, TargetConfig}
import kernel.toolchain.Toolchain

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.runner.task.ScaledaReloadTasksAction
import top.criwits.scaleda.idea.runner.task.edit.target.{EditTargetDialog, TargetBasicPanel}

import javax.swing.JPanel

class EditTargetDialogWrapper(project: Project, config: Option[TargetConfig])
    extends EditDialogWrapper[TargetConfig](
      project,
      ScaledaBundle.message("windows.edit.target.title")
    ) {

  var inner: EditTargetDialog = _

//
//  private def reInit: EditTargetDialog = { // why this? inner should never be null???
//    if (inner == null) inner = new EditTargetDialog(config)
//    inner
//  }

  private def checkData(target: TargetConfig): Option[String] = {
    // TODO: Only detect name-collapse
    None
  }

  override def doOKAction() = {
    try {
      val target = inner.getData
      checkData(target) match {
        case None => {
          ProjectConfig.insertOrReplaceTarget(target)
          ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)
          super.doOKAction()
        }
        case Some(msg) => MainLogger.error("Cannot create/edit target:", msg)
      }
    } catch {
      case e: NumberFormatException =>
        MainLogger.error("Input number format error: ", e)
    }
  }

  override def createMainPanel: JPanel = {
    inner = new EditTargetDialog(config, this)
    updateOKStatus()
    inner.getMainPanel
  }

  def updateOKStatus(): Unit = {
    setOKActionEnabled(inner != null && inner.validateConfig)
  }


}
