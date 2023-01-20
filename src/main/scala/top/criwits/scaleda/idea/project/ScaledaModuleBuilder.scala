package top.criwits.scaleda
package idea.project

import idea.project.ScaledaModuleBuilder.{GROUP_NAME, ICON, createProjectStructure}
import idea.utils.{Icons, MainLogger, invokeLater}

import com.intellij.ide.util.EditorHelper
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module
import com.intellij.openapi.module.{ModifiableModuleModel, ModuleType}
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.{FileUtil, FileUtilRt}
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFileManager}
import com.intellij.psi.PsiManager

import java.io.File
import javax.swing.Icon

/** Module builder for [[ScaledaModuleType]]
  */
class ScaledaModuleBuilder extends ModuleBuilder {
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
      createProjectStructure(contentDir, getName, "rtl", "src")

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

  override def createModule(moduleModel: ModifiableModuleModel): module.Module = {
    setModuleFilePath({
      val file = new File(getModuleFilePath)
      FileUtilRt.toSystemIndependentName(file.getParent) + "/.idea/idea-module/" + file.getName
    })
    super.createModule(moduleModel)
  }

//  override def getBuilderId: String = getClass.getName


//  override def commitModule(project: Project, model: ModifiableModuleModel): module.Module = {
//    val module = super.commitModule(project, model)
//    module
//  }
}

object ScaledaModuleBuilder {
  val GROUP_NAME: String = "Scaleda"
  val ICON: Icon         = Icons.mainSmall

  /** Create project with initalising it's basic structure and file
    * @param root
    * @param projectName
    * @param projectType
    * @param sourceRoot
    */
  def createProjectStructure(root: File, projectName: String, projectType: String, sourceRoot: String): Unit = {
    MainLogger.warn(
      s"Generate new Scaleda project structure, project name: $projectName, project root: ${root.getAbsolutePath}"
    )
    val configFile = new File(root, "scaleda.yml")
    val sourceDir  = new File(root, sourceRoot)

    if (configFile.createNewFile() && sourceDir.mkdir()) {
      // write scaleda.yml
      val configContent =
        s"""---
          |name: $projectName
          |type: $projectType
          |source: $sourceRoot
          |""".stripMargin
      FileUtil.writeToFile(configFile, configContent)
    } else {
      MainLogger.error(s"Cannot generate new Scaleda project $projectName")
    }
  }
}
