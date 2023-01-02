package top.criwits.scaleda
package kernel.utils

/**
 * OS type detecter
 */
object OS extends Enumeration {
  private val osName = System.getProperty("os.name").toLowerCase()
  val Windows, Unix, MacOS = Value
  def getOSType: Value = osName match {
    case name if (name.indexOf("win") >= 0) => Windows
    case name if (name.indexOf("mac") >= 0) => MacOS
    case _ => Unix
  }
}
