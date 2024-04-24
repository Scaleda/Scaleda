package top.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import idea.project.io.YmlRootManager
import idea.utils.ScaledaIdeaLogger
import idea.utils.notification.CreateTypicalNotification
import idea.windows.bottomPanel.ConsoleService
import kernel.shell.ScaledaRun
import kernel.toolchain.runner.ScaledaRuntime
import kernel.utils.serialise.JSONHelper

import com.intellij.execution.Executor
import com.intellij.execution.configurations.{LocatableConfigurationBase, RunProfileState}
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.notification.NotificationType
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.search.ExecutionSearchScopes
import org.jdom.Element
import org.jetbrains.annotations.Nls
import top.scaleda.idea.windows.bottomPanel.console.ConsoleReceiver
import top.scaleda.kernel.utils.LogLevel

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
      .generateRuntimeFromName(targetName, taskName, profileName, profileHost)(project =
        YmlRootManager.getInstance(project).getRoots.head.toScaledaProject // fixme
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

    val searchScope =
      ExecutionSearchScopes
        .executionScope(project, environment.getRunProfile)

    val myConsoleBuilder =
      TextConsoleBuilderFactory.getInstance
        .createBuilder(project, searchScope)

    val console = myConsoleBuilder.getConsole
    consoleService.addListener((text: String, level: LogLevel.Value) => {
      console.print(
        text,
        level match {
          case LogLevel.Info => ConsoleViewContentType.NORMAL_OUTPUT
          case LogLevel.Warn => ConsoleViewContentType.LOG_WARNING_OUTPUT
          case LogLevel.Error => ConsoleViewContentType.ERROR_OUTPUT
          case LogLevel.Fatal => ConsoleViewContentType.ERROR_OUTPUT
          case _ => ConsoleViewContentType.SYSTEM_OUTPUT
        }
      )
    })

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

    new ScaledaRunProfileState(project, runtime, console, afterExecution)
  }

  private def afterExecution(
      rt: ScaledaRuntime,
      returnValues: Seq[Int],
      finishedAll: Boolean,
      meetErrors: Boolean
  ): Unit = {
//    if (meetErrors) {
//      runInEdt {
//
//        MessageListPanel.instance.foreach(_.switchToTab())
//      }
//
//      val causeMessage     = MessageListPanel.instance.flatMap(_.getCauseMessage(rt.id))
//      val causeCodeMessage = MessageListPanel.instance.flatMap(_.getCauseCode(rt.id))
//
//      // create notification
//      val notification = Notification.NOTIFICATION_GROUP.createNotification(
//        ScaledaBundle.message("notification.runner.error.execute.title"),
//        ScaledaBundle.message(
//          "notification.runner.error.execute.content.prefix",
//          causeMessage
//            .map(m => m.text)
//            .getOrElse(ScaledaBundle.message("notification.runner.error.execute.content.default"))
//        ),
//        Notification.levelMatch(LogLevel.Error)
//      )
//      notification.setSubtitle(ScaledaBundle.message("notification.runner.error.execute.subtitle"))
//      notification.setContextHelpAction(
//        new AnAction(
//          ScaledaBundle.message("notification.runner.error.execute.action.help.title"),
//          ScaledaBundle.message("notification.runner.error.execute.action.help.description"),
//          AllIcons.Actions.Help
//        ) {
//          override def actionPerformed(e: AnActionEvent) = {
//            // TODO: Help dialog
//            ScaledaIdeaLogger.warn("help", e)
//          }
//        }
//      )
//      causeMessage.foreach(m => {
//        notification.addAction(
//          new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.message")) {
//            override def actionPerformed(e: AnActionEvent) = {
//              // TODO: goto message window
//              MessageListPanel.instance.foreach(_.switchToTab())
//            }
//          }
//        )
//      })
//      causeCodeMessage.foreach(m => {
//        val file = new LocalFilePath(m.file.get, false).getVirtualFile
//        if (file != null) {
//          notification.addAction(new AnAction(ScaledaBundle.message("notification.runner.error.execute.action.code")) {
//            override def actionPerformed(e: AnActionEvent) = {
//              require(m.file.nonEmpty, "causeCodeMessage.file isEmpty")
//              val descriptor = new OpenFileDescriptor(
//                project,
//                file,
//                m.line.getOrElse(0),
//                0
//              )
//              descriptor.navigate(true)
//            }
//          })
//        }
//      })
//      notification.notify(project)
//    }
//    // remove message listeners
//    MessageListPanel.instance.foreach(_.detachFromLogger(rt.id))
//    ScaledaMessageParser.removeParser(rt.id)
//    if (!meetErrors) {
//      rt.task.taskType match {
//        // Only call waveform when simulate
//        case TaskType.Simulation =>
//          val config = ScaledaIdeaConfig.getConfig
//          val doHandleWaveform = () =>
//            WaveformHandler(config.waveformHandler).foreach(
//              _.handle(
//                project,
//                rt.executor.asInstanceOf[SimulationExecutor].vcdFile,
//                rt.context.get("sourceFiles").map(_.asInstanceOf[Seq[File]]).getOrElse(Seq())
//              )
//            )
//          if (config.autoOpenWaveform) doHandleWaveform()
//          else if (config.notifyWaveformUpdate) {
//            val notification =
//              Notification.NOTIFICATION_GROUP.createNotification(
//                ScaledaBundle.message("notification.runner.ok.simulation"),
//                ScaledaBundle.message("notification.runner.ok.simulation.waveform"),
//                NotificationType.INFORMATION
//              )
//            val action = if (config.waveformHandler == RvcdHandler.getId && RvcdService.hasInstance) {
//              new AnAction(ScaledaBundle.message("notification.runner.ok.simulation.action.reload")) {
//                override def actionPerformed(e: AnActionEvent) = doHandleWaveform()
//              }
//            } else {
//              new AnAction(ScaledaBundle.message("notification.runner.ok.simulation.action.open")) {
//                override def actionPerformed(e: AnActionEvent) = doHandleWaveform()
//              }
//            }
//            notification.addAction(action)
//            notification.notify(project)
//          }
//        case _ =>
//      }
//    }
  }
}
