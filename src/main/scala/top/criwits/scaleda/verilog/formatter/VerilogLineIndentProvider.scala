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

class VerilogLineIndentProvider extends LineIndentProvider {

  override def getLineIndent(project: Project, editor: Editor, language: Language, offset: Int): String = {
    if (offset > 0) {
      val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset - 1)
      if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.White_space))) {
        moveAtEndOfPreviousLine(pos)
        if (pos.isAtAnyOf(
          VerilogLanguage.getTokenType(VerilogLexer.K_begin),
          VerilogLanguage.getTokenType(VerilogLexer.Right_parenthes), // ) <caret> [Enter] => ) \n
          VerilogLanguage.getTokenType(VerilogLexer.Left_parenthes)
        )) {
          return getIndentString(editor, pos.getStartOffset, true)
        }
        if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.Semicolon))) {
          // for ...); <caret> [Enter]
          pos.moveBeforeOptionalMix(
            VerilogLanguage.getTokenType(VerilogLexer.Semicolon),
            VerilogLanguage.getTokenType(VerilogLexer.White_space))
          if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.Right_parenthes))) {
            return getIndentString(editor, pos.getStartOffset, true)
          }
        }
      }
//
//      if (element.textMatches("begin")) {
//        moveAtEndOfPreviousLine(pos)
//        if (pos.isAtAnyOf(getTokenType(VerilogLexer.K_begin))) {
//          println("begin detected")
//          return getIndentString(editor, pos.getStartOffset, true)
//        }
//      }
    }
    null
  }

  override def isSuitableFor(language: Language): Boolean = language.isInstanceOf[VerilogLanguage.type] // ?

  private def moveAtEndOfPreviousLine(pos: VerilogSemanticEditorPosition): Unit = {
    pos.moveBeforeOptionalMix(VerilogLanguage.getTokenType(VerilogLexer.White_space))
    // should be enough
  }

  /**
   * Magic?
   * @param editor
   * @param offset
   * @param shouldExpand
   * @return
   */
  private def getIndentString(editor: Editor, offset: Int, shouldExpand: Boolean): String = {
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
          if (shouldExpand) baseIndent = docChars.subSequence(indentStart, indentEnd).toString
          else if (diff >= 4) baseIndent = docChars.subSequence(indentStart, indentEnd - 4).toString
        }
      }
    }

    if (shouldExpand) {
      baseIndent += new IndentInfo(0, 4, 0).generateNewWhiteSpace(indentOptions)
    }
    baseIndent
  }
}
