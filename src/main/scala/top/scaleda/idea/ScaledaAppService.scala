package top.scaleda
package idea

import com.intellij.openapi.Disposable

trait ScaledaAppService extends Disposable {
  def initLoggers(): Unit
  def initJinja(): Unit
}
