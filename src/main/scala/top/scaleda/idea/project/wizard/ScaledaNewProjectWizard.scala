package top.scaleda
package idea.project.wizard

import idea.utils.Icons

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard

import javax.swing.Icon

/** Project wizard for new Scaleda project
  *
  * Registered as `newProjectWizard.languageGenerator`
  */
class ScaledaNewProjectWizard extends LanguageGeneratorNewProjectWizard {
  override def isEnabled(wizardContext: WizardContext): Boolean = true

  override def getName: String = "Scaleda"

  // override def createStep(
  //     p: NewProjectWizardLanguageStep
  // ): NewProjectWizardStep = new ScaledaNewProjectWizardStep(p)

  override def getIcon: Icon = Icons.project

  override def createStep(p: NewProjectWizardStep): NewProjectWizardStep =
    new ScaledaNewProjectWizardStep(p)
}
