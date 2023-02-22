package top.criwits.scaleda
package verilog.formatter

import verilog.parser.VerilogLexer
import verilog.{VerilogFileType, VerilogLanguage}
import verilog.VerilogLanguage.getTokenType

import com.intellij.formatting.IndentInfo
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider
import com.intellij.util.text.CharArrayUtil
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

//class VerilogLineIndentProvider extends LineIndentProvider {
//  override def getLineIndent(project: Project, editor: Editor, language: Language, offset: Int): String = {
//    if (offset > 0) {
//      // Update PSI...
//      PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument);
//      val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument)
//      val element = psiFile.findElementAt(offset).getPrevSibling
//
//      if (element.textMatches("begin")) {
//        moveAtEndOfPreviousLine(pos)
//        if (pos.isAtAnyOf(getTokenType(VerilogLexer.K_begin))) {
//          println("begin detected")
//          return getIndentString(editor, pos.getStartOffset, true)
//        }
//      }
//    }
//    null
//  }
//
//  override def isSuitableFor(language: Language): Boolean = language.isInstanceOf[VerilogLanguage.type] // ?
//
//  private def moveAtEndOfPreviousLine(pos: VerilogSemanticEditorPosition): Unit = {
//    pos.moveBeforeOptionalMix()
//    // should be enough
//  }
//
//  private def getIndentString(editor: Editor, offset: Int, shouldExpand: Boolean): String = {
//    val docChars = editor.getDocument.getCharsSequence
//    import com.intellij.application.options.CodeStyle
//    val settings = CodeStyle.getSettings(editor)
//    val indentOptions = settings.getIndentOptions(VerilogFileType.instance)
//
//    var baseIndent = ""
//    if (offset > 0) {
//      val indentStart = CharArrayUtil.shiftBackwardUntil(docChars, offset, "\n") + 1
//      if (indentStart >= 0) {
//        val indentEnd = CharArrayUtil.shiftForward(docChars, offset, "\t")
//        val diff = indentEnd - indentStart
//        if (diff > 0) {
//          if (shouldExpand) baseIndent = docChars.subSequence(indentStart, indentEnd).toString
//          else if (diff >= 4) baseIndent = docChars.subSequence(indentStart, indentEnd - 4).toString
//        }
//      }
//    }
//
//    if (shouldExpand) {
//      baseIndent += new IndentInfo(0, 4, 0).generateNewWhiteSpace(indentOptions)
//    }
//    baseIndent
//  }
//}
