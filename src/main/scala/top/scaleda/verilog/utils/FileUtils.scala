package top.scaleda
package verilog.utils

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFile}
import com.intellij.psi.PsiManager
import top.scaleda.idea.project.io.YmlRootManager
import top.scaleda.idea.runner.configuration.ScaledaRunConfiguration
import top.scaleda.kernel.project.ScaledaProject
import top.scaleda.kernel.project.config.ProjectConfig
import top.scaleda.kernel.utils.KernelFileUtils
import top.scaleda.verilog.VerilogPSIFileRoot

import java.io.{File, FilenameFilter}
import scala.collection.mutable.ArrayBuffer
import scala.jdk.javaapi.CollectionConverters

object FileUtils {
  /** Get Verilog sources for IDEA Editor for current project
    */
  def getAllVerilogFiles(project: Project): List[VerilogPSIFileRoot] = {
    // get project
    implicit val scaledaProject = YmlRootManager.getInstance(project).getRoots.map(_.toScaledaProject).head
    val defaultSources = ProjectConfig.getConfig.map(c => c.getSourceSet ++ c.getTestSet).getOrElse(Set())

    val sourceFiles        = defaultSources.map(new File(_)).filter(_.exists())
    val sourceRegularFiles = sourceFiles.filter(_.isFile)
    val sourceDirectories  = sourceFiles.filter(_.isDirectory)
    val result             = ArrayBuffer[VerilogPSIFileRoot]()

    // get configuration
//    val selectedConfiguration = RunManager.getInstance(project).getSelectedConfiguration
//    val rt =
//      if (selectedConfiguration == null) None
//      else {
//        val configuration: RunConfiguration = selectedConfiguration.getConfiguration
//        configuration match {
//          case configuration: ScaledaRunConfiguration => configuration.generateRuntime
//          case _                                      => None
//        }
//      }


    // process long tasks in this function will not block IDEA main thread, just do it
    // get configuration selected now
    // this can be null! check first plz!
    // into synchronized block, only one thread to operate ip cache
//    val sources = synchronized {
//      // get ALL Scaleda IPs, (absPath, ProjectConfig)
//      val ips: Map[String, ProjectConfig] = rt
//        .map(_.task.getAllIps.filter(_._2.exports.nonEmpty))
//        .getOrElse(Map())
//      val ipStubsSources: Set[String] =
//        rt.map(rt => KernelFileUtils.doUpdateStubCacheFromRuntime(rt).map(_.getAbsolutePath).toSet).getOrElse(Set())
//      // source set of this project
//      val sources: Set[String] = rt match {
//        // has runtime, get sources
//        case Some(rt) => rt.task.getSourceSet ++ rt.task.getTestSet
//        // otherwise, get default sources from ProjectConfig
//        case None => defaultSources
//      }
//      // source set from ips
//      val ipSources: Set[String] = ips
//        .flatMap(c => {
//          c._2.getSourceSet(project = ScaledaProject.getTemporalProject(c._1)) ++ c._2
//            .getTestSet(project = ScaledaProject.getTemporalProject(c._1))
//        })
//        .toSet
//      sources ++ ipSources ++ ipStubsSources
//    }
//    // may reach `pwd`
//    // TODO: apply file search paths... example: a.v => test/a.v
//

    // append files from sourceRegularFiles
    val psiManager = PsiManager.getInstance(project)
    def doParseFile(f: File) = {
      if (f != null) {
        val d = LocalFileSystem.getInstance().findFileByIoFile(f)
        if (d != null) {
          val foundFile = psiManager.findFile(d)
          foundFile match {
            case f: VerilogPSIFileRoot => result += f
            case _                     =>
          }
        }
      }
    }
    sourceRegularFiles.foreach(f => doParseFile(f))

    def doParseDir(dir: File): Unit = {
      val allFiles = dir.listFiles()
      allFiles.foreach(file => {
        if (file == null || !file.exists()) return
        if (file.isFile) doParseFile(file)
        // do search recursively, DFS
        else if (file.isDirectory) doParseDir(file)
      })
    }
    sourceDirectories.foreach(doParseDir)

    if (result.isEmpty) {
      // cannot get any sources, try to load all sources from project base
      if (project.getProjectFile != null && project.getProjectFile.isInLocalFileSystem)
        doParseDir(new File(project.getBasePath))
    }
    result.toList
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
