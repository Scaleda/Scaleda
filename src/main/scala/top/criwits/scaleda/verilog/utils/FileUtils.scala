package top.criwits.scaleda
package verilog.utils

import idea.project.IdeaManifestManager
import idea.runner.configuration.ScaledaRunConfiguration
import kernel.project.ProjectManifest
import kernel.project.config.ProjectConfig
import kernel.utils.KernelFileUtils
import verilog.VerilogPSIFileRoot

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager

import java.io.{File, FilenameFilter}
import scala.collection.mutable.ArrayBuffer
import scala.jdk.javaapi.CollectionConverters

object FileUtils {

  /** Get Verilog sources for IDEA Editor, will search sources by task selected in IDEA
    */
  def getAllVerilogFiles(project: Project): List[VerilogPSIFileRoot] = {
    implicit val manifest = IdeaManifestManager.getImplicitManifest(project = project)
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
          case configuration: ScaledaRunConfiguration => configuration.generateRuntime(project = project)
          case _                                      => None
        }
      }
    val defaultSources = ProjectConfig.getConfig.map(c => c.getSourceSet ++ c.getTestSet).getOrElse(Set())
    // into synchronized block, only one thread to operate ip cache
    val sources = synchronized {
      // get ALL Scaleda IPs, (absPath, ProjectConfig)
      val ips: Map[String, ProjectConfig] = rt
        .map(_.task.getAllIps.filter(_._2.exports.nonEmpty))
        .getOrElse(Map())
      val ipStubsSources: Set[String] =
        rt.map(rt => KernelFileUtils.doUpdateStubCacheFromRuntime(rt).map(_.getAbsolutePath).toSet).getOrElse(Set())
      // source set of this project
      val sources: Set[String] = rt match {
        // has runtime, get sources
        case Some(rt) => rt.task.getSourceSet ++ rt.task.getTestSet
        // otherwise, get default sources from ProjectConfig
        case None => defaultSources
      }
      // source set from ips
      val ipSources: Set[String] = ips
        .flatMap(c => {
          c._2.getSourceSet(manifest = ProjectManifest.getTemporalManifest(c._1)) ++ c._2
            .getTestSet(manifest = ProjectManifest.getTemporalManifest(c._1))
        })
        .toSet
      sources ++ ipSources ++ ipStubsSources
    }
    // may reach `pwd`
    // TODO: apply file search paths... example: a.v => test/a.v
    val sourceFiles        = sources.map(new File(_)).filter(_.exists())
    val sourceRegularFiles = sourceFiles.filter(_.isFile)
    val sourceDirectories  = sourceFiles.filter(_.isDirectory)
    val result             = ArrayBuffer[VerilogPSIFileRoot]()

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
    def doParseDir(dirs: Set[File]): Unit = {
      // search and append files from sourceDirectories
      // for dirs (`ip/<ip_name>/*.v`) including `*_stub.v`，, just accept `*_stub.v` and ignore others
      dirs.foreach(f => {
        val parent = f.getParentFile
        val isVivadoIp = parent != null && parent.getName == "ip" && f
          .listFiles(new FilenameFilter {
            override def accept(file: File, s: String) = s.endsWith(".xci")
          })
          .nonEmpty

        val fileList: Array[File] =
          if (isVivadoIp)
            f.listFiles(new FilenameFilter {
              override def accept(file: File, s: String) = s.endsWith("_stub.v")
            })
          else Array()
        if (fileList.isEmpty) {
          // no stub files, just accept all files
          val allFiles = f.listFiles()
          allFiles.foreach(file => {
            if (file == null || !file.exists()) return
            if (file.isFile) doParseFile(file)
            // do search recursively, DFS
            else if (file.isDirectory) doParseDir(Set(file))
          })
          // val d = LocalFileSystem.getInstance().findFileByIoFile(f)
          // if (d != null) {
          //   val v = PsiTreeUtil
          //     .findChildrenOfAnyType(
          //       psiManager.findDirectory(d),
          //       classOf[VerilogPSIFileRoot]
          //     )
          //     .asScala
          //   result ++= v
          // }
        } else {
          fileList.foreach(doParseFile)
        }
      })
    }
    doParseDir(sourceDirectories)
    if (result.isEmpty) {
      // cannot get any sources, try to load all sources from project base
      if (project.getProjectFile != null && project.getProjectFile.isInLocalFileSystem)
        doParseDir(Set(new File(project.getBasePath)))
    }
    result.toList
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
