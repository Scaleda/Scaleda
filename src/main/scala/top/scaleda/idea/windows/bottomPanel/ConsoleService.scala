package top.scaleda
package idea.windows.bottomPanel

import idea.utils.ScaledaIdeaLogger
import idea.windows.bottomPanel.console.{ConsoleReceiver, ConsoleTabManager}
import kernel.utils.LogLevel

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

import scala.collection.mutable

/** Service managing console outputs as well as message list
  */
class ConsoleService(val project: Project) extends Disposable {
  // private val listeners                                    = new mutable.HashSet[ConsoleReceiver]
  private val listenersDirected = new mutable.HashMap[String, mutable.HashSet[ConsoleReceiver]]

  private var consoleTabManager: Option[ConsoleTabManager] = None

  def setConsoleTabManager(manager: ConsoleTabManager): Unit = {
    Disposer.register(this, manager)
    consoleTabManager = Some(manager)
  }

  def getConsoleTabManager: Option[ConsoleTabManager] = consoleTabManager

  def addListener(receiver: ConsoleReceiver, key: String): Unit = {
    synchronized {
      // key match {
      //   case Some(k) =>
      //     // listenersDirected.getOrElseUpdate(k, mutable.ArrayBuffer()).addOne(receiver)
      //     listenersDirected.get(k) match {
      //       case Some(list) => list.addOne(receiver)
      //       case None       => listenersDirected.put(k, mutable.HashSet(receiver))
      //     }
      //   case None => listeners.add(receiver)
      // }
      listenersDirected.get(key) match {
        case Some(list) =>
          ScaledaIdeaLogger.debug(s"addListener append: $receiver, key: $key")
          if (!list.add(receiver)) {
            ScaledaIdeaLogger.debug(s"addListener failed to append: $receiver, key: $key")
          }
        case None =>
          ScaledaIdeaLogger.debug(s"addListener create list: $receiver, key: $key")
          listenersDirected.put(key, mutable.HashSet(receiver))
      }
      ScaledaIdeaLogger.debug(s"addListener created list: ${listenersDirected(key).mkString(", ")}")
    }
  }

  def removeAllListeners(): Unit = synchronized {
    ScaledaIdeaLogger.debug("removeAllListeners")
    // listeners.clear()
    listenersDirected.clear()
  }
  def removeListener(receiver: ConsoleReceiver): Unit = {
    synchronized {
      // if (!listeners.remove(receiver)) {
      listenersDirected.find(_._2.exists(_ == receiver)).foreach { r =>
        r._2.remove(receiver)
        ScaledaIdeaLogger.debug(s"removeListener in map: $receiver, key: ${r._1}")
      }
      // } else {
      //   ScaledaIdeaLogger.debug("removeListener in list: " + receiver)
      // }
    }
  }

  def removeListenerByKey(key: String): Option[mutable.HashSet[ConsoleReceiver]] =
    synchronized {
      val r = listenersDirected.remove(key)
      ScaledaIdeaLogger.debug(s"removeListenerByKey: $key, $r")
      r
    }

  def queryRunningIdByDisplayName(displayName: String): Seq[String] =
    listenersDirected.filter(_._2.exists(_.getDisplayName == displayName)).keys.toSeq

  def print(msg: String, level: LogLevel.Value, key: String): Boolean = {
    var success = false
    synchronized {
      // key match {
      //   case Some(k) =>
      //     listenersDirected
      //       .get(k)
      //       .foreach(_.foreach(r => {
      //         r.print(msg, level)
      //         success = true
      //       }))
      //   case None =>
      //     listeners.foreach(r => {
      //       r.print(msg, level)
      //       success = true
      //     })
      // }
      listenersDirected
        .get(key)
        .foreach(_.foreach(r => {
          r.print(msg, level)
          success = true
        }))
    }
    if (!success) {
      ScaledaIdeaLogger.debug(s"Failed to print to console: [$key] $msg")
      // key match {
      //   case Some(k) =>
      ScaledaIdeaLogger.debug(
        s"Listeners: ${listenersDirected.keys.mkString(", ")} " +
          s"(${listenersDirected.getOrElse(key, Seq()).mkString(", ")}) " +
          s"len ${listenersDirected.get(key).map(_.size).getOrElse(-1)}"
      )
      //   case None =>
      //     ScaledaIdeaLogger.debug(s"Listeners: ${listeners.mkString(", ")}")
      // }
    }
    success
  }

  def clearAll(): Unit = {
    synchronized {
      // listeners.foreach(_.clear())
      listenersDirected.values.foreach(_.clear())
    }
  }

  def clearById(key: String): Unit = {
    synchronized {
      listenersDirected.get(key).foreach(_.foreach(_.clear()))
    }
  }

  override def dispose(): Unit = {
    removeAllListeners()
  }
}
