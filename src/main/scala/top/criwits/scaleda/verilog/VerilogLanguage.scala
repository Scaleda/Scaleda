package top.criwits.scaleda
package verilog

import com.intellij.lang.Language
import org.antlr.intellij.adaptor.lexer.{PSIElementTypeFactory, RuleIElementType, TokenIElementType}

object VerilogLanguage extends Language("Verilog") {
  def getTokenType(id: Int): TokenIElementType = PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).get(id)
  def getRuleType(id: Int): RuleIElementType = PSIElementTypeFactory.getRuleIElementTypes(VerilogLanguage).get(id)
}