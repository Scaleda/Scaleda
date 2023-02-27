package top.criwits.scaleda
package idea

import idea.settings.toolchains.ProfileDetectAction
import idea.utils.{Icons, MainLogger, inReadAction}
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig
import kernel.template.Template
import kernel.toolchain.Toolchain
import kernel.utils.KernelLogger

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
import top.criwits.scaleda.idea.runner.task.ScaledaReloadTasksAction
import top.criwits.scaleda.kernel.bin.ExtractBinaryFiles

class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    MainLogger.info("This is Scaleda, an EDA tool for FPGAs based on IntelliJ platform")
    // copy kernel logs to main logger
    KernelLogger.append(MainLogger)
    // init jinjia
    Template.initJinja()
//
//    // check is having project config
//    var searchedFile: Option[VirtualFile] = None
//    inReadAction {
//      ProjectFileIndex
//        .getInstance(project)
//        .iterateContent(fileOrDir => {
//          if (
//            !fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile
//          ) {
//            searchedFile = Some(fileOrDir)
//          }
//          true
//        })
//    }
//
//    if (searchedFile.isEmpty) {
//      MainLogger.warn("No available Scaleda config (scaleda.yml) found under this project. This is not a Scaleda project")
//    } else {
//      val f = searchedFile.get
//      ProjectConfig.configFile = Some(f.getPath)
//      ProjectConfig.projectBase = Some(f.getParent.getPath)
//      MainLogger.info(s"Scaleda config file $f detected")
//    }

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
      ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)
    })

    // if no profiles loaded, detect all profiles and popup message
    if (Toolchain.profiles().isEmpty) {
      ActionManager.getInstance()
        .tryToExecute(new ProfileDetectAction(project), null, null, null, true)
    }

    // install binaries
    ExtractBinaryFiles.run()
  }
}
