package top.criwits.scaleda
package idea

import idea.runner.task.ScaledaReloadTasksAction
import idea.rvcd.RvcdService
import idea.settings.toolchains.ProfileDetectAction
import idea.utils.{AssetsInstallAction, Icons, MainLogger, RpcService}
import idea.windows.tasks.ScaledaRunWindowFactory
import idea.windows.tool.ScaledaToolWindowFactory
import idea.windows.tool.logging.ScaledaLoggingService
import idea.windows.tool.message.ScaledaMessageTab
import kernel.bin.ExtractAssets
import kernel.toolchain.Toolchain
import kernel.toolchain.impl._
import kernel.utils.{EnvironmentUtils, KernelLogger}

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}

/** This is the startup activity of Scaleda. It will:
  *  - Initialise logger, jinja and other kernel components;
  *  - Initialise windows (tasks and messages);
  *  - Check if latest assets are installed;
  *  - Initialise remote service;
  */
class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    MainLogger.info("This is Scaleda, an EDA tool for FPGAs based on IntelliJ platform")

    // invoke Env backup
    val _ = EnvironmentUtils.Backup

    // Logging service, handling IDEA log output
    project.getService(classOf[ScaledaLoggingService])
    // Main service, init jinja and kernel log
    project.getService(classOf[ScaledaMainService])

    // setup windows
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

    toolWindowManager.invokeLater(() => {
      val builder =
        new RegisterToolWindowTaskBuilder(ScaledaToolWindowFactory.WINDOW_ID)
      builder.icon = Icons.mainSmall
      builder.contentFactory = new ScaledaToolWindowFactory
      builder.anchor = ToolWindowAnchor.BOTTOM
      builder.shouldBeAvailable = true

      val messageWindow = toolWindowManager.registerToolWindow(builder.build())
    })

    // attempt to load project
    ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)

    // if no profiles loaded, detect all profiles and popup message
    if (Toolchain.profiles().isEmpty) {
      ActionManager
        .getInstance()
        .tryToExecute(new ProfileDetectAction(project), null, null, null, true)
    }

    // check binaries
    if (!ExtractAssets.isInstalled) {
      ActionManager
        .getInstance()
        .tryToExecute(new AssetsInstallAction(project), null, null, null, true)
    }

    // Remote
    val rpcService = project.getService(classOf[RpcService])
    rpcService.setProject(project)
    project.getService(classOf[RvcdService]).setProject(project)

    // invoke MessageTab instance
    val _ = ScaledaMessageTab(project)

    // invoke all toolchains
    Seq(IVerilog, Vivado, PDS, Quartus, Verilator).foreach(KernelLogger.info("Load Toolchain object", _))
  }
}
