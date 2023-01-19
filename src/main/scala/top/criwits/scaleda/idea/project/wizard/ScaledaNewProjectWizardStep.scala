package top.criwits.scaleda
package idea.project.wizard

import com.intellij.ide.wizard.{AbstractNewProjectWizardStep, NewProjectWizardLanguageStep}
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Panel
import top.criwits.scaleda.idea.project.ScaledaModuleBuilder

/**
 * Class for project wizard step, or call it configuration?
 * @param parent NewProjectWizardLanguageStep
 */
class ScaledaNewProjectWizardStep(private val parent: NewProjectWizardLanguageStep)
  extends AbstractNewProjectWizardStep(parent) {

  override def setupProject(project: Project): Unit = {
    val builder = new ScaledaModuleBuilder
    builder.setName(parent.getName)
    builder.setContentEntryPath(s"${parent.getPath}/${parent.getName}")
    builder.commit(project)

    // TODO: Add something?
  }
}
