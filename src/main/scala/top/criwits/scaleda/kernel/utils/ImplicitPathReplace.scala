package top.criwits.scaleda
package kernel.utils

class ImplicitPathReplace(pattern: String, replacement: String) {
  def doReplace(src: String): String = src.replace(pattern, replacement)
}

object NoPathReplace extends ImplicitPathReplace("", "")
