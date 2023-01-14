package top.criwits.scaleda
package idea.utils

import com.intellij.openapi.project.{Project, ProjectManager}

object ProjectNow {
  def apply(): Option[Project] =
    ProjectManager.getInstance().getOpenProjects.headOption
}
