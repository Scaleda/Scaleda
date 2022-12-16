package top.criwits.scaleda
package verilog

import verilog.parser.VerilogLexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

class VerilogLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(VerilogLanguage, new VerilogLexer(null))

  override def getTokenHighlights(tokenType: IElementType): Array[TextAttributesKey] = {
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t = tokenIElementType.getANTLRTokenType
    import VerilogLexerSyntaxHighlighter._
    // println(s"t = ${t}")
    t match {
      // TODO: check is this block comments?
      case VerilogLexer.COMMENTS => Array(createTextAttributesKey(
        "VERILOG_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT))
      case VerilogLexer.COMMENT => Array(createTextAttributesKey(
        "VERILOG_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT))
      case n if n == getTypeForLiteralName(";") => Array(createTextAttributesKey(
        "VERILOG_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON))
      case n if n == getTypeForLiteralName(",") => Array(createTextAttributesKey(
        "VERILOG_SEMICOLON", DefaultLanguageHighlighterColors.COMMA))
      // case VerilogLexer.BINARY_NUMBER_MODE | VerilogLexer.BINARY_VALUE | VerilogLexer.BINARY_BASE |
      //      // VerilogLexer.OCTAL_NUMBER_MODE | VerilogLexer.OCTAL_VALUE | VerilogLexer.OCTAL_BASE |
      //      // VerilogLexer.HEX_NUMBER_MODE | VerilogLexer.HEX_VALUE | VerilogLexer.HEX_BASE |
      //      // VerilogLexer.DECIMAL_NUMBER_MODE | VerilogLexer.DECIMAL_BASE |
      //      VerilogLexer.REAL => Array(createTextAttributesKey(
      //     "VERILOG_NUMBER", DefaultLanguageHighlighterColors.NUMBER))
      // TODO: VerilogLexer.Dollar_identifier
      case n if VerilogKeywords.VERILOG_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(createTextAttributesKey(
          "VERILOG_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD))
      case VerilogLexer.STRING => Array(createTextAttributesKey(
        "VERILOG_STRING", DefaultLanguageHighlighterColors.STRING))
      case VerilogLexer.MACRO_IDENTIFIER | VerilogLexer.SIMPLE_IDENTIFIER |
           VerilogLexer.ESCAPED_IDENTIFIER | VerilogLexer.SYSTEM_TF_IDENTIFIER => Array(createTextAttributesKey(
        "VERILOG_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER))
      case _ => Array()
    }
  }
}

object VerilogLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to VerilogLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == VerilogLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }
}