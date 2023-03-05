package top.criwits.scaleda
package idea.runner.configuration

import idea.runner.ScaledaRunProcessHandler
import idea.utils.{ConsoleLogger, MainLogger, Notification}
import idea.windows.tool.message.ScaledaMessageTab
import kernel.net.{RemoteClient, RemoteServer}
import kernel.net.remote.{Empty, RemoteGrpc, RemoteProfile}
import kernel.project.config.{ProjectConfig, TaskConfig, TaskType}
import kernel.shell.ScaledaRun
import kernel.toolchain.Toolchain

import top.criwits.scaleda.kernel.toolchain.executor.{SimulationExecutor, Executor => SExecutor}
import com.intellij.execution.configurations.{LocatableConfigurationBase, RunConfiguration, RunProfileState}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.{ProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.openapi.actionSystem.{ActionManager, AnAction}
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes
import io.grpc.ManagedChannelBuilder
import org.jdom.Element
import top.criwits.scaleda.idea.rvcd.{RvcdLaunchAction, RvcdService}

import java.io.File
import scala.collection.mutable
import scala.language.existentials

/**
 * A configuration representing a specific Scaleda task as well as corresponding toolchain
 * @param project
 * @param factory
 * @param name
 */
class ScaledaRunConfiguration(
    project: Project,
    factory: ScaledaRunConfigurationFactory,
    name: String
) extends LocatableConfigurationBase[RunProfileState](project, factory, name) {

  var targetName = ""
  var taskName = ""
  // TODO: Add toolchain profile
  val extraEnvs = new mutable.HashMap[String, String]

  private val STORAGE_ID: String = "scaleda"

  override def writeExternal(element: Element): Unit = {
    super.writeExternal(element)
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      child.setAttribute("taskName", taskName)
      child.setAttribute("targetName", targetName)
    } else {
      val c = new Element(STORAGE_ID)
      c.setAttribute("taskName", taskName)
      c.setAttribute("targetName", targetName)
      element.addContent(c)
    }
  }

  // FIXME: configuration cannot load from storage
  override def readExternal(element: Element): Unit = {
    super.readExternal(element)
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      ProjectConfig
        .getConfig()
        .foreach(c => {
          val t = child.getAttributeValue("taskName")
          val r = child.getAttributeValue("targetName")
          c.taskByName(t, r)
            .foreach(f => {
              targetName = f._1.name
              taskName = f._2.name
            })
        })
    }
  }

  override def getConfigurationEditor: SettingsEditor[_ <: RunConfiguration] =
    new ScaledaRunConfigurationEditor(project)

  /**
   * Returns a [[RunProfileState]], which is actually used to run
   * @param executor
   * @param environment
   * @return
   */
  override def getState(
      executor: Executor,
      environment: ExecutionEnvironment
  ): RunProfileState = {
    MainLogger.info(s"getState: taskName=$taskName, targetName=$targetName")
    ProjectConfig
      .getConfig()
      .flatMap(c => {
        c.taskByName(taskName, targetName)
          .map(f => {
            val (target, task) = f
            var remoteProfiles: Option[Seq[RemoteProfile]] = None
            val hasProfile =
              if (task.host.isEmpty) {
                // Run locally if no host argument provided
                Toolchain
                  .profiles()
                  .exists(_.toolchainType == target.toolchain)
              } else {
                // TODO: add proxy
                val stub = RemoteClient(task.host.get, RemoteServer.port)
                remoteProfiles = Some(stub.getProfiles(Empty()).profiles)
                remoteProfiles.get.exists(_.toolchainType == target.toolchain)
              }
            if (!hasProfile) {
              Notification(project).error(
                "Toolchain not found",
                s"Cannot find toolchain ${target.toolchain}, check your profile list"
              )
              null
            } else {
              val searchScope =
                ExecutionSearchScopes
                  .executionScope(project, environment.getRunProfile)

              val myConsoleBuilder =
                TextConsoleBuilderFactory.getInstance
                  .createBuilder(project, searchScope)

              val console = myConsoleBuilder.getConsole

              def startRvcdAfterExecution(task: TaskConfig, executor: SExecutor): Unit = {
                task.taskType match {
                  // Only call rvcd when simulate
                  case TaskType.Simulation =>
                    project.getService(classOf[RvcdService]).launchWithWaveformAndSource(executor.asInstanceOf[SimulationExecutor].vcdFile, Seq.empty)
                    // TODO / FIXME: Source is not given
                  case _ =>
                }
              }

              val handler =
                new ScaledaRunProcessHandler(
                  new ConsoleLogger(
                    console,
                    logSourceId = Some(
                      s"${ScaledaMessageTab.MESSAGE_ID}-${target.toolchain}"
                    )
                  ), task, startRvcdAfterExecution
                )


              val state = new RunProfileState {
                override def execute(
                    executor: Executor,
                    runner: ProgramRunner[_]
                ): ExecutionResult = {
                  // attach listener to control the green dot
                  ProcessTerminatedListener.attach(handler)

                  // prepare process
                  val process = ScaledaRun.runTaskBackground(
                      handler,
                      new File(ProjectConfig.projectBase.get),
                      target,
                      task
                  )

                  // run process in the background
                  process.start()

                  // return result
                  new ExecutionResult {
                    override def getExecutionConsole: ExecutionConsole = console

                    override def getActions: Array[AnAction] = Array()

                    override def getProcessHandler: ProcessHandler = handler
                  }
                }
              }

              state
            }
          })
      })
      .getOrElse({
        MainLogger.warn(s"Cannot find task name: $taskName")
        null
      })
  }
}
