package top.criwits.scaleda
package verilog.utils

import idea.runner.configuration.ScaledaRunConfiguration
import idea.windows.tasks.ip.IPInstance
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
        .map(_.task.getAllIps().filter(_._2.exports.nonEmpty))
        .getOrElse(Map())
      // collects single file ip, in this project and every Scaleda IP
      // val singleFileIps = rt
      //   .map(rt => rt.task.getIpFiles())
      //   .getOrElse(Set()) ++
      //   ips.map(p => p._2.getIpFiles(projectBase = Some(p._1))).foldLeft(Set[String]())(_ ++ _)
      // KernelFileUtils.doUpdateIpFilesCache(singleFileIps)
      // collect Scaleda IPs and make stubs from ip instances
      val ipInstances: Seq[IPInstance] = rt
        .map(rt => rt.task.getIpInstances())
        .getOrElse(Seq())
      // val stubs: Map[String, String] = ips.flatMap { case (_, ip) =>
      //   ipInstances.get(ip.exports.get.name).map(context => (ip.exports.get.name, ip.exports.get.renderStub(context)))
      // }
      // save these stubs to .cache/stubs/hash/id-module.v
      val stubsCacheDir = new File(KernelFileUtils.ipCacheDirectory, "stubs")
      KernelFileUtils.doUpdateIpStubsCache(ips, ipInstances, stubsCacheDir)
      val ipStubsSources: Set[String] =
        // KernelFileUtils.doUpdateIpStubsCache(ips, ipInstances, stubsCacheDir).map(_.getAbsolutePath)
        KernelFileUtils
          .getAllSourceFiles(Set(stubsCacheDir.getAbsolutePath))
          .map(_.getAbsolutePath)
          .toSet
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
      // val ipStubsSources: Set[String] = KernelFileUtils
      //   .getAllSourceFiles(Set(stubsCacheDir.getAbsolutePath))
      //   .map(_.getAbsolutePath)
      //   .toSet
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
    sourceRegularFiles.foreach(f => {
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
    })
    def doParseDir(dirs: Set[File]) = {
      // search and append files from sourceDirectories
      dirs.foreach(f => {
        val d = LocalFileSystem.getInstance().findFileByIoFile(f)
        if (d != null) {
          val v = PsiTreeUtil
            .findChildrenOfAnyType(
              psiManager.findDirectory(d),
              classOf[VerilogPSIFileRoot]
            )
            .asScala
          result ++= v
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
