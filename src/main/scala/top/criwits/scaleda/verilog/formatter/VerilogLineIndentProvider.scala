package top.criwits.scaleda
package verilog.formatter

import verilog.parser.VerilogLexer
import verilog.{VerilogFileType, VerilogLanguage}

import com.intellij.application.options.CodeStyle
import com.intellij.formatting.IndentInfo
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider
import com.intellij.util.text.CharArrayUtil
import top.criwits.scaleda.verilog.formatter.VerilogLineIndentProvider.{getIndentString, moveAtEndOfPreviousLine}

class VerilogLineIndentProvider extends LineIndentProvider {

  override def getLineIndent(project: Project, editor: Editor, language: Language, offset: Int): String = {
    if (offset > 0) {
      val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset - 1)
      if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.White_space))) { // This seems is needed to check if it is enter event
        moveAtEndOfPreviousLine(pos)
        if (pos.isAtAnyOf(
          VerilogLanguage.getTokenType(VerilogLexer.K_begin),
          VerilogLanguage.getTokenType(VerilogLexer.Right_parenthes), // ) <caret> [Enter]
          VerilogLanguage.getTokenType(VerilogLexer.Left_parenthes), // (<caret> [Enter])
          VerilogLanguage.getTokenType(VerilogLexer.K_else) // else <carent> [Enter]
        )) {
          return getIndentString(editor, pos.getStartOffset, 1)
        }

        // TODO: check if `);`

        return getIndentString(editor, pos.getStartOffset, 0)
      }

    }
    null
  }

  override def isSuitableFor(language: Language): Boolean = language.isInstanceOf[VerilogLanguage.type] // ?


}

object VerilogLineIndentProvider {
  def moveAtEndOfPreviousLine(pos: VerilogSemanticEditorPosition): Unit = {
    pos.moveBeforeOptionalMix(VerilogLanguage.getTokenType(VerilogLexer.White_space))
    // should be enough
  }

  /**
   * Magic?
   * @param editor
   * @param offset
   * @param policy -1: shrink 0: hold 1: expand
   * @return
   */
  def getIndentString(editor: Editor, offset: Int, policy: Int): String = {
    val docChars = editor.getDocument.getCharsSequence
    val settings = CodeStyle.getSettings(editor)
    val indentOptions = settings.getIndentOptions(VerilogFileType.instance)

    var baseIndent = ""
    if (offset > 0) {
      val indentStart = CharArrayUtil.shiftBackwardUntil(docChars, offset, "\n") + 1
      if (indentStart >= 0) {
        val indentEnd = CharArrayUtil.shiftForward(docChars, indentStart, " \t")
        val diff = indentEnd - indentStart
        if (diff > 0) {
          if (policy >= 0) baseIndent = docChars.subSequence(indentStart, indentEnd).toString
          else if (diff >= 4) baseIndent = docChars.subSequence(indentStart, indentEnd - 4).toString
        }
      }
    }

    if (policy == 1) {
      baseIndent += new IndentInfo(0, 4, 0).generateNewWhiteSpace(indentOptions)
    }
    baseIndent
  }
}
