package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.Disposable

import scala.collection.mutable

class ScaledaLoggingService extends Disposable {
  private val listeners = new mutable.HashMap[String, ConsoleView]

  def addListener(sourceId: String, receiver: ConsoleView) =
    listeners.put(sourceId, receiver)

  def print(id: String, msg: String, t: ConsoleViewContentType) =
    listeners.get(id).foreach(_.print(msg, t))

  def stop(logSourceId: String): Unit = {}

  override def dispose() = {}
}
