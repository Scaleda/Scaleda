package top.criwits.scaleda
package idea.runner.configuration

import idea.runner.ScaledaRunProcessHandler
import idea.utils.{ConsoleLogger, MainLogger}
import kernel.project.config.ProjectConfig
import kernel.shell.ScaledaRun

import com.intellij.execution.configurations.{LocatableConfigurationBase, RunConfiguration, RunProfileState}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes
import org.jdom.Element
import top.criwits.scaleda.idea.windows.tool.message.{ScaledaMessage, ScaledaMessageTab}

import java.io.File
import scala.collection.mutable

class ScaledaRunConfiguration(
    project: Project,
    factory: ScaledaRunConfigurationFactory,
    name: String
) extends LocatableConfigurationBase[RunProfileState](project, factory, name) {

  var targetName = ""
  var taskName = ""
  val extraEnvs = new mutable.HashMap[String, String]

  private val STORAGE_ID: String = "scaleda"

  override def writeExternal(element: Element) = {
    super.writeExternal(element)
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      child.setAttribute("taskName", taskName)
    } else {
      val c = new Element(STORAGE_ID)
      c.setAttribute("taskName", taskName)
      element.addContent(c)
    }
  }

  override def readExternal(element: Element) = {
    super.readExternal(element)
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      ProjectConfig.getConfig().foreach(c => {
        val t = child.getAttributeValue("taskName")
        c.taskByName(t).foreach(f => {
          targetName = f._1.name
          taskName = f._2.name
        })
      })
    }
  }

  override def getConfigurationEditor: SettingsEditor[_ <: RunConfiguration] =
    new ScaledaRunConfigurationEditor(project)

  override def getState(
      executor: Executor,
      environment: ExecutionEnvironment
  ): RunProfileState = {
    MainLogger.warn(s"executing: taskName=$taskName, targetName=$targetName")
    ProjectConfig
      .getConfig()
      .flatMap(c => {
        c.taskByName(taskName)
          .map(f => {
            val (target, task) = f
            val searchScope =
              ExecutionSearchScopes
                .executionScope(project, environment.getRunProfile)
            val myConsoleBuilder =
              TextConsoleBuilderFactory.getInstance
                .createBuilder(project, searchScope)
            val console = myConsoleBuilder.getConsole

            val handler =
              new ScaledaRunProcessHandler(
                new ConsoleLogger(console,
                  logSourceId = Some(s"${ScaledaMessageTab.MESSAGE_ID}-${target.toolchain}")))
            val state = new RunProfileState {
              override def execute(
                  executor: Executor,
                  runner: ProgramRunner[_]
              ) = {
                new ExecutionResult {
                  override def getExecutionConsole: ExecutionConsole = console

                  override def getActions: Array[AnAction] = Array()

                  override def getProcessHandler: ProcessHandler = handler
                }
              }
            }
            ScaledaRun.runTaskBackground(
              handler,
              new File(ProjectConfig.projectBase.get),
              target,
              task
            )
            state
          })
      })
      .getOrElse({
        MainLogger.warn(s"Cannot find task name: $taskName")
        null
      })
  }
}
