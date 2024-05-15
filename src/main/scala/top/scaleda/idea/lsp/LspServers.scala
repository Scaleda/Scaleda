package top.scaleda
package idea.lsp

import idea.application.config.{ScaledaIdeaConfig, ScaledaLspConfig}
import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.intellij.platform.lsp.api.customization.LspFormattingSupport

object LspServers {

  trait LspServer {
    def supportedFile(file: VirtualFile): Boolean
    def commandLine: GeneralCommandLine = new GeneralCommandLine({
      val path = readConfig.path.getOrElse(name, defaultPath)
      if (path.isEmpty) defaultPath
      else path
    })
    def name: String
    def defaultPath: String
    def supportLinter: Boolean               = false
    def supportFormatter: Boolean            = false
    def supportFileLists: Boolean            = false
    private def readConfig: ScaledaLspConfig = ScaledaIdeaConfig.getConfig.lsp
    def createDescriptor(project: Project): Option[ProjectWideLspServerDescriptor] = {
      try {
        Some(new ProjectWideLspServerDescriptor(project, name) {
          override def isSupportedFile(file: VirtualFile): Boolean = supportedFile(file)
          override def createCommandLine(): GeneralCommandLine     = commandLine

          override def getLspFormattingSupport: LspFormattingSupport = {
            if (supportFormatter) {
              // FIXME: check it
              new LspFormattingSupport {
                override def shouldFormatThisFileExclusivelyByServer(
                    file: VirtualFile,
                    ideCanFormatThisFileItself: Boolean,
                    serverExplicitlyWantsToFormatThisFile: Boolean
                ): Boolean =
                  serverExplicitlyWantsToFormatThisFile
              }
            } else {
              super.getLspFormattingSupport
            }
          }

          override def getLspGoToDefinitionSupport: Boolean = supportLinter
          override def getLspHoverSupport: Boolean          = supportLinter
        })
      } catch {
        case _: Throwable => None
      }
    }
  }

  object VeribleLspServerDescriptor extends LspServer {
    override def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]

    override def name: String = "Verible"

    override def defaultPath: String = "verible-verilog-ls"
  }

  object SvlsLspServerDescriptor extends LspServer {
    override def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]

    override def name: String = "Svls"

    override def defaultPath: String = "svls"
  }

  object CustomLspServerDescriptor extends LspServer {
    override def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]

    override def name: String = "Custom"

    override def defaultPath: String = ""
  }

  def servers: Seq[LspServer] = Seq(
    VeribleLspServerDescriptor,
    SvlsLspServerDescriptor,
    CustomLspServerDescriptor
  )
}
