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
      case VerilogLexer.Block_comment => Array(createTextAttributesKey(
        "VERILOG_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT))
      case VerilogLexer.One_line_comment => Array(createTextAttributesKey(
        "VERILOG_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT))
      case n if n == getTypeForLiteralName(";") => Array(createTextAttributesKey(
        "VERILOG_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON))
      case n if n == getTypeForLiteralName(",") => Array(createTextAttributesKey(
        "VERILOG_COMMA", DefaultLanguageHighlighterColors.COMMA))
      case
        VerilogLexer.Binary_number |
        VerilogLexer.Octal_number |
        VerilogLexer.Real_number |
        VerilogLexer.Decimal_number |
        VerilogLexer.Hex_number
      => Array(createTextAttributesKey(
        "VERILOG_NUMBER", DefaultLanguageHighlighterColors.NUMBER))
      case VerilogLexer.Dollar_identifier => Array(createTextAttributesKey(
        "VERILOG_DOLLAR_IDENTIFIER", DefaultLanguageHighlighterColors.KEYWORD))
      case n if VerilogKeywords.VERILOG_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(createTextAttributesKey(
          "VERILOG_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD))
      case VerilogLexer.String => Array(createTextAttributesKey(
        "VERILOG_STRING", DefaultLanguageHighlighterColors.STRING))
      case VerilogLexer.Simple_identifier | VerilogLexer.Time_identifier | VerilogLexer.Escaped_identifier => Array(createTextAttributesKey(
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