package top.scaleda
package idea.runner.configuration

import idea.runner.ScaledaRunProcessHandler
import idea.toolchain.ToolchainIdea
import idea.utils.Notification
import idea.utils.notification.CreateTypicalNotification
import idea.windows.bottomPanel.message.MessageListService
import kernel.database.UserException
import kernel.shell.ScaledaRun
import kernel.toolchain.runner.ScaledaRuntime

import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.{ProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

import java.util.concurrent.TimeoutException

class ScaledaRunProfileState(
    project: Project,
    runtime: ScaledaRuntime,
    invokeAfterFinish: (ScaledaRuntime, Seq[Int], Boolean, Boolean) => Unit
) extends RunProfileState {
  override def execute(executor: Executor, runner: ProgramRunner[_]): ExecutionResult = {
    val handler =
      new ScaledaRunProcessHandler(
        project,
        runtime,
        invokeAfterFinish
      )

    ProcessTerminatedListener.attach(handler, project)

    // prepare process
    val thread = ScaledaRun.runTaskBackground(handler, runtime)


    // message parser
    val consoleService = project.getService(classOf[MessageListService])
    ToolchainIdea.toolchainMessageParser
      .get(runtime.target.toolchain)
      .foreach(parserProvider => consoleService.registerParser(parserProvider.messageParser))
    consoleService.registerRuntime(runtime)

    // setup exception handler
    thread.setUncaughtExceptionHandler((_thread: Thread, throwable: Throwable) => {
      throwable match {
        case e: UserException =>
          CreateTypicalNotification.makeAuthorizationNotification(project, e.getMessage, NotificationType.ERROR)
        case e: TimeoutException =>
          Notification(project).error("Timeout", e.getMessage, ", check your connections")
        case e: Throwable =>
          e.printStackTrace()
          throw e
      }
      handler.destroyProcess()
    })

    // run process in the background
    thread.start()

    // return result
    new ExecutionResult {
      override def getExecutionConsole: ExecutionConsole = null

      override def getActions: Array[AnAction] = Array()

      override def getProcessHandler: ProcessHandler = handler
    }
  }
}
