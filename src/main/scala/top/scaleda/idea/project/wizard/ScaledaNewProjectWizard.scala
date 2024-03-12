package top.scaleda
package idea.project.wizard

import idea.project.wizard.ScaledaNewProjectWizard.ScaledaLanguageText

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.{LanguageNewProjectWizard, NewProjectWizardLanguageStep, NewProjectWizardStep}

/** Project wizard for new Scaleda project
  *
  * Registered as `newProjectWizard.language`
  */
class ScaledaNewProjectWizard extends LanguageNewProjectWizard {
  override def isEnabled(wizardContext: WizardContext): Boolean = true

  override def getName: String = ScaledaLanguageText

  override def createStep(
      p: NewProjectWizardLanguageStep
  ): NewProjectWizardStep = new ScaledaNewProjectWizardStep(p)
}

object ScaledaNewProjectWizard {
  val ScaledaLanguageText: String = "Scaleda"
}
