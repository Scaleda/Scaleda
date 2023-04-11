package top.criwits.scaleda
package kernel.utils

object NameLegalization {
  val illegalChars = "\\/\'\"*?<>|:{};=+"
  val replaceChar  = '_'
  def legalization(src: String): String = {
    var now = src
    illegalChars.foreach(c => {
      now = now.replace(c, replaceChar)
    })
    now
  }
}
