package top.criwits.scaleda
package kernel.utils

import java.util.regex.Pattern

class ImplicitPathReplace(
    pattern: String,
    replacement: String,
    regexPatten: Option[String] = None,
    regexReplacements: Seq[String] = Seq()
) {
  def doReplace(src: String): String = src.replace(pattern, replacement)
  def doRegexReplace(src: String): String = regexPatten
    .map(r => {
      val p     = Pattern.compile(r)
      val m     = p.matcher(src)
      val sb    = new StringBuffer()
      while (m.find()) {
        // TODO: more replace matches? well
        val replacement = regexReplacements.head.replace("$1", m.group(1))
        KernelLogger.info(m.group(), "replacement", replacement)
        m.appendReplacement(sb, replacement)
      }
      m.appendTail(sb).toString
    })
    .getOrElse(src)
  def doInvReplace(src: String): String = src.replace(replacement, pattern)
}

object NoPathReplace extends ImplicitPathReplace("", "")
