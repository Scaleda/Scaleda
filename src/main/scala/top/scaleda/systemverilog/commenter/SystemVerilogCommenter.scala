package top.scaleda
package systemverilog.commenter

import com.intellij.lang.Commenter

class SystemVerilogCommenter extends Commenter {
  override def getLineCommentPrefix: String = "#"

  override def getBlockCommentPrefix: String = "if 0 {"

  override def getBlockCommentSuffix: String = "}"

  override def getCommentedBlockCommentPrefix: String = null

  override def getCommentedBlockCommentSuffix: String = null
}
