package top.criwits.scaleda
package idea.project

import kernel.project.ManifestManager

import com.intellij.openapi.project.Project

object IdeaManifestManager {
  def getImplicitManifest(implicit project: Project) = ManifestManager.getManifest(project)

  def getObject[T](key: String)(implicit project: Project = null): Option[T] = {
    ManifestManager.getObject(key)(project = project)
  }

  def putObject[T](key: String, value: T)(implicit project: Project = null): Unit = {
    ManifestManager.putObject(key, value)(project = project)
  }
}
