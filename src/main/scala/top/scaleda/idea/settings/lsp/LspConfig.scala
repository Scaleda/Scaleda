package top.scaleda
package idea.settings.lsp

import idea.ScaledaBundle
import idea.application.config.{ScaledaIdeaConfig, ScaledaLspConfig}
import idea.lsp.LspServers
import idea.lsp.LspServers.CustomLspServerDescriptor

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.{ComboBox, TextBrowseFolderListener, TextFieldWithBrowseButton}
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder

import javax.swing.{JComponent, JPanel}

class LspConfig extends SearchableConfigurable {

  private def configNow: ScaledaLspConfig = ScaledaIdeaConfig.getConfig.lsp

  override def getId: String = LspConfig.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.lsp.name")

  private val tool = new ComboBox[String]()
  LspServers.servers.foreach(t =>
    if (t.name == CustomLspServerDescriptor.name) tool.addItem(ScaledaBundle.message("settings.lsp.label.tool.custom"))
    else tool.addItem(t.name)
  )
  private val path = new TextFieldWithBrowseButton()
  path.addBrowseFolderListener(
    new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor())
  )
  LspServers.servers.foreach(server =>
    if (server.name == configNow.tool) {
      tool.setSelectedItem(server.name)
      path.setText(configNow.path.getOrElse(server.name, server.defaultPath))
    } else if (server.name == CustomLspServerDescriptor.name && configNow.tool == CustomLspServerDescriptor.name) {
      tool.setSelectedItem(ScaledaBundle.message("settings.lsp.label.tool.custom"))
      path.setText(configNow.path.getOrElse(CustomLspServerDescriptor.name, "custom-lsp"))
    }
  )
  tool.addItemListener(_ => {
    val server = LspServers.servers
      .find(s =>
        s.name == tool.getSelectedItem ||
          (s.name == CustomLspServerDescriptor.name && tool.getSelectedItem == ScaledaBundle
            .message("settings.lsp.label.tool.custom"))
      )
      .get
    path.setText(
      configNow.path.getOrElse(server.name, server.defaultPath)
    )
  })

  private val mainPanel = new JPanel()
  mainPanel.add(
    FormBuilder
      .createFormBuilder()
      // .setAlignLabelOnRight(false)
      // .setHorizontalGap(UIUtil.DEFAULT_HGAP)
      // .setVerticalGap(UIUtil.DEFAULT_VGAP)
      .addLabeledComponent(new JBLabel(ScaledaBundle.message("settings.lsp.label.tool")), tool)
      .addLabeledComponent(new JBLabel(ScaledaBundle.message("settings.lsp.label.path")), path)
      .getPanel
  )

  override def createComponent(): JComponent = mainPanel

  override def isModified: Boolean = dumpConfig != configNow

  private def dumpConfig = {
    val now = configNow
    val toolName =
      if (tool.getItem == ScaledaBundle.message("settings.lsp.label.tool.custom")) CustomLspServerDescriptor.name
      else tool.getItem
    now.copy(
      tool = toolName,
      path = now.path ++ Map(toolName -> path.getText)
    )
  }

  override def apply(): Unit = {
    ScaledaIdeaConfig.saveConfig(ScaledaIdeaConfig.getConfig.copy(lsp = dumpConfig))
  }
}

object LspConfig {
  val SETTINGS_ID = "top.scaleda.idea.settings.lsp.LspConfig"
}
