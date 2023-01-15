package top.criwits.scaleda
package idea.runner.task.edit.target

import idea.runner.task.edit.{EditDialogProvider, EditTargetDialogWrapper}
import idea.runner.task.edit.target.toolchain.{EmptyConfigPanel, ExtraConfigPanel, VivadoConfigPanel}
import kernel.project.config.{ProjectConfig, TargetConfig}
import kernel.toolchain.Toolchain

import com.intellij.openapi.ui.Splitter
import com.intellij.ui.DocumentAdapter
import com.intellij.util.ui.JBUI
import org.jdesktop.swingx.prompt.PromptSupport
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.utils.MainLogger

import java.awt.{BorderLayout, Color, Font}
import java.awt.event.{ItemEvent, ItemListener}
import javax.swing.JPanel
import javax.swing.event.DocumentEvent

class EditTargetDialog(config: Option[TargetConfig], parent: EditTargetDialogWrapper) extends JPanel with EditDialogProvider[TargetConfig] {

  // use border layout
  setLayout(new BorderLayout())

  /** UPPER PANEL */
  val upperPanel = new TargetBasicPanel
  add(upperPanel.mainPanel, BorderLayout.NORTH)

  // add toolchain types to the combo box
  // by getting index we get the toolchain type
  private val toolchainList = Toolchain.toolchains.toList
  toolchainList.foreach(f => upperPanel.toolchainField.addItem(f._2._1))
  upperPanel.toolchainField.setSelectedIndex(0) // should work??

  private var toolchainType = toolchainList.head._1
  private var name: String = ""
  private var topModule = ""

  // listeners
  upperPanel.nameField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = {
      name = upperPanel.nameField.getText
      parent.updateOKStatus()
    }
  })

  upperPanel.topModuleField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = {
      topModule = upperPanel.topModuleField.getText
      parent.updateOKStatus()
    }
  })

  upperPanel.toolchainField.addItemListener((e: ItemEvent) => if (e.getStateChange == ItemEvent.SELECTED) {
    toolchainType = toolchainList(upperPanel.toolchainField.getSelectedIndex)._1
    updateConfigPanel()
    parent.updateOKStatus()
  })

  /** LOWER PANEL */
  private val lowerPanel = new JPanel()
  lowerPanel.setLayout(new BorderLayout())
  add(lowerPanel, BorderLayout.CENTER)

  private var configPanel: ExtraConfigPanel = new EmptyConfigPanel
  private def updateConfigPanel() = {
    configPanel = toolchainType match {
      case "vivado" => new VivadoConfigPanel
      case _ => new EmptyConfigPanel
    }
    configPanel.addEditListener(new DocumentAdapter {
      override def textChanged(e: DocumentEvent): Unit = parent.updateOKStatus()
    })
    lowerPanel.removeAll()
    lowerPanel.add(configPanel.getComponent, BorderLayout.CENTER)
    parent.updateOKStatus()
    revalidate()
  }


  // Load initial config, if config exists
  config match {
    case Some(config) =>
      upperPanel.nameField.setText(config.name)
      config.topModule match {
        case Some(topModule) => upperPanel.topModuleField.setText(topModule)
        case None =>
      }
      upperPanel.toolchainField.setSelectedIndex(Toolchain.toolchains.zipWithIndex.filter(_._1._1 == config.toolchain).head._2) // FIXME: if not exist?
      config.options match {
        case Some(options) => configPanel.loadTargetSpecificConfig(options)
        case None =>
      }
    case None =>
      // do nothing!
  }
  updateConfigPanel()

  // check top module status
  private val projectConfig = ProjectConfig.getConfig().get // FIXME
  private val projectTopModuleValid = projectConfig.topModule.isDefined
  if (projectTopModuleValid) {
    // add a prompt!
    PromptSupport.setPrompt(ScaledaBundle.message("windows.edit.target.inherited_top_module",
      projectConfig.topModule.get),
      upperPanel.topModuleField)
    PromptSupport.setFontStyle(Font.ITALIC, upperPanel.topModuleField)
    PromptSupport.setBackground(new Color(0, 0, 0, 0), upperPanel.topModuleField)
  }

  override def validateConfig: Boolean = configPanel.getStatus && !name.isBlank

  override def getMainPanel: JPanel = this

  override def getData: TargetConfig = {
    val targetConfig = TargetConfig(
      name = name,
      topModule = {
        if (topModule.isBlank) None else Some(topModule)
      },
      toolchain = toolchainType,
      options = if (configPanel.getTargetSpecificConfig.isEmpty) None else Some(configPanel.getTargetSpecificConfig),
      tasks = config match {
        case Some(config) => config.tasks
        case None => Array()
      }
    )
    targetConfig
  }
}
