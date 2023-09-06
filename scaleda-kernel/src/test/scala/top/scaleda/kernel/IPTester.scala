package top.scaleda
package kernel

import kernel.project.config.ProjectConfig
import kernel.project.{ManifestManager, ScaledaProject}
import kernel.utils.KernelFileUtils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.File

class IPTester extends AnyFlatSpec with should.Matchers {
  behavior of "Scaleda IP"
  it should "test context parse" in {
    val workDir     = "/home/chiro/Vivado/Tests/ip_tests"
    val workDirFile = new File(workDir)
    if (workDirFile.exists()) {
      // backup global project path
      val bkp                                = ManifestManager.getManifest().projectBase
      implicit val manifest: ScaledaProject = ManifestManager.getManifest()
      manifest.projectBase = Some(workDir)
      manifest.configFile = Some(new File(workDirFile, ProjectConfig.defaultConfigFile).getAbsolutePath)
      val config =
        ProjectConfig.getConfig.get
      val ips = config.getAllIps
      println(ips)
      val ipSources = ips
        .flatMap(c => {
          c._2.getSourceSet(project = ScaledaProject.getTemporalProject(c._1)) ++ c._2
            .getTestSet(project = ScaledaProject.getTemporalProject(c._1))
        })
        .toSet
      println(ipSources)
      // test update cache
      val ipFiles = config.getIpFiles ++ ips
        .flatMap(c => c._2.getIpFiles(project = ScaledaProject.getTemporalProject(c._1)))
        .toSet
      KernelFileUtils.doUpdateIpFilesCache(ipFiles)
      // restore bkp
      ManifestManager.getManifest().projectBase = bkp
    }
  }
}
