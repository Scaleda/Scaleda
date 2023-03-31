package top.criwits.scaleda
package kernel.utils

import java.util.regex.Pattern

/** Tools for implicit path replace
  * @param pattern char sequence to be replaced
  * @param replacement replacement
  * @param regexPatten optional regex pattern
  * @param regexReplacements optional regex replacements, only one replacement is supported: $1
  * @param inner inner replace, will firstly do inner replace
  */
class ImplicitPathReplace(
    pattern: String = "",
    replacement: String = "",
    regexPatten: Option[String] = None,
    regexReplacements: Seq[String] = Seq(),
    inner: Option[ImplicitPathReplace] = None
) {
  def doCharReplace(src: String, skipInner: Boolean = false): String = inner match {
    case Some(i) if !skipInner => doCharReplace(i.doCharReplace(src), skipInner = true)
    case _                     => src.replace(pattern, replacement)
  }
  def doRegexReplace(src: String, skipInner: Boolean = false): String = {
    inner match {
      case Some(i) if !skipInner => doRegexReplace(i.doRegexReplace(src))
      case _ =>
        regexPatten
          .map(r => {
            val p  = Pattern.compile(r)
            val m  = p.matcher(src)
            val sb = new StringBuffer()
            while (m.find()) {
              // TODO: more replace matches? well
              val replacement = regexReplacements.head.replace("$1", m.group(1))
              KernelLogger.debug(m.group(), "replacement", replacement)
              m.appendReplacement(sb, replacement)
            }
            m.appendTail(sb).toString
          })
          .getOrElse(src)
    }
  }
  def doInvCharReplace(src: String): String = src.replace(replacement, pattern)

  def doAllReplace(src: String): String = doRegexReplace(doCharReplace(src))
}

object NoPathReplace extends ImplicitPathReplace("", "")

class RegexReplace(
    regexPatten: String,
    regexReplacements: Seq[String] = Seq()
) extends ImplicitPathReplace(regexPatten = Some(regexPatten), regexReplacements = regexReplacements)

/** { {{module}} } =template=> { module_value } ==> {module_value}
  */
object TemplateContextReplace extends RegexReplace("\\{ ([^\\{\\}]+) \\}", Seq("{$1}"))
