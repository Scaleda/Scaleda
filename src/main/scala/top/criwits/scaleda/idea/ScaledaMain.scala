package top.criwits.scaleda
package idea

import idea.settings.toolchains.ProfileDetectAction
import idea.utils.{Icons, MainLogger, RpcService, inReadAction}
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig
import kernel.template.Template
import kernel.toolchain.Toolchain
import kernel.utils.KernelLogger

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.CoreProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
import top.criwits.scaleda.idea.runner.task.ScaledaReloadTasksAction
import top.criwits.scaleda.idea.windows.tool.logging.ScaledaLoggingService
import top.criwits.scaleda.kernel.bin.ExtractBinaryFiles
import top.criwits.scaleda.idea.rvcd.RvcdService

class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    MainLogger.info("This is Scaleda, an EDA tool for FPGAs based on IntelliJ platform")

    // Logging service
    project.getService(classOf[ScaledaLoggingService])

    project.getService(classOf[ScaledaMainService])

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

    // check binaries
    if (!ExtractBinaryFiles.isInstalled) {
      ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable {
        override def run(): Unit = ExtractBinaryFiles.run()
      }, ScaledaBundle.message("utils.installing.assets"), false, project)
    }

    // Remote
    val rpcService = project.getService(classOf[RpcService])
    rpcService.setProject(project)
    project.getService(classOf[RvcdService]).setProject(project)
  }
}
