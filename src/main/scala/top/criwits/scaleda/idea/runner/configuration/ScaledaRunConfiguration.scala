package top.criwits.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import idea.runner.{ScaledaRunProcessHandler, ScaledaRuntimeInfo}
import idea.rvcd.RvcdService
import idea.settings.auth.AuthorizationEditor
import idea.utils.{ConsoleLogger, MainLogger, Notification, runInEdt}
import idea.windows.tool.message.{ScaledaMessage, ScaledaMessageParser, ScaledaMessageTab}
import kernel.database.UserException
import kernel.project.config.TaskType
import kernel.shell.ScaledaRun
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.SimulationExecutor
import kernel.utils.LogLevel

import com.intellij.execution.configurations.{LocatableConfigurationBase, RunConfiguration, RunProfileState}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.{ProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes
import org.jdom.Element
import org.jetbrains.annotations.Nls

import java.util.concurrent.TimeoutException
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
  val extraEnvs   = new mutable.HashMap[String, String]

  private val STORAGE_ID: String = "scaleda"

  private def dumpValuesToElement(e: Element): Element = {
    val c = if (e != null) e else new Element(STORAGE_ID)
    c.setAttribute("taskName", taskName)
    c.setAttribute("targetName", targetName)
    if (profileName != null) c.setAttribute("profileName", profileName)
    else c.setAttribute("profileName", "")
    if (profileHost != null) c.setAttribute("profileHost", profileHost)
    else c.setAttribute("profileHost", "")
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
    * @param ideaExecutor
    * @param environment
    * @return
    */
  override def getState(
      ideaExecutor: Executor,
      environment: ExecutionEnvironment
  ): RunProfileState = {
    MainLogger.info(s"getState: taskName=$taskName, targetName=$targetName, profileName=$profileName")
    val runtimeOptional = ScaledaRun.generateRuntimeFromName(targetName, taskName, profileName, profileHost)
    if (runtimeOptional.isEmpty) {
      MainLogger.warn("cannot generate runtime", targetName, taskName, profileName, profileHost)
      return null
    }
    val runtime = runtimeOptional.get
    val searchScope =
      ExecutionSearchScopes
        .executionScope(project, environment.getRunProfile)

    val myConsoleBuilder =
      TextConsoleBuilderFactory.getInstance
        .createBuilder(project, searchScope)

    val console = myConsoleBuilder.getConsole
    def afterExecution(
        rt: ScaledaRuntimeInfo,
        returnValues: Seq[Int],
        finishedAll: Boolean,
        meetErrors: Boolean
    ): Unit = {
      // TODO: if errors, switch to message tab
      if (meetErrors) {
        // Assert: must be called on EDT
        // ToolWindowManager.getInstance(project).getFocusManager.toFront(ScaledaMessageTab.instance)
        runInEdt {
          // ToolWindowManager.getInstance(project).getFocusManager.toFront(ScaledaMessageTab.instance)
          // ToolWindowManager.getInstance(project).getFocusManager.requestFocusInProject(ScaledaMessageTab.instance, project)
          // ScaledaMessageTab.instance.requestFocusInWindow()
          // ToolWindowManager.getInstance(project).
          // not working
          ScaledaMessageTab.instance.switchToTab()
        }
        // DumbService
        //   .getInstance(project)
        //   .runWhenSmart(() => {
        //     ToolWindowManager.getInstance(project).getFocusManager.toFront(ScaledaMessageTab.instance)
        //   })

        // ToolWindowManager.getInstance(project).getFocusManager
        // ScaledaMessageTab.instance.switchToTab()

        val causeMessage: (ScaledaRuntimeInfo, Option[ScaledaMessage]) =
          ScaledaMessageTab.instance
            .getFirstError(rt.id)
            .getOrElse(
              ScaledaMessageTab.instance
                .getFirstWarning(rt.id)
                .getOrElse(
                  ScaledaMessageTab.instance
                    .getFirstInfo(rt.id)
                    .getOrElse((rt, None))
                )
            )

        // create notification
        val notification = Notification.NOTIFICATION_GROUP.createNotification(
          ScaledaBundle.message("notification.runner.error.execute.title"),
          ScaledaBundle.message(
            "notification.runner.error.execute.content.prefix",
            causeMessage._2
              .map(m => m.text)
              .getOrElse(ScaledaBundle.message("notification.runner.error.execute.content.default"))
          ),
          Notification.levelMatch(LogLevel.Error)
        )
        notification.setSubtitle(ScaledaBundle.message("notification.runner.error.execute.subtitle"))
        notification.setContextHelpAction(
          new AnAction(
            ScaledaBundle.message("notification.runner.error.execute.action.help.title"),
            ScaledaBundle.message("notification.runner.error.execute.action.help.description"),
            AllIcons.Actions.Help
          ) {
            override def actionPerformed(e: AnActionEvent) = {
              // TODO: Help dialog
              MainLogger.warn("help", e)
            }
          }
        )
        Seq(
          new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.message")) {
            override def actionPerformed(e: AnActionEvent) = {
              // TODO: goto message window
              ScaledaMessageTab.instance.switchToTab()
            }
          },
          new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.code")) {
            override def actionPerformed(e: AnActionEvent) = {
              // TODO: Goto Source Code
            }
          }
        ).foreach(notification.addAction)
        notification.notify(project)
      }
      // remove message listeners
      ScaledaMessageTab.instance.detachFromLogger(rt.id)
      ScaledaMessageParser.removeParser(rt.id)
      if (!meetErrors) {
        rt.task.taskType match {
          // Only call rvcd when simulate
          case TaskType.Simulation =>
            project
              .getService(classOf[RvcdService])
              .launchWithWaveformAndSource(rt.executor.asInstanceOf[SimulationExecutor].vcdFile, Seq.empty)
          // TODO / FIXME: Source is not given
          case _ =>
        }
      }
    }

    val handler =
      new ScaledaRunProcessHandler(
        project,
        new ConsoleLogger(console, logSourceId = Some(runtime.id)),
        runtime,
        afterExecution
      )

    (_: Executor, _: ProgramRunner[_]) => {
      // attach listener to control the green dot
      ProcessTerminatedListener.attach(handler)

      // prepare process
      val thread = ScaledaRun.runTaskBackground(handler, runtime)

      // attach message parser listener
      ScaledaMessageTab.instance.attach(runtime)
      Toolchain.toolchainMessageParser
        .get(runtime.target.toolchain)
        .foreach(parserProvider => ScaledaMessageParser.registerParser(runtime.id, parserProvider.messageParser))

      // setup exception handler
      thread.setUncaughtExceptionHandler((_thread: Thread, throwable: Throwable) => {
        // detach message parser listener
        ScaledaMessageTab.instance.detachFromLogger(runtime.id)
        ScaledaMessageParser.removeParser(runtime.id)
        throwable match {
          case e: UserException =>
            val notification = Notification.NOTIFICATION_GROUP
              .createNotification(
                ScaledaBundle.message("notification.runner.error.auth.title"),
                e.getMessage,
                Notification.levelMatch(LogLevel.Error)
              )
            notification.addAction(new AnAction(ScaledaBundle.message("notification.runner.error.auth.action")) {
              override def actionPerformed(e: AnActionEvent) = {
                ShowSettingsUtilImpl.showSettingsDialog(project, AuthorizationEditor.SETTINGS_ID, "")
              }
            })
          case e: TimeoutException =>
            Notification().error("Timeout", e.getMessage, ", check your connections")
          case e: Throwable => throw e
        }
        handler.destroyProcess()
      })

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
}
