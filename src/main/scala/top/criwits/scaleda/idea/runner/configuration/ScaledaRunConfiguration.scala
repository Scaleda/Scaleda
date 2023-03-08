package top.criwits.scaleda
package idea.runner.configuration

import idea.runner.{ScaledaRunProcessHandler, ScaledaRuntimeInfo}
import idea.rvcd.RvcdService
import idea.utils.{ConsoleLogger, MainLogger, Notification}
import idea.windows.tool.message.{ScaledaMessageParser, ScaledaMessageTab}
import kernel.net.RemoteClient
import kernel.net.remote.{Empty, RemoteProfile}
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
import org.jetbrains.annotations.Nls

import java.io.File
import java.time.Instant
import scala.collection.mutable
import scala.language.existentials

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
  // set empty to auto select
  var profileName = ""
  // set empty to disable remote
  var profileHost = ""
  // TODO: Add toolchain profile
  val extraEnvs = new mutable.HashMap[String, String]

  private val STORAGE_ID: String = "scaleda"

  private def dumpValuesToElement(e: Element): Element = {
    val c = if (e != null) e else new Element(STORAGE_ID)
    c.setAttribute("taskName", taskName)
    c.setAttribute("targetName", targetName)
    c.setAttribute("profileName", profileName)
    c.setAttribute("profileHost", profileHost)
    c
  }

  private def loadValuesFromElement(@Nls e: Element): Unit = {
    taskName = e.getAttributeValue("taskName")
    targetName = e.getAttributeValue("targetName")
    profileName = e.getAttributeValue("profileName")
    profileHost = e.getAttributeValue("profileHost")
  }

  override def writeExternal(element: Element): Unit = {
    val child: Element = element.getChild(STORAGE_ID)
    // ignore empty write
    if (targetName.nonEmpty && taskName.nonEmpty) {
      MainLogger.debug(s"writeExternal: write $targetName $taskName")
      if (child != null) {
        dumpValuesToElement(child)
      } else {
        element.addContent(dumpValuesToElement(null))
      }
      super.writeExternal(element)
    }
  }

  // FIXME: configuration cannot load from storage
  override def readExternal(element: Element): Unit = {
    val child = element.getChild(STORAGE_ID)
    if (child != null) {
      loadValuesFromElement(child)
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
    MainLogger.info(s"getState: taskName=$taskName, targetName=$targetName, profileName=$profileName")
    ProjectConfig
      .getConfig()
      .flatMap(c => {
        c.taskByName(taskName, targetName)
          .map(f => {
            val (target, task)                             = f
            var remoteProfiles: Option[Seq[RemoteProfile]] = None
            val profileHostUse                             = task.host.getOrElse(profileHost)
            val profile =
              if (profileHostUse.isEmpty) {
                // Run locally if no host argument provided
                Toolchain
                  .profiles()
                  .find(p =>
                    p.toolchainType == target.toolchain && (p.profileName == profileHostUse || profileName.isEmpty)
                  )
              } else {
                val (client, shutdown) = RemoteClient(profileHostUse)
                remoteProfiles = Some(client.getProfiles(Empty()).profiles)
                shutdown()
                remoteProfiles.get
                  .find(p => p.toolchainType == target.toolchain && (p.name == profileName || profileName.isEmpty))
              }
            if (profile.isEmpty) {
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
                s"${target.toolchain}-${target.name}-${task.name}-${Instant.now()}"

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
        MainLogger.warn(s"Cannot find task name: $taskName, target name: $targetName")
        null
      })
  }
}
