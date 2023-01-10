package top.criwits.scaleda
package idea

import idea.utils.{Icons, MainLogger, OutputLogger}
import idea.windows.tasks.ScaledaRunWindowFactory
import idea.windows.tool.ScaledaToolWindowFactory
import kernel.project.config.ProjectConfig
import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{RegisterToolWindowTask, ToolWindowAnchor, ToolWindowManager}

class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    // setup main logger
    try {
      MainLogger.consoleView = Some(ScaledaToolWindowFactory.logPanel(project).consoleView)
    } catch {
      case e: Throwable => MainLogger.error(e.getMessage)
    }
    // setup output logger
    try {
      OutputLogger.consoleView = Some(ScaledaToolWindowFactory.outputPanel(project).consoleView)
    } catch {
      case e: Throwable => MainLogger.error(e.getMessage)
    }
    // copy kernel logger to main logger
    KernelLogger.append(MainLogger)
    // init jinjia
    Template.initJinja()
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
      ProjectConfig.projectBase = Some(f.getParent.getPath)
      MainLogger.info(s"found config: ${f}")
    }

    // setup tool window
    val toolWindowManager = ToolWindowManager.getInstance(project)
    toolWindowManager.invokeLater(() => {
      val toolWindow = toolWindowManager.registerToolWindow(RegisterToolWindowTask.notClosable(
        ScaledaRunWindowFactory.WINDOW_ID,
        Icons.mainSmall,
        ToolWindowAnchor.RIGHT
      ))
    })

    MainLogger.info("Scaleda launched.")
  }
}
