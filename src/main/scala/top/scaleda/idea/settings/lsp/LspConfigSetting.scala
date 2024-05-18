package top.scaleda
package idea.settings.lsp

import idea.ScaledaBundle
import idea.application.config.{ScaledaIdeaConfig, ScaledaLspConfig}
import idea.lsp.LspServers
import idea.lsp.LspServers.CustomLspServer

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.{ComboBox, TextBrowseFolderListener, TextFieldWithBrowseButton}
import com.intellij.ui.components.{JBLabel, JBPanelWithEmptyText}
import com.intellij.util.ui.{FormBuilder, JBUI}

import javax.swing.{JCheckBox, JComponent, JPanel}

class LspConfigSetting extends SearchableConfigurable {

  private def configNow: ScaledaLspConfig = ScaledaIdeaConfig.getConfig.lsp

  override def getId: String = LspConfigSetting.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.lsp.name")

  private val tool = new ComboBox[String]()
  LspServers.servers.foreach(t =>
    if (t.name == CustomLspServer.name) tool.addItem(ScaledaBundle.message("settings.lsp.label.tool.custom"))
    else tool.addItem(t.name)
  )
  private val generateFileLists = new JCheckBox()
  private val path              = new TextFieldWithBrowseButton()
  path.addBrowseFolderListener(
    new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor())
  )
  private val configInit = configNow
  LspServers.servers.foreach(server =>
    if (server.name == configInit.tool) {
      tool.setSelectedItem(server.name)
      path.setText(configInit.path.getOrElse(server.name, server.defaultPath))
    } else if (server.name == CustomLspServer.name && configInit.tool == CustomLspServer.name) {
      tool.setSelectedItem(ScaledaBundle.message("settings.lsp.label.tool.custom"))
      path.setText(configInit.path.getOrElse(CustomLspServer.name, "custom-lsp"))
    }
  )
  generateFileLists.setSelected(configInit.generateFileLists)
  tool.addItemListener(_ => {
    val server = LspServers.servers
      .find(s =>
        s.name == tool.getSelectedItem ||
          (s.name == CustomLspServer.name && tool.getSelectedItem == ScaledaBundle
            .message("settings.lsp.label.tool.custom"))
      )
      .get
    path.setText(
      configNow.path.getOrElse(server.name, server.defaultPath)
    )
  })

  override def createComponent(): JComponent = {
    if (LspConfigSetting.isLspSupport) {
      FormBuilder
        .createFormBuilder()
        // .setAlignLabelOnRight(false)
        // .setHorizontalGap(UIUtil.DEFAULT_HGAP)
        // .setVerticalGap(UIUtil.DEFAULT_VGAP)
        .addLabeledComponent(new JBLabel(ScaledaBundle.message("settings.lsp.label.tool")), tool)
        .addLabeledComponent(
          new JBLabel(ScaledaBundle.message("settings.lsp.label.generate_file_lists")),
          generateFileLists
        )
        .addLabeledComponent(new JBLabel(ScaledaBundle.message("settings.lsp.label.path")), path)
        .addComponentFillVertically(new JPanel, 0)
        .getPanel
    } else {
      new JBPanelWithEmptyText()
        .withEmptyText(ScaledaBundle.message("settings.lsp.unsupported"))
        .withBorder(JBUI.Borders.emptyLeft(6))
    }
  }

  override def isModified: Boolean = dumpConfig != configNow

  private def dumpConfig = {
    val now = configNow
    val toolName =
      if (tool.getItem == ScaledaBundle.message("settings.lsp.label.tool.custom")) CustomLspServer.name
      else tool.getItem
    now.copy(
      tool = toolName,
      generateFileLists = generateFileLists.isSelected,
      path = now.path ++ Map(toolName -> path.getText)
    )
  }

  override def apply(): Unit = {
    ScaledaIdeaConfig.saveConfig(ScaledaIdeaConfig.getConfig.copy(lsp = dumpConfig))
  }
}

object LspConfigSetting {
  val SETTINGS_ID = "top.scaleda.idea.settings.lsp.LspConfig"

  // def isLspSupport: Boolean = !PlatformUtils.isCommunityEdition
  def isLspSupport: Boolean = {
    // val packageName = "com.intellij.platform.lsp"
    val packageName = "com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor"
    // val packageUrl = packageName.replace('.', '/')
    // getClass.getClassLoader.getResource(packageUrl) != null
    try {
      Class.forName(packageName) != null
    } catch {
      case _: ClassNotFoundException => false
    }
  }
}
