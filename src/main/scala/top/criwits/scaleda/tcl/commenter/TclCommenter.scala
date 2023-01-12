package top.criwits.scaleda
package tcl.commenter

import com.intellij.lang.Commenter

class TclCommenter extends Commenter {
  override def getLineCommentPrefix: String = "#"

  override def getBlockCommentPrefix: String = "if 0 {"

  override def getBlockCommentSuffix: String = "}"

  override def getCommentedBlockCommentPrefix: String = null

  override def getCommentedBlockCommentSuffix: String = null
}
