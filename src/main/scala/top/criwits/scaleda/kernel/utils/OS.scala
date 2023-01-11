package top.criwits.scaleda
package kernel.utils

/**
 * OS-related functions, including OS type detection, user home directory resolver and etc.
 */
object OS extends Enumeration {
  // Enumeration items for different OSes
  val Windows, Unix, MacOS = Value

  /**
   * Get host OS type
   *
   * @return [[Windows]], [[Unix]] or [[MacOS]]
   */
  def getOSType: Value = System.getProperty("os.name").toLowerCase() match {
    case name if (name.indexOf("win") >= 0) => Windows
    case name if (name.indexOf("mac") >= 0) => MacOS
    case _ => Unix
  }

  def isWindows: Boolean = getOSType == Windows

  def getShell: String = if (isWindows) "C:\\Windows\\System32\\cmd.exe /c" else "/bin/sh -c"

  def shell(command: String): String = s"$getShell \"$command\""

  /**
   * Get user home directory path, i.e. `~`
   *
   * @return
   */
  def getUserHome: String = System.getProperty("user.home")

  val cpuCount = Runtime.getRuntime.availableProcessors()
}
