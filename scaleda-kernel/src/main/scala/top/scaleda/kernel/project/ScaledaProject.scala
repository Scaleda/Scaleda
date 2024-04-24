package top.scaleda
package kernel.project

class ScaledaProject(
//    var project: AbstractProject = AbstractProject.default,
    var projectBase: Option[String] = None,
    var configFile: Option[String] = None
) {
  // def getTemporallyChild(name: String) = new ProjectManifest(projectBase = projectBase.map(_ + "/" + name))
}

object ScaledaProject {
  def getTemporalProject(absPath: String) = new ScaledaProject(projectBase = Some(absPath))
}
