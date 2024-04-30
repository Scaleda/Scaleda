package top.scaleda
package verilog.editor.brace

import verilog.VerilogLanguage
import verilog.editor.brace.VerilogBraceMatcherProvider.PAIRS
import verilog.parser.VerilogLexer

import com.intellij.codeInsight.highlighting.BraceMatchingUtil
import com.intellij.lang.{BracePair, PairedBraceMatcher}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

class VerilogBraceMatcherProvider extends PairedBraceMatcher {
  override def getPairs: Array[BracePair] = PAIRS

  override def isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType): Boolean = true

  override def getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset
}

object VerilogBraceMatcherProvider {
  final val PAIRS: Array[BracePair] = Array(
    // {} [] ()
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Left_brace),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Right_brace),
      false
    ),
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Left_bracket),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Right_bracket),
      false
    ),
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Left_parenthes),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.Right_parenthes),
      false
    ),
    // begin ... end
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_begin),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_end),
      true
    ),
    // case ... endcase
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_case),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_endcase),
      true
    ),
    // module ... endmodule
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_module),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(VerilogLexer.K_endmodule),
      true
    ),
  )


  /**
   * Magical code copied from IDEA source code, not know how it work, but it works
   * @param editor the [[Editor]]
   * @param offset the offset
   * @param fileType the [[FileType]]
   * @return
   */
  def getUnmatchedBracesCount(editor: Editor, offset: Int, fileType: FileType): Int = {
    if (offset == 0) return -1
    val chars = editor.getDocument.getCharsSequence
//    if (chars.charAt(offset - 1) != '{') return -1

    val highlighter = editor.getHighlighter

    var iterator = highlighter.createIterator(offset - 1)
    if (iterator.getTokenType == VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.White_space)) iterator.retreat()
    val braceMatcher = BraceMatchingUtil.getBraceMatcher(fileType, iterator)

    if (!braceMatcher.isLBraceToken(iterator, chars, fileType)
      || !braceMatcher.isStructuralBrace(iterator, chars, fileType)) return -1

    val language = iterator.getTokenType.getLanguage

    var lBracesBeforeOffset = 0
    var rBracesAfterOffset = 0

    iterator = highlighter.createIterator(0)
    while (!iterator.atEnd) {
      val tokenType = iterator.getTokenType
      if (tokenType.getLanguage == language && braceMatcher.isStructuralBrace(iterator, chars, fileType)) {
        val beforeOffset = iterator.getStart < offset
        if (braceMatcher.isLBraceToken(iterator, chars, fileType))
          if (beforeOffset) lBracesBeforeOffset += 1
          else rBracesAfterOffset -= 1
        else if (braceMatcher.isRBraceToken(iterator, chars, fileType))
          if (beforeOffset) { // If there are more right braces then left - code before is invalid but let's not break the code after.
          if (lBracesBeforeOffset > 0) lBracesBeforeOffset -= 1
        }
        else {
          rBracesAfterOffset += 1
          if (rBracesAfterOffset == lBracesBeforeOffset) { // Do not calculate further. We've completed all scopes before cursor.
            return 0
          }
        }

      }
      iterator.advance()
    }

    lBracesBeforeOffset - rBracesAfterOffset
  }

}
