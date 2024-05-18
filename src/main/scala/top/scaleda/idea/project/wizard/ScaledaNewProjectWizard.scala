// package top.scaleda
// package idea.project.wizard
//
// import idea.utils.Icons
//
// import com.intellij.ide.util.projectWizard.WizardContext
// import com.intellij.ide.wizard.{GeneratorNewProjectWizard, NewProjectWizardStep, RootNewProjectWizardStep}
// import com.intellij.ide.wizard.language.LanguageGeneratorNewProjectWizard
//
// import javax.swing.Icon
//
// /** Project wizard for new Scaleda project
//   *
//   * Registered as `newProjectWizard.languageGenerator`
//   */
// class ScaledaNewProjectWizard extends GeneratorNewProjectWizard {
//   // override def isEnabled(wizardContext: WizardContext): Boolean = true
//
//   override def getName: String = "Scaleda"
//
//   // override def createStep(
//   //     p: NewProjectWizardLanguageStep
//   // ): NewProjectWizardStep = new ScaledaNewProjectWizardStep(p)
//
//   override def getIcon: Icon = Icons.project
//
//   // override def createStep(p: NewProjectWizardStep): NewProjectWizardStep =
//   //   new ScaledaNewProjectWizardStep(p)
//
//   override def getId: String = "Scaleda"
//
//   override def createStep(wizardContext: WizardContext): NewProjectWizardStep =
//     new RootNewProjectWizardStep(wizardContext)
// }
