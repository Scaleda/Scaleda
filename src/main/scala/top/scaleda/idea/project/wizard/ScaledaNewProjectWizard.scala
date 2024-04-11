package top.scaleda
package idea.project.wizard

import idea.project.wizard.ScaledaNewProjectWizard.ScaledaLanguageText

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard
import com.intellij.ide.wizard.{LanguageNewProjectWizard, NewProjectWizardLanguageStep, NewProjectWizardStep}
import top.scaleda.idea.utils.Icons

import javax.swing.Icon

/** Project wizard for new Scaleda project
  *
  * Registered as `newProjectWizard.language`
  */
class ScaledaNewProjectWizard extends LanguageGeneratorNewProjectWizard {
  override def isEnabled(wizardContext: WizardContext): Boolean = true

  override def getName: String = ScaledaLanguageText

  // override def createStep(
  //     p: NewProjectWizardLanguageStep
  // ): NewProjectWizardStep = new ScaledaNewProjectWizardStep(p)

  override def getIcon: Icon = Icons.project

  override def createStep(p: NewProjectWizardStep): NewProjectWizardStep =
    new ScaledaNewProjectWizardStep(p)
}

object ScaledaNewProjectWizard {
  val ScaledaLanguageText: String = "Scaleda"
}
