package top.criwits.scaleda
package project.wizard

import com.intellij.ide.util.projectWizard.{ModuleBuilder, ModuleWizardStep, WizardContext}
import com.intellij.openapi.module.{EmptyModuleType, ModuleType}
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider

import javax.swing.{JComponent, JLabel}

class RTLModuleWizardStep extends ModuleBuilder {
  override def setupRootModel(modifiableRootModel: ModifiableRootModel): Unit = {}
  def getModuleType: ModuleType[_] = EmptyModuleType.getInstance()

  override def createWizardSteps(wizardContext: WizardContext, modulesProvider: ModulesProvider): Array[ModuleWizardStep] =
    Array(new ModuleWizardStep {
      override def updateDataModel(): Unit = {}

      override def getComponent: JComponent = new JLabel("Test");
    })
}
