package top.scaleda
package idea

import idea.rvcd.RvcdService
import idea.settings.toolchains.ProfileDetectAction
import idea.utils.{AssetsInstallAction, RpcService, ScaledaIdeaLogger, runInEdt}
import idea.windows.bottomPanel.ConsoleService
import idea.windows.rightPanel.ScaledaReloadTasksAction
import kernel.bin.ExtractAssets
import kernel.toolchain.Toolchain
import kernel.utils.EnvironmentUtils

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlin.coroutines.Continuation
import top.scaleda.idea.runner.configuration.ScaledaRunConfiguration

/** This is the startup activity of Scaleda. It will:
  *  - Initialise logger, jinja and other kernel components;
  *  - Initialise windows (tasks and messages);
  *  - Check if latest assets are installed;
  *  - Initialise remote service;
  */
class ScaledaMain extends ProjectActivity {
  override def execute(project: Project, continuation: Continuation[_ >: kotlin.Unit]): AnyRef = {
    ScaledaIdeaLogger.info("This is Scaleda, an EDA tool for FPGAs based on IntelliJ platform")

    // invoke Env backup
    val _ = EnvironmentUtils.Backup
    // when tool window not loaded, global logger not available...
    // runInEdt {
    //   ToolWindowManager
    //     .getInstance(project)
    //     .getToolWindow("Scaleda")
    //     .activate(() => {
    //       ScaledaIdeaLogger.debug("Scaleda Messages activated")
    //     })
    // }
    // register ScaledaIdeaLogger to console service
    ScaledaIdeaLogger.setConsoleService(project.getService(classOf[ConsoleService]))

    // Check assets
    if (!ExtractAssets.isInstalled) {
      try {
        ActionManager
          .getInstance()
          .tryToExecute(new AssetsInstallAction(project), null, null, null, true)
      } catch {
        case e: Exception =>
          ScaledaIdeaLogger.warn("Failed to install assets, retry in main thread", e)
          try {
            ExtractAssets.run()
          } catch {
            case e: Exception =>
              ScaledaIdeaLogger.error("Failed to install assets", e)
          }
      }
    }

    project.getService(classOf[RvcdService]).setProject(project)

    val rpcService = project.getService(classOf[RpcService])
    rpcService.setProject(project)
    runInEdt {
      // Remote and RPC
      ActionManager
        .getInstance()
        .tryToExecute(
          e => {
            val _thread = RpcService.startRpcGotoHandler(project)
          },
          null,
          null,
          null,
          true
        )
    }
    // Attempt to load project
    runInEdt {
      ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, false)
    }

    // Toolchain detect
    if (Toolchain.profiles().isEmpty) {
      runInEdt {
        ActionManager
          .getInstance()
          .tryToExecute(new ProfileDetectAction(project), null, null, null, false)
      }
    }

    // register RunConfigurationChangeListener
    ScaledaRunConfiguration.registerConfigurationChangeListener(project)
    AnyRef
  }
}
