package top.criwits.scaleda
package kernel.init

import kernel.utils.KernelLogger

import java.io.{File, FileOutputStream}

object InitHelper {

  /** Create project with initalising it's basic structure and file
    * @param root Project root
    * @param projectName Project name
    * @param projectType Project type identifier, here only "rtl"
    * @param sourceRoot Source root, usually "src"
    */
  def createProjectStructure(root: File, projectName: String, projectType: String, sourceRoot: String): Boolean = {
    val configFile = new File(root, "scaleda.yml")
    val sourceDir  = new File(root, sourceRoot)

    if (configFile.createNewFile() && sourceDir.mkdir()) {
      // write scaleda.yml
      val configContent =
        s"""---
          |name: $projectName
          |type: $projectType
          |source: $sourceRoot
          |""".stripMargin
      val stream = new FileOutputStream(configFile, false)
      stream.write(configContent.getBytes)
      KernelLogger.info(
        s"Generated new Scaleda project structure, project name: $projectName, project root: ${root.getAbsolutePath}"
      )
      true
    } else {
      KernelLogger.error(s"Cannot generate new Scaleda project $projectName")
      false
    }
  }
}
