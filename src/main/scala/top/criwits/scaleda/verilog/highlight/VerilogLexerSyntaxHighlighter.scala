package top.criwits.scaleda
package verilog.highlight

import verilog.parser.VerilogLexer
import verilog.{VerilogKeywords, VerilogLanguage}

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.editor.{DefaultLanguageHighlighterColors => DLHC}
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

/**
 * Syntax highlighter for Verilog
 *
 * Registered by [[VerilogSyntaxHighlighterFactory]], providing lexer-based highlighting functionality.
 * Note that grammar-based highlighting is implemented by [[VerilogHighlightingAnnotator]].
 */
class VerilogLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(VerilogLanguage, new VerilogLexer(null))

  override def getTokenHighlights(tokenType: IElementType): Array[TextAttributesKey] = {
    if (!tokenType.isInstanceOf[TokenIElementType]) return Array.empty
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t                 = tokenIElementType.getANTLRTokenType
    import VerilogLexerSyntaxHighlighter._
    t match {
      case VerilogLexer.Block_comment           => Array(VERILOG_BLOCK_COMMENT)
      case VerilogLexer.One_line_comment        => Array(VERILOG_LINE_COMMENT)
      case n if n == getTypeForLiteralName(";") => Array(VERILOG_COMMA)
      case n if n == getTypeForLiteralName(",") => Array(VERILOG_SEMICOLON)
      case VerilogLexer.Binary_number | VerilogLexer.Octal_number | VerilogLexer.Real_number |
          VerilogLexer.Decimal_number | VerilogLexer.Hex_number =>
        Array(VERILOG_NUMBER)
//      case VerilogLexer.Dollar_identifier => Array(VERILOG_DOLLAR_IDENTIFIER)
      case n if VerilogKeywords.VERILOG_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(VERILOG_KEYWORD)
      case VerilogLexer.String => Array(VERILOG_STRING)
      case VerilogLexer.Simple_identifier | VerilogLexer.Time_identifier | VerilogLexer.Escaped_identifier =>
        Array(VERILOG_IDENTIFIER)
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

  // Directive
  val VERILOG_DIRECTIVE = createTextAttributesKey("VERILOG_DIRECTIVE", DLHC.METADATA)

  // Braces and Operators
  val VERILOG_SEMICOLON = createTextAttributesKey("VERILOG_SEMICOLON", DLHC.SEMICOLON)
  val VERILOG_COMMA     = createTextAttributesKey("VERILOG_COMMA", DLHC.COMMA)



  // Signals
  val VERILOG_SIGNAL_NET       = createTextAttributesKey("VERILOG_SIGNAL_NET", DLHC.STATIC_FIELD)
  val VERILOG_SIGNAL_REG       = createTextAttributesKey("VERILOG_SIGNAL_REG", DLHC.INSTANCE_FIELD)
  val VERILOG_SIGNAL_PARAMETER = createTextAttributesKey("VERILOG_SIGNAL_PARAMETER", DLHC.PARAMETER)

  // Module
  val VERILOG_MODULE_IDENTIFIER    = createTextAttributesKey("VERILOG_MODULE_IDENTIFIER", DLHC.CLASS_NAME)
  val VERILOG_MODULE_INSTANTIATION = createTextAttributesKey("VERILOG_MODULE_INSTANTIATION", DLHC.FUNCTION_DECLARATION)
  val VERILOG_PORT_INPUT = createTextAttributesKey("VERILOG_PORT_INPUT", VERILOG_SIGNAL_NET)
  val VERILOG_PORT_INOUT = createTextAttributesKey("VERILOG_PORT_INOUT", VERILOG_SIGNAL_NET)
  val VERILOG_PORT_OUTPUT = createTextAttributesKey("VERILOG_PORT_OUTPUT", VERILOG_SIGNAL_NET)
  val VERILOG_PORT_OUTPUT_REG = createTextAttributesKey("VERILOG_PORT_OUTPUT_REG", VERILOG_SIGNAL_REG)

  // Comments
  val VERILOG_BLOCK_COMMENT =
    createTextAttributesKey("VERILOG_BLOCK_COMMENT", DLHC.BLOCK_COMMENT)
  val VERILOG_LINE_COMMENT =
    createTextAttributesKey("VERILOG_LINE_COMMENT", DLHC.LINE_COMMENT)

  // Numbers
  val VERILOG_NUMBER = createTextAttributesKey("VERILOG_NUMBER", DLHC.NUMBER)
  // String
  val VERILOG_STRING = createTextAttributesKey("VERILOG_STRING", DLHC.STRING)
  // Keywords
  val VERILOG_DOLLAR_IDENTIFIER =
    createTextAttributesKey("VERILOG_DOLLAR_IDENTIFIER", DLHC.KEYWORD)
  val VERILOG_KEYWORD = createTextAttributesKey("VERILOG_KEYWORD", DLHC.KEYWORD)
  // Identifier
  val VERILOG_IDENTIFIER = createTextAttributesKey("VERILOG_IDENTIFIER", DLHC.IDENTIFIER)
}
