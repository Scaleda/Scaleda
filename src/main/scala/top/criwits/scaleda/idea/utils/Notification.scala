package top.criwits.scaleda
package idea.utils

import idea.ScaledaBundle
import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.notification.{NotificationGroupManager, NotificationType}
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import sourcecode.{File, Line, Name}

object Notification extends BasicLogger with Disposable {
  private var project: Option[Project] = None

  def setProject(p: Project) = project = Some(p)

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit
      line: Line,
      file: File,
      name: Name
  ): Unit = {
    val msg = xs.mkString(" ")
    import LogLevel._
    project.foreach(project => {
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
    })
  }

  override def dispose() = project = None
}
