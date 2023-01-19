package top.criwits.scaleda
package idea.project

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module
import com.intellij.openapi.module.{ModifiableModuleModel, ModuleType}
import com.intellij.openapi.project.Project
import top.criwits.scaleda.idea.project.ScaledaModuleBuilder.{GROUP_NAME, ICON}
import top.criwits.scaleda.idea.utils.{Icons, MainLogger}

import javax.swing.Icon

/** Module builder for [[ScaledaModuleType]]
 *
 */
class ScaledaModuleBuilder extends ModuleBuilder {
  override def getModuleType: ModuleType[_] = ScaledaModuleType.getInstance

  override def getNodeIcon: Icon = ICON

  override def getGroupName: String = GROUP_NAME

//  override def getBuilderId: String = getClass.getName

  override def commitModule(project: Project, model: ModifiableModuleModel): module.Module = {
    val module = super.commitModule(project, model)
    // generate something
    MainLogger.info(s"Generating new Scaleda project ${getName}")
    module
  }
}

object ScaledaModuleBuilder {
  val GROUP_NAME: String = "Scaleda"
  val ICON: Icon = Icons.mainSmall
}
