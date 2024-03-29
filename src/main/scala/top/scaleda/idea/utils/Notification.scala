package top.scaleda
package idea.utils

import idea.utils.Notification.NOTIFICATION_GROUP
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.notification.{NotificationGroup, NotificationGroupManager, NotificationType}
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}

class Notification(project: Project) extends BasicLogger {
  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ): Unit = {
    if (xs.length < 2) {
      val msg = xs.mkString(" ")
      NOTIFICATION_GROUP
        .createNotification(msg, Notification.levelMatch(level))
        .notify(project)
    } else {
      // select title and content
      val title   = xs.head.toString
      val content = xs.slice(1, xs.length).mkString(" ")
      val notification = NOTIFICATION_GROUP
        .createNotification(content, Notification.levelMatch(level))
      notification.setTitle(title)
      notification.notify(project)
    }
    MainLogger.info(Seq("notification:") ++ xs: _*)
  }
}

object Notification {
  val NOTIFICATION_GROUP_ID = "Scaleda"
  val NOTIFICATION_GROUP: NotificationGroup =
    NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_ID)

  def apply(project: Project): Notification = new Notification(project)
  // def apply(): Notification                 = new Notification(ProjectNow.apply().get)

  def levelMatch(level: LogLevel.Value): NotificationType = {
    import LogLevel._
    level match {
      case Info => NotificationType.INFORMATION
      case Warn => NotificationType.WARNING
      case _    => NotificationType.ERROR
    }
  }
}
