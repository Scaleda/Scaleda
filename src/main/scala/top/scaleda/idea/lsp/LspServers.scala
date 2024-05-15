package top.scaleda
package idea.lsp

import idea.application.config.{ScaledaIdeaConfig, ScaledaLspConfig}
import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.vfs.VirtualFile

object LspServers {
  trait LspServer {
    private def readConfig: ScaledaLspConfig = ScaledaIdeaConfig.getConfig.lsp
    def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]
    def commandLine: GeneralCommandLine = new GeneralCommandLine({
      val path = readConfig.path.getOrElse(name, defaultPath)
      if (path.isEmpty) defaultPath
      else path
    })
    def name: String
    def defaultPath: String
    def supportLinter: Boolean    = false
    def supportFormatter: Boolean = false
    def supportFileLists: Boolean = false
  }

  class VeribleLspServer extends LspServer {
    override def name: String = "Verible"
    override def defaultPath: String = "verible-verilog-ls"
  }

  class SvlsLspServer extends LspServer {
    override def name: String = "Svls"
    override def defaultPath: String = "svls"
  }

  class CustomLspServer extends LspServer {
    override def name: String = CustomLspServer.name
    override def defaultPath: String = ""
  }
  object CustomLspServer {
    val name = "Custom"
  }


  val servers: Seq[LspServer] = Seq(
    new VeribleLspServer,
    new SvlsLspServer,
    new CustomLspServer
  )
}
