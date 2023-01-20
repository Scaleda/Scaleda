package top.criwits.scaleda
package idea.project

import idea.ScaledaBundle
import idea.project.ScaledaModuleType.SCALEDA_MODULE

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.{ModuleType, ModuleTypeManager}

import javax.swing.Icon

/** Module type for Scaleda
  */
class ScaledaModuleType extends ModuleType[ScaledaModuleBuilder](SCALEDA_MODULE) {
  override def createModuleBuilder(): ScaledaModuleBuilder =
    new ScaledaModuleBuilder

  override def getName: String = ScaledaBundle.message("module.name")

  override def getDescription: String =
    ScaledaBundle.message("module.description")

  override def getNodeIcon(isOpened: Boolean): Icon = AllIcons.Nodes.Module
}

object ScaledaModuleType {
  val SCALEDA_MODULE: String = "SCALEDA_MODULE"

  def getInstance: ScaledaModuleType = ModuleTypeManager
    .getInstance()
    .findByID(SCALEDA_MODULE)
    .asInstanceOf[ScaledaModuleType]
}
