package top.criwits.scaleda
package verilog.editor

import verilog.VerilogLanguage
import verilog.editor.VerilogBraceMatcherProvider.PAIRS
import verilog.parser.VerilogLexer

import com.intellij.lang.{BracePair, PairedBraceMatcher}
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
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Left_brace),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Right_brace),
      false
    ),
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Left_bracket),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Right_bracket),
      false
    ),
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Left_parenthes),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.Right_parenthes),
      false
    ),
    // begin ... end
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_begin),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_end),
      true
    ),
    // case ... endcase
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_case),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_endcase),
      true
    ),
    // module ... endmodule
    new BracePair(
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_module),
      PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(VerilogLexer.K_endmodule),
      true
    ),
  )

}
