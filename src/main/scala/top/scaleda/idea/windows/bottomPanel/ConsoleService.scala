package top.scaleda
package idea.windows.bottomPanel

import idea.utils.ScaledaIdeaLogger
import idea.windows.bottomPanel.console.{ConsoleReceiver, ConsoleTabManager}
import idea.windows.bottomPanel.message.MessageListService
import kernel.utils.LogLevel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

import scala.collection.mutable

/** Service managing console outputs as well as message list
  */
class ConsoleService(val project: Project) extends Disposable {
  private val listeners = new mutable.HashMap[String, mutable.HashSet[ConsoleReceiver]]

  private var consoleTabManager: Option[ConsoleTabManager] = None

  def setConsoleTabManager(manager: ConsoleTabManager): Unit = {
    Disposer.register(this, manager)
    consoleTabManager = Some(manager)
  }

  def getConsoleTabManager: Option[ConsoleTabManager] = consoleTabManager

  def addListener(receiver: ConsoleReceiver, key: String): Unit = {
    synchronized {
      listeners.get(key) match {
        case Some(list) =>
          ScaledaIdeaLogger.debug(s"addListener append: $receiver, key: $key")
          if (!list.add(receiver)) {
            ScaledaIdeaLogger.debug(s"addListener failed to append: $receiver, key: $key")
          }
        case None =>
          ScaledaIdeaLogger.debug(s"addListener create list: $receiver, key: $key")
          listeners.put(key, mutable.HashSet(receiver))
      }
      ScaledaIdeaLogger.debug(s"addListener created list: ${listeners(key).mkString(", ")}")
    }
  }

  def removeAllListeners(): Unit = synchronized {
    ScaledaIdeaLogger.debug("removeAllListeners")
    listeners.clear()
  }
  def removeListener(receiver: ConsoleReceiver): Unit = {
    synchronized {
      listeners.find(_._2.exists(_ == receiver)).foreach { r =>
        r._2.remove(receiver)
        ScaledaIdeaLogger.debug(s"removeListener in map: $receiver, key: ${r._1}")
      }
    }
  }

  def removeListenerByKey(key: String): Option[mutable.HashSet[ConsoleReceiver]] =
    synchronized {
      val r = listeners.remove(key)
      ScaledaIdeaLogger.debug(s"removeListenerByKey: $key, $r")
      r
    }

  def queryRunningIdByDisplayName(displayName: String): Seq[String] =
    listeners.filter(_._2.exists(_.getDisplayName == displayName)).keys.toSeq

  def print(msg: String, level: LogLevel.Value, key: String): Boolean = {
    var success = false
    synchronized {
      listeners
        .get(key)
        .foreach(_.foreach(r => {
          r.print(msg, level)
          success = true
        }))
    }
    if (key != ScaledaIdeaLogger.getId)
      Option(project.getService(classOf[MessageListService])).foreach(_.print(msg, level))

    if (!success) {
      ScaledaIdeaLogger.debug(s"Failed to print to console: [$key] $msg")
      ScaledaIdeaLogger.debug(
        s"Listeners: ${listeners.keys.mkString(", ")} " +
          s"(${listeners.getOrElse(key, Seq()).mkString(", ")}) " +
          s"len ${listeners.get(key).map(_.size).getOrElse(-1)}"
      )
    }
    success
  }

  def clearAll(): Unit = {
    synchronized {
      listeners.values.foreach(_.clear())
    }
  }

  def clearById(key: String): Unit = {
    synchronized {
      listeners.get(key).foreach(_.foreach(_.clear()))
    }
  }

  override def dispose(): Unit = {
    removeAllListeners()
  }
}
