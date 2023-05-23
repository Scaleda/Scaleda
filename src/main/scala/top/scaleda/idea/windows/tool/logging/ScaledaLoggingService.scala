package top.scaleda
package idea.windows.tool.logging

import kernel.utils.LogLevel

import com.intellij.openapi.Disposable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class ScaledaLoggingMessage(id: String, msg: String, level: LogLevel.Value)

class ScaledaLoggingService extends Disposable {
  private val listeners          = new mutable.HashMap[String, ScaledaLogReceiver]
  private val messagesDidNotRecv = ArrayBuffer[ScaledaLoggingMessage]()

  def addListener(sourceId: String, receiver: ScaledaLogReceiver) = {
    synchronized {
      listeners.put(sourceId, receiver)
      getBufferedMessages(sourceId).foreach(m => receiver.print(m.id, m.msg, m.level))
    }
  }

  def print(id: String, msg: String, l: LogLevel.Value) = {
    synchronized {
      listeners.get(id) match {
        case Some(listener) => listener.print(id, msg, l)
        case _              => messagesDidNotRecv.addOne(ScaledaLoggingMessage(id, msg, l))
      }
    }
  }

  def removeListener(logSourceId: String): Unit = synchronized { listeners.remove(logSourceId) }

  private def getBufferedMessages(logSourceId: String) = {
    val r = messagesDidNotRecv.filter(_.id == logSourceId).toSeq
    messagesDidNotRecv.filterInPlace(_.id != logSourceId)
    r
  }

  override def dispose() = {}
}
