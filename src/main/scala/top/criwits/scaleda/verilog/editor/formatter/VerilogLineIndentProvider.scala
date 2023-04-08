package top.criwits.scaleda
package verilog.editor.formatter

import verilog.editor.formatter.VerilogLineIndentProvider.getIndentString
import verilog.parser.VerilogLexer
import verilog.psi.nodes.module.ModuleHeadPsiNode
import verilog.{VerilogFileType, VerilogLanguage}

import com.intellij.application.options.CodeStyle
import com.intellij.formatting.IndentInfo
import com.intellij.lang.Language
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.{PsiDocumentManager, PsiElement, PsiFile, PsiWhiteSpace}
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.text.CharArrayUtil
import top.criwits.scaleda.verilog.psi.nodes.condition.ConditionalStatementPsiNode
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode

/** Verilog line indent provider. This provider is used for quick, real-time indent complementation.
 * Block (formatter) based line indent provider is not this one.
 *
 * @todo Shall never treat PSI here.
  */
class VerilogLineIndentProvider extends LineIndentProvider {
  //noinspection DuplicatedCode
  override def getLineIndent(project: Project, editor: Editor, language: Language, offset: Int): String = {
    if (offset > 0) {
      val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset - 1)
      if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.White_space))) { // This seems is needed to check if it is enter event
        pos.moveAtEndOfPreviousLine()
        if (
          pos.isAtAnyOf(
            VerilogLanguage.getTokenType(VerilogLexer.K_else) // TODO: add IF
          )
        ) {
          return getIndentString(editor, pos.getStartOffset, 1)
        }
      }
    }
    null
  }

  override def isSuitableFor(language: Language): Boolean = language.isInstanceOf[VerilogLanguage.type]

}

object VerilogLineIndentProvider {

  /** Calculate the indent and get the indent string for the current line
    * @param editor current [[Editor]]
    * @param offset offset to caret
    * @param policy -1: shrink 0: hold 1: expand
    * @return
    */
  def getIndentString(editor: Editor, offset: Int, policy: Int): String = {
    val docChars      = editor.getDocument.getCharsSequence
    val settings      = CodeStyle.getSettings(editor)
    val indentOptions = settings.getIndentOptions(VerilogFileType.instance)
    val indentLength  = indentOptions.INDENT_SIZE
    var baseIndent    = ""
    if (offset > 0) {
      val indentStart = CharArrayUtil.shiftBackwardUntil(docChars, offset, "\n") + 1
      if (indentStart >= 0) {
        val indentEnd = CharArrayUtil.shiftForward(docChars, indentStart, " \t")
        val diff      = indentEnd - indentStart
        if (diff > 0) {
          if (policy >= 0) baseIndent = docChars.subSequence(indentStart, indentEnd).toString
          else if (diff >= indentLength)
            baseIndent = docChars.subSequence(indentStart, indentEnd - indentLength).toString
        }
      }
    }

    if (policy == 1) {
      baseIndent += new IndentInfo(0, indentLength, 0).generateNewWhiteSpace(indentOptions)
    }
    baseIndent
  }


  def indentElement(file: PsiFile, checkVisibleOnly: Boolean = true)
                                   (document: Document, project: Project, element: PsiElement, offset: Int)
                                   (prevCondition: PsiElement => Boolean,
                                    condition: PsiElement => Boolean = _.isInstanceOf[PsiWhiteSpace]): Unit = {
    if (condition(element)) {
      val prev = if (checkVisibleOnly) PsiTreeUtil.prevVisibleLeaf(element) else PsiTreeUtil.prevLeaf(element)
      if (prevCondition(prev)) {
        PsiDocumentManager.getInstance(project).commitDocument(document)
        CodeStyleManager.getInstance(project).adjustLineIndent(file, prev.getTextRange)
      }
    }
  }
}
