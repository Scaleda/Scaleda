package top.criwits.scaleda
package idea.runner.configuration

import idea.runner.ScaledaRunProcessHandler
import idea.windows.tool.ScaledaToolWindowFactory
import kernel.utils.KernelLogger

import com.intellij.execution.configurations.{
  LocatableConfigurationBase,
  RunConfiguration,
  RunProfileState
}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes

import scala.collection.mutable

class ScaledaRunConfiguration(
    project: Project,
    factory: ScaledaRunConfigurationFactory,
    name: String
) extends LocatableConfigurationBase[RunProfileState](project, factory, name) {

  var targetName = ""
  var taskName = ""
  val extraEnvs = new mutable.HashMap[String, String]

  override def getConfigurationEditor: SettingsEditor[_ <: RunConfiguration] =
    new ScaledaRunConfigurationEditor(project)

  override def getState(
      executor: Executor,
      environment: ExecutionEnvironment
  ): RunProfileState = {
    // new CommandLineState(environment) {
    //   override def startProcess() = {
    //     val cmd = new GeneralCommandLine(
    //       "/opt/Xilinx/Vivado/2019.2/bin/vivado",
    //       "-help"
    //     )
    //     val processHandler = new ColoredProcessHandler(cmd)
    //     processHandler.setShouldKillProcessSoftly(true)
    //     ProcessTerminatedListener.attach(processHandler)
    //     processHandler
    //   }
    // }
    KernelLogger.warn("starting")
    val searchScope =
      ExecutionSearchScopes.executionScope(project, environment.getRunProfile)
    val myConsoleBuilder =
      TextConsoleBuilderFactory.getInstance.createBuilder(project, searchScope)
    val console = myConsoleBuilder.getConsole

    (executor: Executor, runner: ProgramRunner[_]) => {
      new ExecutionResult() {
        override def getExecutionConsole: ExecutionConsole =
          // ScaledaToolWindowFactory.outputPanel(project).consoleView
          console

        override def getActions: Array[AnAction] = Array()

        override def getProcessHandler: ProcessHandler =
          new ScaledaRunProcessHandler(project, console)
      }
    }
  }
}
