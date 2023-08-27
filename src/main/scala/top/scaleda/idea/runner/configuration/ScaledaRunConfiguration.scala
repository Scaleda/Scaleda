package top.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import idea.application.config.ScaledaIdeaConfig
import idea.project.io.YmlRootManager
import idea.runner.ScaledaRunProcessHandler
import idea.rvcd.RvcdService
import idea.toolchain.ToolchainIdea
import idea.utils.notification.CreateTypicalNotification
import idea.utils.{Notification, ScaledaIdeaLogger, runInEdt}
import idea.waveform.{RvcdHandler, WaveformHandler}
import idea.windows.bottomPanel.message.{MessageListService, ScaledaMessageParser}
import kernel.database.UserException
import kernel.project.config.TaskType
import kernel.shell.ScaledaRun
import kernel.toolchain.executor.SimulationExecutor
import kernel.toolchain.runner.ScaledaRuntime
import kernel.utils.LogLevel
import kernel.utils.serialise.JSONHelper

import com.intellij.execution.configurations.{LocatableConfigurationBase, RunProfileState}
import com.intellij.execution.process.{ProcessHandler, ProcessTerminatedListener}
import com.intellij.execution.runners.{ExecutionEnvironment, ProgramRunner}
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.LocalFilePath
import org.jdom.Element
import org.jetbrains.annotations.Nls
import top.scaleda.idea.windows.bottomPanel.ConsoleService

import java.io.File
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

  implicit val projectUsing: Project = project

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
    c.setAttribute("extraEnvs", JSONHelper(extraEnvs.toMap))
    c
  }

  private def loadValuesFromElement(@Nls e: Element): Unit = {
    taskName = e.getAttributeValue("taskName")
    targetName = e.getAttributeValue("targetName")
    profileName = e.getAttributeValue("profileName")
    profileHost = e.getAttributeValue("profileHost")
    val v = e.getAttributeValue("extraEnvs")
    extraEnvs.clear()
    if (v != null) {
      val m = JSONHelper(v, classOf[Map[String, String]])
      if (m != null && m.nonEmpty)
        extraEnvs.addAll(m)
    }
  }

  override def writeExternal(element: Element): Unit = {
    val child: Element = element.getChild(STORAGE_ID)
    // ignore empty write
    if (targetName.nonEmpty && taskName.nonEmpty) {
      ScaledaIdeaLogger.debug(s"writeExternal: write $targetName $taskName")
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
      ScaledaIdeaLogger.info(s"readExternal: got $targetName $taskName")
    }
  }

  override def getConfigurationEditor: SettingsEditor[ScaledaRunConfiguration] =
    new ScaledaRunConfigurationEditor(project)

  def generateRuntime: Option[ScaledaRuntime] =
    ScaledaRun
      .generateRuntimeFromName(targetName, taskName, profileName, profileHost)(manifest =
        YmlRootManager.getInstance(project).getRoots.head.toProjectManifest
      )
      .map(_.copy(extraEnvs = extraEnvs.toMap))

  /** Returns a [[RunProfileState]], which is actually used to run
    * @param ideaExecutor
    * @param environment
    * @return
    */
  override def getState(
      ideaExecutor: Executor,
      environment: ExecutionEnvironment
  ): RunProfileState = {
    // clear console and msg list
    val consoleService = project.getService(classOf[ConsoleService])
    consoleService.clear()

    val runtimeOptional = generateRuntime
    if (runtimeOptional.isEmpty) {
      ScaledaIdeaLogger.warn("cannot generate runtime", targetName, taskName, profileName, profileHost)
      CreateTypicalNotification.makeAuthorizationNotification(
        project,
        ScaledaBundle.message("tasks.configuration.profile.state.auth", profileHost),
        NotificationType.WARNING
      )
      return null
    }

    val runtime = ScaledaRun.preprocess(runtimeOptional.get)
    return null

    (_: Executor, _: ProgramRunner[_]) => {
      // may run preset

      val handler =
        new ScaledaRunProcessHandler(
          project,
          runtime,
          afterExecution
        )

      // attach listener to control the green dot
      ProcessTerminatedListener.attach(handler)

      // prepare process
      val thread = ScaledaRun.runTaskBackground(handler, runtime)

      // message parser
      ToolchainIdea.toolchainMessageParser
        .get(runtime.target.toolchain)
        .foreach(parserProvider =>
          project.getService(classOf[MessageListService]).registerParser(parserProvider.messageParser)
        )
      project.getService(classOf[MessageListService]).registerRuntime(runtime)

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

  private def afterExecution(
      rt: ScaledaRuntime,
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
        MessageListPanel.instance.foreach(_.switchToTab())
      }
      // DumbService
      //   .getInstance(project)
      //   .runWhenSmart(() => {
      //     ToolWindowManager.getInstance(project).getFocusManager.toFront(ScaledaMessageTab.instance)
      //   })

      // ToolWindowManager.getInstance(project).getFocusManager
      // ScaledaMessageTab.instance.switchToTab()

      val causeMessage     = MessageListPanel.instance.flatMap(_.getCauseMessage(rt.id))
      val causeCodeMessage = MessageListPanel.instance.flatMap(_.getCauseCode(rt.id))

      // create notification
      val notification = Notification.NOTIFICATION_GROUP.createNotification(
        ScaledaBundle.message("notification.runner.error.execute.title"),
        ScaledaBundle.message(
          "notification.runner.error.execute.content.prefix",
          causeMessage
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
            ScaledaIdeaLogger.warn("help", e)
          }
        }
      )
      causeMessage.foreach(m => {
        notification.addAction(
          new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.message")) {
            override def actionPerformed(e: AnActionEvent) = {
              // TODO: goto message window
              MessageListPanel.instance.foreach(_.switchToTab())
            }
          }
        )
      })
      causeCodeMessage.foreach(m => {
        val file = new LocalFilePath(m.file.get, false).getVirtualFile
        if (file != null) {
          notification.addAction(new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.code")) {
            override def actionPerformed(e: AnActionEvent) = {
              require(m.file.nonEmpty, "causeCodeMessage.file isEmpty")
              val descriptor = new OpenFileDescriptor(
                project,
                file,
                m.line.getOrElse(0),
                0
              )
              descriptor.navigate(true)
            }
          })
        }
      })
      notification.notify(project)
    }
    // remove message listeners
    MessageListPanel.instance.foreach(_.detachFromLogger(rt.id))
    ScaledaMessageParser.removeParser(rt.id)
    if (!meetErrors) {
      rt.task.taskType match {
        // Only call waveform when simulate
        case TaskType.Simulation =>
          val config = ScaledaIdeaConfig.getConfig
          val doHandleWaveform = () =>
            WaveformHandler(config.waveformHandler).foreach(
              _.handle(
                project,
                rt.executor.asInstanceOf[SimulationExecutor].vcdFile,
                rt.context.get("sourceFiles").map(_.asInstanceOf[Seq[File]]).getOrElse(Seq())
              )
            )
          if (config.autoOpenWaveform) doHandleWaveform()
          else if (config.notifyWaveformUpdate) {
            val notification =
              Notification.NOTIFICATION_GROUP.createNotification(
                ScaledaBundle.message("notification.runner.ok.simulation"),
                ScaledaBundle.message("notification.runner.ok.simulation.waveform"),
                NotificationType.INFORMATION
              )
            val action = if (config.waveformHandler == RvcdHandler.getId && RvcdService.hasInstance) {
              new AnAction(ScaledaBundle.message("notification.runner.ok.simulation.action.reload")) {
                override def actionPerformed(e: AnActionEvent) = doHandleWaveform()
              }
            } else {
              new AnAction(ScaledaBundle.message("notification.runner.ok.simulation.action.open")) {
                override def actionPerformed(e: AnActionEvent) = doHandleWaveform()
              }
            }
            notification.addAction(action)
            notification.notify(project)
          }
        case _ =>
      }
    }
  }
}
