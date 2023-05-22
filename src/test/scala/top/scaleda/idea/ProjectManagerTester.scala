package top.scaleda
package idea

import idea.project.IdeaManifestManager

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ProjectManagerTester extends AnyFlatSpec with should.Matchers {
  behavior of "ProjectManager"
  it should "test manager" in {
    val v = Map("1" -> "2")
    IdeaManifestManager.putObject("v", v)(project = null)
    val v2: Map[String, String] = IdeaManifestManager.getObject("v")(project = null).get
    println(v2)
  }
}
