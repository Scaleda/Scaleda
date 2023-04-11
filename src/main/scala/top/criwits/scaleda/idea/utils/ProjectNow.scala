package top.criwits.scaleda
package idea.utils

import com.intellij.openapi.project.{Project, ProjectManager}

// FIXME: multi-projects support
object ProjectNow {
  def apply(): Option[Project] =
    ProjectManager.getInstance().getOpenProjects.headOption // FIXME: what would happen if multiple projects are opened?
}
