package top.criwits.scaleda
package verilog.editor.formatter

import verilog.parser.VerilogLexer
import verilog.{VerilogFileType, VerilogLanguage}

import com.intellij.application.options.CodeStyle
import com.intellij.formatting.IndentInfo
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider
import com.intellij.util.text.CharArrayUtil
import VerilogLineIndentProvider.getIndentString
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.block.ConditionalStatementPsiNode
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleHeadPsiNode

class VerilogLineIndentProvider extends LineIndentProvider {

  //noinspection DuplicatedCode
  override def getLineIndent(project: Project, editor: Editor, language: Language, offset: Int): String = {
    if (offset > 0) {
      val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset - 1)
      if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.White_space))) { // This seems is needed to check if it is enter event
        pos.moveAtEndOfPreviousLine()

        // Semantic-based check
        if (pos.isAtAnyOf(
          VerilogLanguage.getTokenType(VerilogLexer.Right_parenthes), // ) <caret> [Enter]
          VerilogLanguage.getTokenType(VerilogLexer.Left_parenthes), // (<caret> [Enter])
          VerilogLanguage.getTokenType(VerilogLexer.K_else) // else <carent> [Enter] // TODO
        )) {
          return getIndentString(editor, pos.getStartOffset, 1)
        }

        if (pos.isAtAnyOf(
          VerilogLanguage.getTokenType(VerilogLexer.K_begin)
        )) {
          return getIndentString(editor, pos.getStartOffset, 1)
        }

        if (pos.isAtAnyOf(
          VerilogLanguage.getTokenType(VerilogLexer.K_end)
        )) {
          return getIndentString(editor, pos.getStartOffset, 0)
        }

        // PSI-based check, update PSI
        PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument)
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument)

        val currentElement = psiFile.findElementAt(offset)
        val posStartElement = psiFile.findElementAt(pos.getStartOffset)

        // module head indent
        if (posStartElement != null) {
          if (currentElement != null) {
            val currentParent = PsiTreeUtil.getParentOfType(currentElement, classOf[ModuleHeadPsiNode])
            val posStartParent = PsiTreeUtil.getParentOfType(posStartElement, classOf[ModuleHeadPsiNode])
            if (currentParent == null && posStartParent != null)
              return getIndentString(editor, posStartParent.getTextRange.getStartOffset, 1)
          } else {
            val posStartParent = PsiTreeUtil.getParentOfType(posStartElement, classOf[ModuleHeadPsiNode])
            if (posStartParent != null)
              return getIndentString(editor, posStartParent.getTextRange.getStartOffset, 1)
          }
        }

        // if () shrink indent
        /// TODO: cannot shrink for
        /// if ()
        ///   if ()
        ///     if ()
        ///       foobar()
        ///     | <= here
        if (currentElement != null && posStartElement != null) {
          val currentParent = PsiTreeUtil.getParentOfType(currentElement, classOf[ConditionalStatementPsiNode])
          val posStartParent = PsiTreeUtil.getParentOfType(posStartElement, classOf[ConditionalStatementPsiNode])
          if ((currentParent == null && posStartParent != null) || (currentParent != posStartParent)) {
            return getIndentString(editor, posStartParent.getTextRange.getStartOffset, 0)
          }
        }




        return getIndentString(editor, pos.getStartOffset, 0)
      }

    }
    null
  }

  override def isSuitableFor(language: Language): Boolean = language.isInstanceOf[VerilogLanguage.type] // ?

}

object VerilogLineIndentProvider {
  /**
   * Magic?
   * @param editor
   * @param offset
   * @param policy -1: shrink 0: hold 1: expand
   * @return
   */
  def getIndentString(editor: Editor, offset: Int, policy: Int): String = {
    val indentLength = CodeStyle.getSettings(editor).getIndentOptions.INDENT_SIZE

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
          else if (diff >= indentLength) baseIndent = docChars.subSequence(indentStart, indentEnd - indentLength).toString
        }
      }
    }

    if (policy == 1) {
      baseIndent += new IndentInfo(0, indentLength, 0).generateNewWhiteSpace(indentOptions)
    }
    baseIndent
  }
}
