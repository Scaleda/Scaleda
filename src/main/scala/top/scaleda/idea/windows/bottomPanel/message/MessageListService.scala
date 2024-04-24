package top.scaleda
package idea.windows.bottomPanel.message

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import top.scaleda.idea.windows.bottomPanel.ConsoleService
import top.scaleda.idea.windows.bottomPanel.console.{ConsoleReceiver, ConsoleTabManager}
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime
import top.scaleda.kernel.utils.LogLevel

/**
 * Service managing message list
 */
class MessageListService(val project: Project) extends Disposable with ConsoleReceiver {
  private var messageTab: Option[MessageListPanel] = None // should be registered here after the tab is created

  private var currentParser: Option[ScaledaMessageToolchainParser] = None
  private var currentRuntime: Option[ScaledaRuntime] = None

  private var tabManager: Option[ConsoleTabManager] = None

  // register myself to ConsoleService
  project.getService(classOf[ConsoleService]).addListener(this)

  override def dispose(): Unit = {
  }

  override def print(text: String, level: LogLevel.Value): Unit = {
    if (messageTab.isEmpty) return
    currentParser.foreach(parser => {
      currentRuntime.foreach(rt => {
          parser.parse(rt, text, level).foreach(msg => messageTab.get.addMessage(msg))
      })
    })
  }

  override def clear(): Unit = {
    messageTab.foreach(_.clearMessageList())
  }

  def registerMessageTab(tab: MessageListPanel): Unit = {
    messageTab = Some(tab)
  }

  def registerParser(parser: ScaledaMessageToolchainParser): Unit = {
    currentParser = Some(parser)
  }

  def registerRuntime(runtime: ScaledaRuntime): Unit = {
    currentRuntime = Some(runtime)
  }

  def getCurrentRuntime: Option[ScaledaRuntime] = currentRuntime

}
