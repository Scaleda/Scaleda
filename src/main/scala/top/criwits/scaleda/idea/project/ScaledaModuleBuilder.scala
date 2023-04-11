package top.criwits.scaleda
package idea.project

import idea.project.ScaledaModuleBuilder.{GROUP_NAME, ICON}
import idea.utils.{Icons, invokeLater}
import kernel.init.InitHelper.createProjectStructure

import com.intellij.ide.util.EditorHelper
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module
import com.intellij.openapi.module.{ModifiableModuleModel, ModuleType}
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFileManager}
import com.intellij.psi.PsiManager

import java.io.File
import javax.swing.Icon

/** Module builder for [[ScaledaModuleType]]
  */
class ScaledaModuleBuilder(val srcRoot: String, val testRoot: String) extends ModuleBuilder {
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
        EditorHelper.openInEditor(psiFile)
      }
    }
  }

  /**
   * We override [[createModule]] function to move IJ module file into `.idea`, which is usually ignored.
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

}

object ScaledaModuleBuilder {
  val GROUP_NAME: String = "Scaleda"
  val ICON: Icon         = Icons.mainSmall

}
