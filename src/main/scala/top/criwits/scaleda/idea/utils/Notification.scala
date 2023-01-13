package top.criwits.scaleda
package idea.utils

import idea.ScaledaBundle
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.notification.{NotificationGroupManager, NotificationType}
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}

class Notification(project: Project) extends BasicLogger {
  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ): Unit = {
    val msg = xs.mkString(" ")
    import LogLevel._
    NotificationGroupManager
      .getInstance()
      .getNotificationGroup(ScaledaBundle.message("notification.id"))
      .createNotification(
        msg,
        level match {
          case Info  => NotificationType.INFORMATION
          case Warn  => NotificationType.WARNING
          case Error => NotificationType.ERROR
          case _     => ???
        }
      )
      .notify(project)
  }
}

object Notification {
  def apply(project: Project): Notification = new Notification(project)
}
