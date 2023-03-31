package top.criwits.scaleda
package verilog.utils

import idea.runner.configuration.ScaledaRunConfiguration
import kernel.project.config.ProjectConfig
import kernel.utils.KernelFileUtils
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

  /** Get Verilog sources for IDEA Editor, will search sources by task selected in IDEA
    */
  def getAllVerilogFiles(project: Project): List[VerilogPSIFileRoot] = {
    // process long tasks in this function will not block IDEA main thread, just do it
    // get configuration selected now
    // this can be null! check first plz!
    val selectedConfiguration = RunManager.getInstance(project).getSelectedConfiguration
    val rt =
      if (selectedConfiguration == null) None
      else {
        val configuration: RunConfiguration = selectedConfiguration.getConfiguration
        configuration match {
          // if it's a valid ScaledaRunProcessHandler
          case configuration: ScaledaRunConfiguration => configuration.generateRuntime
          case _                                      => None
        }
      }
    val defaultSources = ProjectConfig.getConfig().map(c => c.getSourceSet() ++ c.getTestSet()).getOrElse(Set())
    // into synchronized block, only one thread to operate ip cache
    val sources = synchronized {
      // get ALL Scaleda IPs
      val ips: Map[String, ProjectConfig] = rt
        .map(_.task.getAllIps())
        .getOrElse(Map())
      // collects single file ip, in this project and every Scaleda IP
      val singleFileIps = rt
        .map(rt => rt.task.getIpFiles())
        .getOrElse(Set()) ++
        ips.map(p => p._2.getIpFiles(projectBase = Some(p._1))).foldLeft(Set[String]())(_ ++ _)
      KernelFileUtils.doUpdateIpFilesCache(singleFileIps)
      // source set of this project
      val sources: Set[String] = rt match {
        // has runtime, get sources
        case Some(rt) => rt.task.getSourceSet() ++ rt.task.getTestSet()
        // otherwise, get default sources from ProjectConfig
        case None => defaultSources
      }
      // source set from ips
      val ipSources: Set[String] = ips
        .flatMap(c => {
          c._2.getSourceSet(projectBase = Some(c._1)) ++ c._2.getTestSet(projectBase = Some(c._1))
        })
        .toSet
      // search cache directory to get all source files
      val ipFileSources: Set[String] = KernelFileUtils
        .getAllSourceFiles(Set(KernelFileUtils.ipCacheDirectory.getAbsolutePath))
        .map(_.getAbsolutePath)
        .toSet
      sources ++ ipSources ++ ipFileSources
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
