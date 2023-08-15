//package top.scaleda
//package idea
//
//import idea.runner.task.ScaledaReloadTasksAction
//import idea.rvcd.RvcdService
//import idea.settings.toolchains.ProfileDetectAction
//import idea.toolchain.{IVerilogIdea, VivadoIdea}
//import idea.utils.{AssetsInstallAction, Icons, IdeaLogger, RpcService, runInEdt}
//import idea.windows.tasks.ScaledaRunWindowFactory
//import idea.windows.tool.ScaledaToolWindowFactory
//import idea.windows.tool.logging.ScaledaLoggingService
//import idea.windows.tool.message.ScaledaMessageTab
//import kernel.bin.ExtractAssets
//import kernel.toolchain.Toolchain
//import kernel.toolchain.impl._
//import kernel.utils.{EnvironmentUtils, KernelLogger}
//
//import com.intellij.openapi.actionSystem.ActionManager
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.startup.ProjectActivity
//import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
//import kotlin.coroutines.Continuation
//
///** This is the startup activity of Scaleda. It will:
//  *  - Initialise logger, jinja and other kernel components;
//  *  - Initialise windows (tasks and messages);
//  *  - Check if latest assets are installed;
//  *  - Initialise remote service;
//  */
//class ScaledaMain extends ProjectActivity {
//  override def execute(project: Project, continuation: Continuation[_ >: kotlin.Unit]): AnyRef = {
//    IdeaLogger.info("This is Scaleda")
//
//    // invoke Env backup
//    val _ = EnvironmentUtils.Backup
//
//    // Logging service, handling IDEA log output
//    project.getService(classOf[ScaledaLoggingService])
//
//    // Main service, init jinja and kernel log
//    val mainService = project.getService(classOf[ScaledaAppService])
//    mainService.initToolWindows
//
//
//    val rpcService = project.getService(classOf[RpcService])
//    rpcService.setProject(project)
//    runInEdt {
//      // Remote and RPC
//      ActionManager
//        .getInstance()
//        .tryToExecute(
//          e => {
//            val _thread = RpcService.startRpcGotoHandler(project)
//          },
//          null,
//          null,
//          null,
//          true
//        )
//    }
//    // try {
//      project.getService(classOf[RvcdService]).setProject(project)
//    // } catch {
//    //   case e: Exception =>
//    //     MainLogger.error("Failed to start RVCD service", "grpc version is",
//    //       io.grpc.internal.GrpcUtil.getGrpcBuildVersion.toString,
//    //       // io.grpc.netty.shaded.io.netty.util.Version.identify().toString,
//    //       e)
//    //     e.printStackTrace()
//    // }
//
//    // invoke MessageTab instance
//    val _ = ScaledaMessageTab(project)
//
//    // invoke all toolchains
//    Seq(IVerilog, Vivado).foreach(KernelLogger.info("Load Toolchain object", _))
//    Seq(VivadoIdea, IVerilogIdea).foreach(KernelLogger.info("Load Toolchain Idea object", _))
//
//    runInEdt {
//      // Attempt to load project
//      ActionManager.getInstance().tryToExecute(new ScaledaReloadTasksAction, null, null, null, true)
//    }
//
//    // Toolchain detect
//    if (Toolchain.profiles().isEmpty) {
//      ActionManager
//        .getInstance()
//        .tryToExecute(new ProfileDetectAction(project), null, null, null, true)
//    }
//    AnyRef
//  }
//}
