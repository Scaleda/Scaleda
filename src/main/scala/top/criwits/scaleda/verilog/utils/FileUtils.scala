package top.criwits.scaleda
package verilog.utils

import idea.runner.ScaledaRunProcessHandler
import kernel.project.config.ProjectConfig
import verilog.VerilogPSIFileRoot

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil

import java.io.File
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._
import scala.jdk.javaapi.CollectionConverters

object FileUtils {
  def getAllVerilogFiles(project: Project, test: Boolean = false): List[VerilogPSIFileRoot] = {
    // get selected RunConfiguration handler
    val selectedContent         = RunContentManager.getInstance(project).getSelectedContent
    val handler: ProcessHandler = if (selectedContent == null) null else selectedContent.getProcessHandler
    val sources: Set[String] = handler match {
      // if it's a valid ScaledaRunProcessHandler, get sources from it
      case handler: ScaledaRunProcessHandler =>
        val rt = handler.rt
        rt.task.getSourceSet ++ rt.task.getTestSet
      // otherwise, get default sources from ProjectConfig
      case _ => ProjectConfig.getConfig().map(c => c.getSourceSet ++ c.getTestSet).getOrElse(Set())
    }
    // may reach `pwd`
    val projectBase = ProjectConfig.projectBase.getOrElse("")
    // TODO: apply file search paths... example: a.v => test/a.v
    val sourceFiles        = sources.map(s => new File(projectBase, s)).filter(_.exists())
    val sourceRegularFiles = sourceFiles.filter(_.isFile)
    val sourceDirectories  = sourceFiles.filter(_.isDirectory)
    val result             = ArrayBuffer[VerilogPSIFileRoot]()

    // append files from sourceRegularFiles
    val psiManager = PsiManager.getInstance(project)
    sourceRegularFiles.foreach(f => {
      PsiTreeUtil.getChildOfType(
        psiManager.findFile(LocalFileSystem.getInstance().findFileByIoFile(f)),
        classOf[VerilogPSIFileRoot]
      ) match {
        case null        =>
        case psiFileRoot => result += psiFileRoot
      }
    })
    // search and append files from sourceDirectories
    sourceDirectories.foreach(f => {
      result ++= PsiTreeUtil
        .findChildrenOfAnyType(
          psiManager.findDirectory(LocalFileSystem.getInstance().findFileByIoFile(f)),
          classOf[VerilogPSIFileRoot]
        )
        .asScala
    })
    result.toList
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
