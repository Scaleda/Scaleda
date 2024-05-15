package top.scaleda
package idea.lsp

import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor

object LspServers {

  trait LspServer {
    def supportedFile(file: VirtualFile): Boolean
    def commandLine(): GeneralCommandLine
    def displayName: String
    def createDescriptor(project: Project): ProjectWideLspServerDescriptor =
      new ProjectWideLspServerDescriptor(project, displayName) {
        override def isSupportedFile(file: VirtualFile): Boolean = supportedFile(file)
        override def createCommandLine(): GeneralCommandLine     = commandLine()
      }
  }

  object VeribleLspServerDescriptor extends LspServer {
    override def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]

    override def commandLine(): GeneralCommandLine = new GeneralCommandLine("verible-verilog-ls")

    override def displayName: String = "Verible"
  }

  object SvlsLspServerDescriptor extends LspServer {
    override def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]

    override def commandLine(): GeneralCommandLine = new GeneralCommandLine("svls")

    override def displayName: String = "Svls"
  }

  val servers: Seq[LspServer] = Seq(
    VeribleLspServerDescriptor,
    SvlsLspServerDescriptor
  )
}
