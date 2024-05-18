package top.scaleda
package idea.lsp

import idea.application.config.ScaledaIdeaConfig
import idea.settings.lsp.LspConfigSetting
import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider

// class ScaledaLspServerSupportProvider extends LspServerSupportProvider {}

object ScaledaLspServerSupportProviderHelper {
  def isLspSupportedFile(file: VirtualFile): Boolean = {
    val maybeVerilog: Boolean = ("(?i)verilog".r findFirstIn file.getFileType.getName).isDefined
    maybeVerilog ||
    file.getFileType.isInstanceOf[VerilogFileType] ||
    file.getFileType.isInstanceOf[SystemVerilogFileType]
  }

  def fileOpened(
      project: Project,
      virtualFile: VirtualFile,
      lspServerStarter: LspServerSupportProvider.LspServerStarter
  ): Unit = {
    if (!LspConfigSetting.isLspSupport)
      return
    if (ScaledaLspServerSupportProviderHelper.isLspSupportedFile(virtualFile)) {
      val config = ScaledaIdeaConfig.getConfig.lsp
      LspServerDescriptors.serverDescriptors
        .find(server => server.name == config.tool && server.supportedFile(virtualFile))
        .foreach { server =>
          if (server.commandLine(project).getCommandLineString.isEmpty) {
            // TODO: show a dialog to ask user to set the path
          } else {
            server.generateFileLists(project)
            lspServerStarter.ensureServerStarted(server.createDescriptor(project))
          }
        }
    }
  }
}
