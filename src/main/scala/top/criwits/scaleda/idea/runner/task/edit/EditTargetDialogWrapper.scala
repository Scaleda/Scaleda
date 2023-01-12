package top.criwits.scaleda
package idea.runner.task.edit

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.project.config.{ProjectConfig, TargetConfig}
import kernel.toolchain.Toolchain

import com.intellij.openapi.project.Project

import javax.swing.JPanel

class EditTargetDialogWrapper(project: Project, initial: TargetConfig)
    extends EditDialogWrapper[TargetConfig](
      project,
      ScaledaBundle.message("windows.edit.target.title")
    ) {
  var inner = new EditTargetDialog(initial)

  private def reInit: EditTargetDialog = {
    if (inner == null) inner = new EditTargetDialog(TargetConfig())
    inner
  }

  private def checkData(target: TargetConfig): Option[String] = {
    if (target.name.isBlank)
      return Some(ScaledaBundle.message("windows.edit.target.error.name"))
    if (!Toolchain.profiles().exists(_.profileName == target.toolchain))
      return Some(ScaledaBundle.message("windows.edit.target.error.toolchain"))
    if (target.device.isBlank)
      return Some(ScaledaBundle.message("windows.edit.target.error.device"))
    if (target.`package`.isBlank)
      return Some(ScaledaBundle.message("windows.edit.target.error.package"))
    if (target.speed == 0 || target.speed > 3)
      return Some(ScaledaBundle.message("windows.edit.target.error.speed"))

    None
  }

  override def doOKAction() = {
    try {
      val target = reInit.getData
      checkData(target) match {
        case None => {
          ProjectConfig.insertOrReplaceTarget(target)
          super.doOKAction()
        }
        case Some(msg) => MainLogger.error("Cannot create/edit target:", msg)
      }
    } catch {
      case e: NumberFormatException =>
        MainLogger.error("Input number format error: ", e)
    }
  }

  override def getMainPanel: JPanel = reInit.getMainPanel

  override def getData: TargetConfig = reInit.getData
}
