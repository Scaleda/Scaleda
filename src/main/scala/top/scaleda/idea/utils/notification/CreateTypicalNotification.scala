package top.scaleda
package idea.utils.notification

import idea.ScaledaBundle
import idea.settings.auth.AuthorizationEditor
import idea.utils.Notification

import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls

object CreateTypicalNotification {
  def makeAuthorizationNotification(
      project: Project,
      @Nls content: String,
      notificationType: NotificationType
  ): Unit = {
    val notification = Notification.NOTIFICATION_GROUP
      .createNotification(
        ScaledaBundle.message("notification.runner.error.auth.title"),
        content,
        notificationType
      )
    notification.addAction(new AnAction(ScaledaBundle.message("notification.runner.error.auth.action")) {
      override def actionPerformed(e: AnActionEvent) = {
        ShowSettingsUtilImpl.showSettingsDialog(project, AuthorizationEditor.SETTINGS_ID, "")
      }
    })
    notification.notify(project)
  }
}
