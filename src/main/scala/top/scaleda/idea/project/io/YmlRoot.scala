package top.scaleda
package idea.project.io

import kernel.project.config.ProjectConfig
import kernel.utils.serialise.YAMLHelper

import com.intellij.openapi.vfs.VirtualFile

/** A [[YmlRoot]] = A directory containing a scaleda.yml file.
  * @param root The directory containing the scaleda.yml file.
  * @param yml The scaleda.yml file.
  */
class YmlRoot(private val root: VirtualFile, private val yml: VirtualFile) {
  def toProjectConfig: Option[ProjectConfig] = {
    val ymlContent = yml.contentsToByteArray()
    try {
      val config = YAMLHelper(new String(ymlContent), classOf[ProjectConfig])
      config.targets.foreach(target => {
        target.parentNode = Some(config)
        target.tasks.foreach(task => task.parentNode = Some(target))
      })
      Some(config)
    } catch {
      case e: com.fasterxml.jackson.databind.exc.MismatchedInputException =>
        None
    }
  }
}

object YmlRoot {
  /** Returns the [[YmlRoot]] for a directory, provided it contains a scaleda.yml file.
    * <p>
    * Otherwise returns [[None]].
    * <p>
    * (The existence check is based on the filesystem cache; doesn't refresh anything.)
    */
  def forDirectory(dir: VirtualFile): Option[YmlRoot] = {
    if (dir == null || !dir.isDirectory) return None
    val yml = dir.findChild("scaleda.yml")
    if (yml == null || !yml.exists || yml.isDirectory) return None
    Some(new YmlRoot(dir, yml))
  }
}
