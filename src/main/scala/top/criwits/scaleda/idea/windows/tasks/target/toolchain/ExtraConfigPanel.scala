package top.criwits.scaleda
package idea.windows.tasks.target.toolchain

import com.intellij.ui.DocumentAdapter
import idea.windows.tasks.ScaledaRunTargetNode
import kernel.project.config.TargetConfig

import javax.swing._
import javax.swing.event.{DocumentEvent, DocumentListener}
import scala.jdk.javaapi.CollectionConverters

/** Extra config panel
 * <p>
 * For some targets like Xilinx Vivado, there'll be extra options like device part name (stored in a [[Map]]),
 * and these options will be edited here
 */
abstract class ExtraConfigPanel {
  def getStatus: Boolean
  def getComponent: JPanel
  def addEditListener(listener: DocumentListener): Unit

  protected def loadConfig(options: java.util.Map[String, String]): Unit
  def loadTargetSpecificConfig(options: Map[String, String]): Unit = {
    loadConfig(CollectionConverters.asJava(options))
  }

  protected def getConfig: java.util.Map[String, String]
  def getTargetSpecificConfig: Option[Map[String, String]] = {
    val options = getConfig
    if (options.isEmpty) None else
    Some(CollectionConverters.asScala(getConfig).toMap)
  }
}
