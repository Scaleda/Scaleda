package top.scaleda
package idea.project.io

import com.intellij.openapi.project.Project
import com.intellij.openapi.module.{Module, ModuleManager}
import com.intellij.openapi.roots.ModuleRootManager

/**
 * Manager for [[YmlRoot]]s in a project
 * @param project The project
 * @todo use cache
 */
class YmlRootManager(private val project: Project) {
  def getRoots(module: Module): Set[YmlRoot] = {
    ModuleRootManager.getInstance(module).getContentRoots.flatMap(YmlRoot.forDirectory).toSet
  }
  def getRoots(project: Project): Set[YmlRoot] = {
    ModuleManager.getInstance(project).getModules.flatMap(getRoots).toSet
  }
  def getRoots: Set[YmlRoot] = {
    getRoots(project)
  }
}

object YmlRootManager {
  def getInstance(project: Project): YmlRootManager = project.getService(classOf[YmlRootManager])
}
