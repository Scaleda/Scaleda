package top.criwits.scaleda
package idea.project

import kernel.project.ManifestManager

import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nullable

object IdeaManifestManager {
  def getImplicitManifest(implicit project: Project) = ManifestManager.getManifest(project)

  def getObject[T](key: String)(implicit @Nullable project: Project): Option[T] = {
    ManifestManager.getObject(key)(project = project)
  }

  def putObject[T](key: String, value: T)(implicit @Nullable project: Project): Unit = {
    ManifestManager.putObject(key, value)(project = project)
  }
}
