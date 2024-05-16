package top.scaleda
package idea.lsp

import idea.application.config.{ScaledaIdeaConfig, ScaledaLspConfig}
import idea.project.io.YmlRootManager
import idea.runner.configuration.ScaledaRunConfiguration
import idea.utils.ScaledaIdeaLogger
import kernel.shell.ScaledaRun
import systemverilog.SystemVerilogFileType
import verilog.VerilogFileType
import verilog.formatter.VeribleAssetsHelper

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

import java.io.File
import scala.collection.mutable.ArrayBuffer

object LspServers {
  trait LspServer {
    private def readConfig: ScaledaLspConfig = ScaledaIdeaConfig.getConfig.lsp
    def supportedFile(file: VirtualFile): Boolean =
      file.getFileType.isInstanceOf[VerilogFileType] ||
        file.getFileType.isInstanceOf[SystemVerilogFileType]
    def commandLine(project: Project): GeneralCommandLine = {
      val commands = ArrayBuffer({
        val path = readConfig.path.getOrElse(name, defaultPath)
        if (path.isEmpty) defaultPath
        else path
      })
      if (supportFileLists && commands(0).nonEmpty) {
        generateFileListsInfo(project) foreach { case (fileListsFilePath, _) =>
          commands += specifyFileListsArgument
          commands += fileListsFilePath.getAbsolutePath
        }
      }
      val finalCommands = commands.toArray
      new GeneralCommandLine(finalCommands: _*)
    }
    def name: String
    def defaultPath: String
    def supportLinter: Boolean           = false
    def supportFormatter: Boolean        = false
    def supportFileLists: Boolean        = false
    def specifyFileListsArgument: String = ""

    def generateFileListsInfo(project: Project): Option[(File, Seq[String])] = {
      if (!supportFileLists) return None
      ScaledaRunConfiguration.getCurrentRunConfiguration(project) flatMap { currentRunConfiguration =>
        currentRunConfiguration.generateRuntime flatMap { rt =>
          val runtime = ScaledaRun.preprocess(rt, writeable = false)
          temporaryFileListsFilePath(name, project) flatMap { fileListsFilePath =>
            Some(
              fileListsFilePath,
              runtime.context
                .get("sourceFiles")
                .map(_.asInstanceOf[Seq[File]])
                .getOrElse({
                  ScaledaIdeaLogger.debug("No source files found in runtime context")
                  Seq.empty
                })
                .map(_.getAbsolutePath)
            )
          }
        }
      }
    }
    def temporaryFileListsFilePath(lspName: String, project: Project): Option[File] = {
      YmlRootManager
        .getInstance(project)
        .getRoots
        .headOption
        .map(_.toScaledaProject)
        .flatMap(
          // This file will be replaced when current run configuration is changed
          _.projectBase.map(p => new File(new File(p, ".lsp"), f"$lspName.f".replace(" ", "")))
        )
    }
    def generateFileLists(project: Project): Unit = {
      val config = ScaledaIdeaConfig.getConfig.lsp
      if (!config.generateFileLists) return
      generateFileListsInfo(project) foreach { case (fileListsFilePath, sourceFiles) =>
        ScaledaIdeaLogger.info(
          s"Generating file lists for $name, file path: $fileListsFilePath, source files: ${sourceFiles.mkString(",")}"
        )
        val fileListsFile = fileListsFilePath
        fileListsFile.getParentFile.mkdirs()
        if (fileListsFile.exists()) fileListsFile.delete()
        fileListsFile.createNewFile()
        fileListsFile.setWritable(true)
        fileListsFile.setReadable(true)
        fileListsFile.setExecutable(false)
        fileListsFile.setWritable(true, false)
        fileListsFile.setReadable(true, false)
        fileListsFile.setExecutable(false, false)
        val writer = new java.io.PrintWriter(fileListsFile)
        sourceFiles.foreach(writer.println)
        writer.close()
      }
    }
  }

  class VeribleLspServer extends LspServer {
    override def name: String = VeribleLspServer.name
    // override def defaultPath: String       = "verible-verilog-ls"
    // default to bundled verible lsp
    override def defaultPath: String       = VeribleAssetsHelper.veribleLsp.getAbsolutePath
    override def supportLinter: Boolean    = true
    override def supportFormatter: Boolean = true

    override def supportFileLists: Boolean        = true
    override def specifyFileListsArgument: String = "--file_list_path"
  }
  object VeribleLspServer {
    val name = "Verible"
  }

  class SvlsLspServer extends LspServer {
    override def name: String           = "Svls"
    override def defaultPath: String    = "svls"
    override def supportLinter: Boolean = true

    // `-f` is svlint argument for file lists, not svls...
    // override def supportFileLists: Boolean        = true
    // override def specifyFileListsArgument: String = "-f"
  }

  class CustomLspServer extends LspServer {
    override def name: String        = CustomLspServer.name
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
