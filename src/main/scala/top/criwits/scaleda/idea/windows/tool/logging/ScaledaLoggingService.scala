package top.criwits.scaleda
package idea.windows.tool.logging

import kernel.utils.LogLevel

import com.intellij.openapi.Disposable

import scala.collection.mutable

class ScaledaLoggingService extends Disposable {
  private val listeners = new mutable.HashMap[String, ScaledaLogReceiver]

  def addListener(sourceId: String, receiver: ScaledaLogReceiver) =
    listeners.put(sourceId, receiver)

  def print(id: String, msg: String, l: LogLevel.Value) =
    listeners.get(id).foreach(_.print(id, msg, l))

  def stop(logSourceId: String): Unit = {}

  override def dispose() = {}
}
