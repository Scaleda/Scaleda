package top.criwits.scaleda
package idea.utils

import idea.ScaledaBundle
import kernel.bin.ExtractAssets

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.{Project, ProjectManager}

class AssetsInstallAction(project: Project) extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    ProgressManager
      .getInstance()
      .runProcessWithProgressSynchronously(
        new Runnable {
          override def run(): Unit = {
            val r = ExtractAssets.run()
            if (!r) {
              Notification.NOTIFICATION_GROUP.createNotification(
                ScaledaBundle.message("notification.assets.install.failed"),
                ScaledaBundle.message("notification.assets.install.failed.description"),
                NotificationType.ERROR
              )
            } else {
              Notification.NOTIFICATION_GROUP.createNotification(
                ScaledaBundle.message("notification.assets.install.success"),
                NotificationType.INFORMATION
              )
            }
          }
        },
        ScaledaBundle.message("utils.installing.assets"),
        false,
        project
      )
  }
}

class AssetsInstallManuallyAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = ProjectManager.getInstance().getDefaultProject
    new AssetsInstallAction(project).actionPerformed(e)
  }
}
