package top.criwits.scaleda
package verilog.utils

import idea.runner.configuration.ScaledaRunConfiguration
import kernel.project.config.ProjectConfig
import kernel.utils.KernelFileUtils
import kernel.utils.KernelFileUtils.parseAsAbsolutePath
import verilog.VerilogPSIFileRoot

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.apache.commons.codec.digest.DigestUtils

import java.io.{File, FileInputStream}
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._
import scala.jdk.javaapi.CollectionConverters

object FileUtils {

  /** Get Verilog sources for IDEA Editor, will search sources by task selected in IDEA
    */
  def getAllVerilogFiles(project: Project): List[VerilogPSIFileRoot] = {
    // get configuration selected now
    val configuration: RunConfiguration = RunManager.getInstance(project).getSelectedConfiguration.getConfiguration
    val defaultSources                  = ProjectConfig.getConfig().map(c => c.getSourceSet() ++ c.getTestSet()).getOrElse(Set())
    val rt = configuration match {
      // if it's a valid ScaledaRunProcessHandler
      case configuration: ScaledaRunConfiguration => configuration.generateRuntime
      case _                                      => None
    }
    // into synchronized block, only one thread to operate ip cache
    val sources = synchronized {
      val ipFiles = rt
        .map(rt => rt.task.getIpFiles().map(parseAsAbsolutePath(_, Some(rt.workingDir.getAbsolutePath))))
        .getOrElse(Set())
        .map(p => new File(p))
        .filter(f => f.exists() && f.isFile)
      // get ip files hashes using commons-codec
      val ipFilesHashes = ipFiles.map(f => (f, DigestUtils.sha256Hex(new FileInputStream(f))))
      // get all hashes
      val allHashes = KernelFileUtils.getAllCachedIpHash
      // remove caches not in hash list
      allHashes.filter(h => !ipFilesHashes.exists(_._2 == h)).foreach(KernelFileUtils.removeIpFileCache)
      // update ip cache
      ipFilesHashes.foreach(h => KernelFileUtils.updateIpFileCache(h._1, Some(h._2)))
      val sources: Set[String] = rt match {
        // has runtime, get sources
        case Some(rt) => rt.task.getSourceSet() ++ rt.task.getTestSet()
        // otherwise, get default sources from ProjectConfig
        case None => defaultSources
      }
      // search cache directory to get all source files
      val ipSources = KernelFileUtils.getAllSourceFiles(Set(KernelFileUtils.ipCacheDirectory.getAbsolutePath)).map(_.getAbsolutePath).toSet
      sources ++ ipSources
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
