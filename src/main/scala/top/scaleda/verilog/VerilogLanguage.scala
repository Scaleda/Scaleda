package top.scaleda
package verilog

import kernel.language.LangVerilog
import verilog.parser.VerilogLexer

import com.intellij.lang.Language
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.{PSIElementTypeFactory, RuleIElementType, TokenIElementType}

class VerilogLanguage extends Language(LangVerilog.getName) {
  def getTokenType(id: Int): TokenIElementType =
    PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage.INSTANCE).get(id)
  def getRuleType(id: Int): RuleIElementType =
    PSIElementTypeFactory.getRuleIElementTypes(VerilogLanguage.INSTANCE).get(id)

  def identifierTokenSet: TokenSet =
    TokenSet.create(VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Simple_identifier))
  def commentTokenSet: TokenSet = TokenSet.create(
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Block_comment),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.One_line_comment)
  )
  def literalTokenSet: TokenSet = TokenSet.create(
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Real_number),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Decimal_number),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Binary_number),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Octal_number),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.Hex_number),
    VerilogLanguage.INSTANCE.getTokenType(VerilogLexer.String)
  )
}
object VerilogLanguage {
  val INSTANCE = new VerilogLanguage
}
