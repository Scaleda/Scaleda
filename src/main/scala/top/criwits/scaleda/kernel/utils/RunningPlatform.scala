package top.criwits.scaleda
package kernel.utils

object RunningPlatform {
  def isInIdea: Boolean = {
    try {
      Class.forName("com.intellij.openapi.project.Project")
      true
    } catch {
      case _: ClassNotFoundException => false
    }
  }
}
