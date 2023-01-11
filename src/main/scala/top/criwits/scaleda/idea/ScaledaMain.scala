package top.criwits.scaleda
package idea

import idea.utils.{Icons, MainLogger, Notification, OutputLogger}
import idea.windows.tasks.ScaledaRunWindowFactory
import idea.windows.tool.ScaledaToolWindowFactory
import kernel.project.config.ProjectConfig
import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}

class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    // setup main logger
    try {
      MainLogger.consoleView = Some(
        ScaledaToolWindowFactory.logPanel(project).consoleView
      )
    } catch {
      case e: Throwable => MainLogger.error(e.getMessage)
    }
    // setup output logger
    try {
      OutputLogger.consoleView = Some(
        ScaledaToolWindowFactory.outputPanel(project).consoleView
      )
    } catch {
      case e: Throwable => MainLogger.error(e.getMessage)
    }
    // setup notification logger
    Notification.setProject(project)
    // copy kernel logger to main logger
    KernelLogger.append(MainLogger)
    // init jinjia
    Template.initJinja()
    // check is having project config
    var searchedFile: Option[VirtualFile] = None
    ProjectFileIndex
      .getInstance(project)
      .iterateContent(fileOrDir => {
        if (
          !fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile
        ) {
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
      val builder =
        new RegisterToolWindowTaskBuilder(ScaledaRunWindowFactory.WINDOW_ID)
      builder.icon = Icons.mainSmall
      builder.contentFactory = new ScaledaRunWindowFactory
      builder.anchor = ToolWindowAnchor.RIGHT
      // builder.stripeTitle = ScaledaBundle.message("tasks.tool.window.title")
      val toolWindow = toolWindowManager.registerToolWindow(builder.build())
    })

    MainLogger.info("Scaleda launched.")
  }
}
