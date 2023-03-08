package top.criwits.scaleda
package idea.runner.configuration

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.net.remote.RemoteProfile
import kernel.net.{RemoteClient, RemoteServer}
import kernel.project.config.ProjectConfig

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.{ComboBox, TextFieldWithBrowseButton}
import com.intellij.util.ui.{FormBuilder, UIUtil}

import java.awt.event.{ItemEvent, KeyEvent, KeyListener}
import scala.collection.mutable
import scala.jdk.javaapi.CollectionConverters

class ScaledaRunConfigurationEditor(private val project: Project) extends SettingsEditor[ScaledaRunConfiguration] {

  private val targetName  = new ComboBox[String]
  private val taskName    = new ComboBox[String]
  private val profileName = new ComboBox[String]
  private val profileHost = new TextFieldWithBrowseButton

  private val loadedRemoteProfiles   = new mutable.HashMap[String, Seq[RemoteProfile]]
  private val requestProfilesThreads = new mutable.HashMap[String, Thread]()

  private val environmentVarsComponent = new EnvironmentVariablesComponent

  ProjectConfig.getConfig().foreach(c => c.targets.foreach(t => targetName.addItem(t.name)))

  private def requestRemoteProfiles(): Unit = {
    val host = profileHost.getText
    MainLogger.info(s"requestRemoteProfiles: $host")
    if (host.isEmpty || loadedRemoteProfiles.contains(host)) return
    val thread = new Thread(() => {
      try {
        val (client, shutdown) = RemoteClient(host)
        val profiles           = client.getProfiles(top.criwits.scaleda.kernel.net.remote.Empty.of()).profiles
        MainLogger.info("got profiles", profiles)
        loadedRemoteProfiles.synchronized {
          loadedRemoteProfiles.put(host, profiles)
        }
        profileName.synchronized {
          profileName.removeAllItems()
          profiles.foreach(p => profileName.addItem(p.name))
        }
        shutdown()
      } catch {
        case e: Throwable => MainLogger.info("cannot connect server:", e)
      } finally {
        requestProfilesThreads.synchronized {
          requestProfilesThreads.remove(host)
        }
      }
    })
    thread.start()
    requestProfilesThreads.synchronized {
      requestProfilesThreads.put(host, thread)
    }
  }

  profileHost.getTextField.addKeyListener(new KeyListener {
    override def keyTyped(keyEvent: KeyEvent) = requestRemoteProfiles()

    override def keyPressed(keyEvent: KeyEvent) = requestRemoteProfiles()

    override def keyReleased(keyEvent: KeyEvent) = requestRemoteProfiles()
  })

  profileHost.addActionListener(e => {
    val group = new DefaultActionGroup()
    RemoteServer.AVAILABLE_REMOTE_HOSTS.foreach(host => {
      group.add(new AnAction(host) {
        override def actionPerformed(e: AnActionEvent) = {
          profileHost.setText(host)
          requestRemoteProfiles()
        }
      })
    })
    val popupMenu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP, group)
    popupMenu.getComponent.show(profileHost, profileHost.getX, profileHost.getY)
  })

  targetName.addItemListener(e => {
    if (e.getStateChange == ItemEvent.SELECTED) {
      val newTargetName = e.getItem.asInstanceOf[String]
      taskName.removeAllItems()
      ProjectConfig
        .getConfig()
        .foreach(c =>
          c.targets.find(_.name == newTargetName).foreach(t => t.tasks.foreach(t => taskName.addItem(t.name)))
        )
    }
  })

  private val panel = FormBuilder
    .createFormBuilder()
    .setAlignLabelOnRight(false)
    .setHorizontalGap(UIUtil.DEFAULT_HGAP)
    .setVerticalGap(UIUtil.DEFAULT_VGAP)
    .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.target.name"), targetName)
    .addLabeledComponent(ScaledaBundle.message("tasks.configuration.label.task.name"), taskName)
    // TODO: i18n
    .addLabeledComponent("Profile name", profileName)
    .addLabeledComponent("Profile host", profileHost)
    .addComponent(environmentVarsComponent)
    .getPanel

  override def resetEditorFrom(s: ScaledaRunConfiguration) = {
    // Warning: because targetName has model data listener, should set targetName first!!
    targetName.setItem(s.targetName)
    taskName.removeAllItems()
    ProjectConfig
      .getConfig()
      .foreach(c => c.targets.find(_.name == s.targetName).foreach(t => t.tasks.foreach(t => taskName.addItem(t.name))))
    taskName.setItem(s.taskName)
    environmentVarsComponent.setEnvs(CollectionConverters.asJava(s.extraEnvs))
    profileHost.setText(s.profileHost)
    profileName.setItem(s.profileName)
    requestRemoteProfiles()
  }

  override def applyEditorTo(s: ScaledaRunConfiguration) = {
    s.taskName = taskName.getItem
    s.targetName = targetName.getItem
    s.extraEnvs.addAll(CollectionConverters.asScala(environmentVarsComponent.getEnvs))
    s.profileName = profileName.getItem
    s.profileHost = profileHost.getText
  }

  override def createEditor() = panel

  override def disposeEditor() = {
    super.disposeEditor()
    requestProfilesThreads.synchronized {
      requestProfilesThreads.foreach(t => {
        if (t._2.isAlive) t._2.interrupt()
      })
    }
  }
}
