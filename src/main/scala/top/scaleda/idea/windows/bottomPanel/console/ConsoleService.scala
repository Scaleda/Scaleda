package top.scaleda
package idea.windows.bottomPanel.console

import kernel.utils.LogLevel

import com.intellij.openapi.Disposable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class ConsoleMessage(id: String, msg: String, level: LogLevel.Value)

class ConsoleService extends Disposable {
  private val listeners          = new mutable.HashMap[String, ConsoleReceiver]
  private val messagesDidNotRecv = ArrayBuffer[ConsoleMessage]()

  def addListener(sourceId: String, receiver: ConsoleReceiver): Unit = {
    synchronized {
      listeners.put(sourceId, receiver)
      getBufferedMessages(sourceId).foreach(m => receiver.print(m.id, m.msg, m.level))
    }
  }

  def removeListener(logSourceId: String): Unit = synchronized { listeners.remove(logSourceId) }

  def print(id: String, msg: String, l: LogLevel.Value): Unit = {
    synchronized {
      listeners.get(id) match {
        case Some(listener) => listener.print(id, msg, l)
        case _              => messagesDidNotRecv.addOne(ConsoleMessage(id, msg, l))
      }
    }
  }


  private def getBufferedMessages(logSourceId: String) = {
    val r = messagesDidNotRecv.filter(_.id == logSourceId).toSeq
    messagesDidNotRecv.filterInPlace(_.id != logSourceId)
    r
  }

  override def dispose(): Unit = {}
}
