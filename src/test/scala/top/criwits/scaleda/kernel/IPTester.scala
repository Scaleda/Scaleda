package top.criwits.scaleda
package kernel

import kernel.project.config.ProjectConfig

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
      val bkp = ProjectConfig.projectBase
      ProjectConfig.projectBase = Some(workDir)
      val config =
        ProjectConfig.getConfig(path = Some(new File(workDirFile, ProjectConfig.defaultConfigFile).getAbsolutePath)).get
      val ips = config.getAllIps()
      println(ips)
      val ipSources = ips
        .flatMap(c => {
          c._2.getSourceSet(projectBase = Some(c._1)) ++ c._2.getTestSet(projectBase = Some(c._1))
        })
        .toSet
      println(ipSources)
      // restore bkp
      ProjectConfig.projectBase = bkp
    }
  }
}
