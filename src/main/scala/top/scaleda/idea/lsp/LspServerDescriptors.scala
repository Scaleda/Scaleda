package top.scaleda
package idea.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor

object LspServerDescriptors {

  trait LspServerDescriptor extends LspServers.LspServer {
    def createDescriptor(project: Project): ProjectWideLspServerDescriptor = {
      new ProjectWideLspServerDescriptor(project, name) {
        override def isSupportedFile(file: VirtualFile): Boolean = supportedFile(file)
        override def createCommandLine(): GeneralCommandLine     = commandLine(project)

        // override def getLspFormattingSupport: LspFormattingSupport = {
        //   if (supportFormatter) {
        //     // FIXME: check it
        //     new LspFormattingSupport {
        //       override def shouldFormatThisFileExclusivelyByServer(
        //           file: VirtualFile,
        //           ideCanFormatThisFileItself: Boolean,
        //           serverExplicitlyWantsToFormatThisFile: Boolean
        //       ): Boolean =
        //         serverExplicitlyWantsToFormatThisFile
        //     }
        //   } else {
        //     super.getLspFormattingSupport
        //   }
        // }

        // override def getLspGoToDefinitionSupport: Boolean = supportLinter
        // override def getLspHoverSupport: Boolean          = supportLinter
      }
    }
  }

  class VeribleLspServerDescriptor extends LspServers.VeribleLspServer with LspServerDescriptor

  class SvlsLspServerDescriptor extends LspServers.SvlsLspServer with LspServerDescriptor

  class CustomLspServerDescriptor extends LspServers.CustomLspServer with LspServerDescriptor

  def serverDescriptors: Seq[LspServerDescriptor] = Seq(
    new VeribleLspServerDescriptor,
    new SvlsLspServerDescriptor,
    new CustomLspServerDescriptor
  )
}
