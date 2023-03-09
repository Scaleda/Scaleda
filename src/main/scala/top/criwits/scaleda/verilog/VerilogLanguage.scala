package top.criwits.scaleda
package verilog

import com.intellij.lang.Language
import com.intellij.notification.NotificationGroupManager
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.{PSIElementTypeFactory, RuleIElementType, TokenIElementType}
import top.criwits.scaleda.verilog.parser.VerilogLexer

object VerilogLanguage extends Language("Verilog") {
  def getTokenType(id: Int): TokenIElementType = PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(id)
  def getRuleType(id: Int): RuleIElementType = PSIElementTypeFactory.getRuleIElementTypes(VerilogLanguage).get(id)

  def identifierTokenSet: TokenSet = TokenSet.create(VerilogLanguage.getTokenType(VerilogLexer.Simple_identifier))
  def commentTokenSet: TokenSet = TokenSet.create(VerilogLanguage.getTokenType(VerilogLexer.Block_comment), VerilogLanguage.getTokenType(VerilogLexer.One_line_comment))
  def literalTokenSet: TokenSet = TokenSet.create(
      VerilogLanguage.getTokenType(VerilogLexer.Real_number),
      VerilogLanguage.getTokenType(VerilogLexer.Decimal_number),
      VerilogLanguage.getTokenType(VerilogLexer.Binary_number),
      VerilogLanguage.getTokenType(VerilogLexer.Octal_number),
      VerilogLanguage.getTokenType(VerilogLexer.Hex_number),
      VerilogLanguage.getTokenType(VerilogLexer.String)
    )
}