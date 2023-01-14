package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.execution.ui.{ConsoleView, ConsoleViewContentType}
import com.intellij.openapi.Disposable
import top.criwits.scaleda.kernel.utils.LogLevel

import scala.collection.mutable

class ScaledaConsoleService extends Disposable {
  private val listeners = new mutable.HashMap[String, ScaledaLogReceiver]

  def addListener(sourceId: String, receiver: ScaledaLogReceiver) =
    listeners.put(sourceId, receiver)

  def print(id: String, msg: String, l: LogLevel.Value) =
    listeners.get(id).foreach(_.print(msg, l))

  def stop(logSourceId: String): Unit = {}

  override def dispose() = {}
}
