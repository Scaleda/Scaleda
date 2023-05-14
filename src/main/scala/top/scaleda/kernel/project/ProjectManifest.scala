package top.scaleda
package kernel.project

class ProjectManifest(
    var project: AbstractProject = AbstractProject.default,
    var projectBase: Option[String] = None,
    var configFile: Option[String] = None
) {
  // def getTemporallyChild(name: String) = new ProjectManifest(projectBase = projectBase.map(_ + "/" + name))
}

object ProjectManifest {
  def getTemporalManifest(absPath: String) = new ProjectManifest(projectBase = Some(absPath))
}
