package top.criwits.scaleda
package idea.runner.configuration

import idea.runner.{ScaledaRunProcessHandler, ScaledaRuntimeInfo}
import idea.rvcd.RvcdService
import idea.utils.{ConsoleLogger, MainLogger, Notification}
import idea.windows.tool.message.{ScaledaMessageParser, ScaledaMessageTab}
import kernel.net.remote.{Empty, RemoteProfile}
import kernel.net.{RemoteClient, RemoteServer}
import kernel.project.config.{ProjectConfig, TaskConfig, TaskType}
import kernel.shell.ScaledaRun
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.{SimulationExecutor, Executor => SExecutor}

import com.intellij.execution.configurations.{LocatableConfigurationBase, RunConfiguration, RunProfileState}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.{ProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes
import org.jdom.Element

import java.io.File
import java.time.Instant
import scala.collection.mutable
import scala.language.existentials
import scala.util.Random

/** A configuration representing a specific Scaleda task as well as corresponding toolchain
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
  var taskName   = ""
  // TODO: Add toolchain profile
  val extraEnvs = new mutable.HashMap[String, String]

  private val STORAGE_ID: String = "scaleda"

  override def writeExternal(element: Element): Unit = {
    val child = element.getChild(STORAGE_ID)
    // ignore empty write
    if (targetName.nonEmpty && taskName.nonEmpty) {
      MainLogger.debug(s"writeExternal: write $targetName $taskName")
      if (child != null) {
        child.setAttribute("taskName", taskName)
        child.setAttribute("targetName", targetName)
      } else {
        val c = new Element(STORAGE_ID)
        c.setAttribute("taskName", taskName)
        c.setAttribute("targetName", targetName)
        element.addContent(c)
      }
      super.writeExternal(element)
    }
  }

  // FIXME: configuration cannot load from storage
  override def readExternal(element: Element): Unit = {
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      val t = child.getAttributeValue("taskName")
      val r = child.getAttributeValue("targetName")
      // FIXME: ignore sync with config yml
      // ProjectConfig
      //   .getConfig()
      //   .foreach(c => {
      //     MainLogger.warn(s"readExternal: raw $r $t")
      //     // ignore empty read
      //     if (t.nonEmpty && r.nonEmpty)
      //       c.taskByName(t, r)
      //         .foreach(f => {
      //           targetName = f._1.name
      //           taskName = f._2.name
      //         })
      //   })
      targetName = r
      taskName = t
      MainLogger.info(s"readExternal: got $targetName $taskName")
    }
  }

  override def getConfigurationEditor: SettingsEditor[_ <: RunConfiguration] =
    new ScaledaRunConfigurationEditor(project)

  /** Returns a [[RunProfileState]], which is actually used to run
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
            val (target, task)                             = f
            var remoteProfiles: Option[Seq[RemoteProfile]] = None
            val hasProfile =
              if (task.host.isEmpty) {
                // Run locally if no host argument provided
                Toolchain
                  .profiles()
                  .exists(_.toolchainType == target.toolchain)
              } else {
                // TODO: add proxy
                val stub = RemoteClient(task.host.get, RemoteServer.DEFAULT_PORT)
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

              val runtimeId =
                s"${target.toolchain}-${target.name}-${task.name}-${Instant.now()}-${Random.nextInt(65535)}"

              def afterExecution(task: TaskConfig, executor: SExecutor): Unit = {
                // remove message listeners
                ScaledaMessageTab.instance.detachFromLogger(runtimeId)
                ScaledaMessageParser.removeParser(runtimeId)
                task.taskType match {
                  // Only call rvcd when simulate
                  case TaskType.Simulation =>
                    project
                      .getService(classOf[RvcdService])
                      .launchWithWaveformAndSource(executor.asInstanceOf[SimulationExecutor].vcdFile, Seq.empty)
                  // TODO / FIXME: Source is not given
                  case _ =>
                }
              }

              val handler =
                new ScaledaRunProcessHandler(
                  new ConsoleLogger(console, logSourceId = Some(runtimeId)),
                  task,
                  afterExecution
                )

              val state = new RunProfileState {
                override def execute(
                    executor: Executor,
                    runner: ProgramRunner[_]
                ): ExecutionResult = {
                  // attach listener to control the green dot
                  ProcessTerminatedListener.attach(handler)

                  // prepare process
                  val thread = ScaledaRun.runTaskBackground(
                    handler,
                    new File(ProjectConfig.projectBase.get),
                    target,
                    task
                  )

                  val runtime = ScaledaRuntimeInfo(
                    runtimeId,
                    target = target,
                    task = task,
                    // TODO: use profile in configuration
                    profile = Toolchain.profiles().find(_.toolchainType == target.toolchain).get
                  )

                  // attach message parser listener
                  ScaledaMessageTab.instance.attach(runtime)
                  Toolchain.toolchainMessageParser
                    .get(target.toolchain)
                    .foreach(parserProvider =>
                      ScaledaMessageParser.registerParser(runtimeId, parserProvider.messageParser)
                    )

                  // run process in the background
                  thread.start()

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
