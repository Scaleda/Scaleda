package top.criwits.scaleda
package verilog.utils

import idea.runner.configuration.ScaledaRunConfiguration
import kernel.project.config.ProjectConfig
import verilog.VerilogPSIFileRoot

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
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
    // get configuration selected now
    val configuration: RunConfiguration = RunManager.getInstance(project).getSelectedConfiguration.getConfiguration
    val defaultSources                  = ProjectConfig.getConfig().map(c => c.getSourceSet() ++ c.getTestSet()).getOrElse(Set())
    val sources: Set[String] = configuration match {
      // if it's a valid ScaledaRunProcessHandler, get sources from it
      case configuration: ScaledaRunConfiguration =>
        configuration.generateRuntime
          .map(rt => {
            rt.task.getSourceSet() ++ rt.task.getTestSet()
          })
          .getOrElse(defaultSources)
      // otherwise, get default sources from ProjectConfig
      case _ => defaultSources
    }
    // may reach `pwd`
    // TODO: apply file search paths... example: a.v => test/a.v
    val sourceFiles        = sources.map(new File(_)).filter(_.exists())
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
      val v = PsiTreeUtil
        .findChildrenOfAnyType(
          psiManager.findDirectory(LocalFileSystem.getInstance().findFileByIoFile(f)),
          classOf[VerilogPSIFileRoot]
        )
        .asScala
      result ++= v
    })
    result.toList
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
