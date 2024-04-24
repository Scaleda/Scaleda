package top.scaleda
package idea.windows.bottomPanel

import idea.windows.bottomPanel.console.ConsoleReceiver
import kernel.utils.LogLevel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project

import scala.collection.mutable

/** Service managing console outputs as well as message list
  */
class ConsoleService(val project: Project) extends Disposable {
  private val listeners = new mutable.HashSet[ConsoleReceiver]

  def addListener(receiver: ConsoleReceiver): Unit = {
    synchronized {
      listeners.add(receiver)
    }
  }

  def removeAllListeners(): Unit = synchronized { listeners.clear() }
  def removeListener(receiver: ConsoleReceiver): Unit =
    synchronized {
      listeners.remove(receiver)
    }

  def print(msg: String, level: LogLevel.Value): Unit = {
    synchronized {
      listeners.foreach(_.print(msg, level))
    }
  }

  def clear(): Unit = {
    synchronized {
      listeners.foreach(_.clear())
    }
  }

  override def dispose(): Unit = {
    removeAllListeners()
  }
}
