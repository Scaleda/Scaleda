package top.criwits.scaleda
package idea

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationManager.{getApplication => application}

import _root_.scala.annotation.nowarn

package object utils {
  def isInternalMode: Boolean = application.isInternal
  def isUnitTestMode: Boolean = application.isUnitTestMode

  def inWriteAction[T](body: => T): T = application match {
    case application if application.isWriteAccessAllowed => body
    case application => application.runWriteAction(body)
  }

  def inReadAction[T](body: => T): T = application match {
    case application if application.isReadAccessAllowed => body
    case application => application.runReadAction(body)
  }

  def invokeLater[T](body: => T): Unit =
    ApplicationManager.getApplication.invokeLater(() => body)


  import _root_.scala.language.implicitConversions

  @nowarn("cat=deprecation")
  private[this] implicit def toComputable[T](action: => T): com.intellij.openapi.util.Computable[T] = () => action

}

