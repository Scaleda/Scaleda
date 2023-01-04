package top.criwits.scaleda
package idea

import idea.utils.MainLogger
import kernel.project.config.ProjectConfig

import com.intellij.openapi.project.{DumbAware, Project, ProjectManager}
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import top.criwits.scaleda.idea.windows.ScaledaTargetWindowFactory
import top.criwits.scaleda.kernel.toolchain.Toolchain

import java.io.File

class ScaledaMain extends /* DumbAware with */ StartupActivity {
  override def runActivity(project: Project): Unit = {
    // check is having project config
    var searchedFile: Option[VirtualFile] = None
    ProjectFileIndex.getInstance(project).iterateContent(fileOrDir => {
      if (!fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile) {
        searchedFile = Some(fileOrDir)
      }
      true
    })
    if (searchedFile.isEmpty) {
      MainLogger.warn("there's no project config")
    } else {
      val f = searchedFile.get
      ProjectConfig.configFile = Some(f.getPath)
      MainLogger.info(s"found config: ${f}")
    }
    MainLogger.info("Scaleda launched.")
  }
}

object ScaledaMain {
}
