package top.scaleda
package idea.project

import idea.project.ScaledaModuleBuilderScala.{GROUP_NAME, ICON}
import idea.utils.{Icons, invokeLater}
import kernel.init.InitHelper.createProjectStructure

import com.intellij.ide.util.EditorHelper
import com.intellij.ide.util.projectWizard.{ModuleBuilder, ModuleWizardStep, WizardContext}
import com.intellij.openapi.module
import com.intellij.openapi.module.{ModifiableModuleModel, ModuleType}
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFileManager}
import com.intellij.psi.PsiManager
import com.intellij.ui.components.{JBLabel, JBTextField}
import com.intellij.util.ui.FormBuilder
import top.scaleda.idea.ScaledaBundle

import java.io.File
import javax.swing.{Icon, JComponent, JPanel}

/** Module builder for [[ScaledaModuleType]]
  */
class ScaledaModuleBuilderScala extends ModuleBuilder {
  private var srcRoot: String  = "src"
  private var testRoot: String = "test"

  def setSrcRoot(srcRoot: String): this.type = {
    this.srcRoot = srcRoot
    this
  }
  def setTestRoot(testRoot: String): this.type = {
    this.testRoot = testRoot
    this
  }

  override def getModuleType: ModuleType[_] = ScaledaModuleType.getInstance
//  override def getModuleType: ModuleType[_] = ModuleType.EMPTY // why?

  override def getNodeIcon: Icon = ICON

  override def getGroupName: String = GROUP_NAME

  override def setupRootModel(modifiableRootModel: ModifiableRootModel): Unit = {
    for {
      contentPath <- Option(getContentEntryPath)
      contentDir = new File(contentPath)
      if FileUtilRt.createDirectory(contentDir)
    } {
      // generate project structure
      createProjectStructure(contentDir, getName, "rtl", srcRoot, testRoot)

      // register module
      val entry =
        modifiableRootModel.addContentEntry(LocalFileSystem.getInstance().refreshAndFindFileByIoFile(contentDir))
//      entry.addSourceFolder("src", false)
      invokeLater {
        openEditorForCodeSampleOrBuildFile(modifiableRootModel.getProject, contentDir)
      }
    }
  }

  private def openEditorForCodeSampleOrBuildFile(project: Project, contentDir: File): Unit = {
    val fileToOpen = Option(
      VirtualFileManager.getInstance().findFileByNioPath(new File(contentDir, "scaleda.yml").toPath)
    )

    fileToOpen.foreach { vFile =>
      val psiManager = PsiManager.getInstance(project)
      val psiFile    = psiManager.findFile(vFile)
      if (psiFile != null) {
        // when project created but not opened, `EditorHelper.openInEditor' fails
        try {
          EditorHelper.openInEditor(psiFile)
        } catch {
          case _: kotlin.UninitializedPropertyAccessException =>
        }
      }
    }
  }

  /** We override [[createModule]] function to move IJ module file into `.idea`, which is usually ignored.
    * @param moduleModel Current module model
    * @return Model
    */
  override def createModule(moduleModel: ModifiableModuleModel): module.Module = {
    setModuleFilePath({
      val file = new File(getModuleFilePath)
      FileUtilRt.toSystemIndependentName(file.getParent) + "/.idea/scaleda-module/" + file.getName
    })
    super.createModule(moduleModel)
  }

  override def createWizardSteps(
      wizardContext: WizardContext,
      modulesProvider: ModulesProvider
  ): Array[ModuleWizardStep] = Array(
    new ModuleWizardStep {
      val sourceDirectory = new JBTextField(srcRoot)
      val testDirectory   = new JBTextField(testRoot)
      private val panel = FormBuilder
        .createFormBuilder()
        .addLabeledComponent(new JBLabel(ScaledaBundle.message("module.source.directory")), sourceDirectory)
        .addLabeledComponent(new JBLabel(ScaledaBundle.message("module.test.directory")), testDirectory)
        .addComponentFillVertically(new JPanel, 0)
        .getPanel

      override def getComponent: JComponent = panel

      override def updateDataModel(): Unit = {
        setSrcRoot(sourceDirectory.getText)
        setTestRoot(testDirectory.getText)
      }
    }
  )

}

object ScaledaModuleBuilderScala {
  val GROUP_NAME: String = "Scaleda"
  val ICON: Icon         = Icons.mainSmall

}
