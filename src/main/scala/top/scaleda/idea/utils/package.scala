package top.scaleda
package idea

import com.intellij.openapi.application.ApplicationManager.{getApplication => application}
import com.intellij.openapi.application.{ApplicationInfo, ApplicationManager, ModalityState}

import _root_.scala.annotation.nowarn

package object utils {
  def isInternalMode: Boolean = application.isInternal
  def isUnitTestMode: Boolean = application.isUnitTestMode

  def inWriteAction[T](body: => T): T = application match {
    case application if application.isWriteAccessAllowed => body
    case application                                     => application.runWriteAction(body)
  }

  def inReadAction[T](body: => T): T = application match {
    case application if application.isReadAccessAllowed => body
    case application                                    => application.runReadAction(body)
  }

  def invokeLater[T](body: => T, modalityState: Option[ModalityState] = None): Unit =
    ApplicationManager.getApplication.invokeLater(
      new Runnable {
        override def run() = body
      },
      modalityState.getOrElse(ModalityState.defaultModalityState())
    )

  def runInEdt[T](body: => T, modalityState: Option[ModalityState] = None) = {
    val app = ApplicationManager.getApplication
    if (app.isDispatchThread) {
      body
    } else {
      invokeLater(body = body, modalityState = modalityState)
    }
  }

  import _root_.scala.language.implicitConversions

  @nowarn("cat=deprecation")
  private[this] implicit def toComputable[T](action: => T): com.intellij.openapi.util.Computable[T] = () => action

  // def ideaVersion: String = application.getComponent(classOf[ApplicationInfo]).getMajorVersion
}
