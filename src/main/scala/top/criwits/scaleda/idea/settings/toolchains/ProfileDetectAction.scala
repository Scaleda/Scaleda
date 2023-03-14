package top.criwits.scaleda
package idea.settings.toolchains

import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.notification.{NotificationGroupManager, NotificationType}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.utils.Notification
import top.criwits.scaleda.kernel.toolchain.{Toolchain, ToolchainProfileDetector}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

/**
 * Detect profiles and popup notification
 * @param project project now
 */
class ProfileDetectAction(project: Project) extends AnAction {
  override def actionPerformed(e: AnActionEvent) = {
    val gotoSettingsAction = new AnAction(ScaledaBundle.message("detector.notification.settings")) {
      override def actionPerformed(e: AnActionEvent) = {
        ShowSettingsUtilImpl.showSettingsDialog(project, ToolchainsConfigurable.SETTINGS_ID, "")
      }
    }
    val detected = Await.result(ToolchainProfileDetector.detectProfiles(), 30.seconds)
    if (detected.nonEmpty) {
      @Nls
      val content = detected.size match {
        case 1 => ScaledaBundle.message("detector.notification.found.one", detected.head.profileName)
        case _ => ScaledaBundle.message("detector.notification.found.many", detected.size)
      }
      val notification = Notification.NOTIFICATION_GROUP
        .createNotification(content, NotificationType.INFORMATION)
      notification.setTitle(ScaledaBundle.message("detector.notification.found.title"))
      // save detected profiles to disk
      notification.addAction(new AnAction(ScaledaBundle.message("detector.notification.add")) {
        override def actionPerformed(e: AnActionEvent) = {
          detected.foreach(Toolchain.syncProfile)
          notification.expire()
        }
      })
      notification.addAction(gotoSettingsAction)
      notification.notify(project)
    } else {
      val notification = Notification.NOTIFICATION_GROUP.createNotification(ScaledaBundle.message("detector.notification.found.title"), ScaledaBundle.message("detector.notification.found.none"), NotificationType.WARNING)
      notification.addAction(gotoSettingsAction)
      notification.notify(project)
    }
  }
}
