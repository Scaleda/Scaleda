package top.criwits.scaleda

import project.config.ProjectConfigLoader
import utils.MainLogger

import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile

class ScaledaMain extends DumbAware with StartupActivity {
  override def runActivity(project: Project) = {
    MainLogger.Log.warn("Hi there, it's scaleda! Launching...")
    // check is having project config
    var searchedFile: Option[VirtualFile] = None
    ProjectFileIndex.getInstance(project).iterateContent(fileOrDir => {
      if (!fileOrDir.isDirectory && fileOrDir.getName == ProjectConfigLoader.defaultConfigFile) {
        searchedFile = Some(fileOrDir)
      }
      true
    })
    if (searchedFile.isEmpty) {
      MainLogger.Log.warn("there's no project config")
    } else {
      val f = searchedFile.get
      MainLogger.Log.warn(s"found config: ${f}")
    }
    MainLogger.Log.warn("Scaleda launched.")
  }
}
