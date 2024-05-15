package top.scaleda
package idea.lsp

import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider

class ScaledaLspServerSupportProvider extends LspServerSupportProvider {
  override def fileOpened(
      project: Project,
      virtualFile: VirtualFile,
      lspServerStarter: LspServerSupportProvider.LspServerStarter
  ): Unit = {
    if (ScaledaLspServerSupportProvider.isLspSupportedFile(virtualFile)) {
      var success = false
      LspServers.servers.foreach { server =>
        if (!success && server.supportedFile(virtualFile)) {
          lspServerStarter.ensureServerStarted(server.createDescriptor(project))
          success = true
        }
      }
    }
  }
}

object ScaledaLspServerSupportProvider {
  def isLspSupportedFile(file: VirtualFile): Boolean = {
    val maybeVerilog: Boolean = ("(?i)verilog".r findFirstIn file.getFileType.getName).isDefined
    maybeVerilog ||
    file.getFileType.isInstanceOf[VerilogFileType] ||
    file.getFileType.isInstanceOf[SystemVerilogFileType]
  }
}
