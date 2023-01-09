package top.criwits.scaleda
package idea.runner.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.{CommandLineState, GeneralCommandLine, LocatableConfigurationBase, RunConfiguration, RunProfileState}
import com.intellij.execution.process.{ColoredProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

import scala.collection.mutable

class ScaledaRunConfiguration(project: Project, factory: ScaledaRunConfigurationFactory, name: String)
  extends LocatableConfigurationBase[RunProfileState](project, factory, name) {

  var targetName = ""
  var taskName = ""
  val extraEnvs = new mutable.HashMap[String, String]

  override def getConfigurationEditor: SettingsEditor[_ <: RunConfiguration] = new ScaledaRunConfigurationEditor(project)

  override def getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState = {
    new CommandLineState(environment) {
      override def startProcess() = {
        val cmd = new GeneralCommandLine("echo hi")
        val processHandler = new ColoredProcessHandler(cmd)
        processHandler.setShouldKillProcessSoftly(true)
        ProcessTerminatedListener.attach(processHandler)
        processHandler
      }
    }
  }
}
