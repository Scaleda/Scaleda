package top.scaleda
package systemverilog.highlight

import systemverilog.{SystemVerilogKeywords, SystemVerilogLanguage}
import systemverilog.parser.SystemVerilogLexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.editor.{DefaultLanguageHighlighterColors => DLHC}
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

/** Syntax highlighter for SystemVerilog
  *
  * Registered by [[SystemVerilogSyntaxHighlighterFactory]], providing lexer-based highlighting functionality.
  * Note that grammar-based highlighting is implemented by [[SystemVerilogHighlightingAnnotator]].
  */
class SystemVerilogLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(SystemVerilogLanguage.INSTANCE, new SystemVerilogLexer(null))

  override def getTokenHighlights(tokenType: IElementType): Array[TextAttributesKey] = {
    if (!tokenType.isInstanceOf[TokenIElementType]) return Array.empty
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t                 = tokenIElementType.getANTLRTokenType
    import SystemVerilogLexerSyntaxHighlighter._
    t match {
      case SystemVerilogLexer.BLOCK_COMMENT       => Array(SYSTEMVERILOG_BLOCK_COMMENT)
      case SystemVerilogLexer.LINE_COMMENT        => Array(SYSTEMVERILOG_LINE_COMMENT)
      case n if n == getTypeForLiteralName(";")   => Array(SYSTEMVERILOG_COMMA)
      case n if n == getTypeForLiteralName(",")   => Array(SYSTEMVERILOG_SEMICOLON)
      case SystemVerilogLexer.DECIMAL_NUMBER_MODE => Array(SYSTEMVERILOG_NUMBER)
      // case SystemVerilogLexer.Dollar_identifier => Array(SYSTEMVERILOG_DOLLAR_IDENTIFIER)
      case n if SystemVerilogKeywords.SYSTEMVERILOG_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(SYSTEMVERILOG_KEYWORD)
      case SystemVerilogLexer.STRING_LITERAL => Array(SYSTEMVERILOG_STRING)
      case SystemVerilogLexer.IDENTIFIER     => Array(SYSTEMVERILOG_IDENTIFIER)
      case _                                 => Array()
    }
  }
}

object SystemVerilogLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to SystemVerilogLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == SystemVerilogLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }

  // Directive
  val SYSTEMVERILOG_DIRECTIVE = createTextAttributesKey("SYSTEMVERILOG_DIRECTIVE", DLHC.METADATA)

  // Braces and Operators
  val SYSTEMVERILOG_SEMICOLON = createTextAttributesKey("SYSTEMVERILOG_SEMICOLON", DLHC.SEMICOLON)
  val SYSTEMVERILOG_COMMA     = createTextAttributesKey("SYSTEMVERILOG_COMMA", DLHC.COMMA)

  // Signals
  val SYSTEMVERILOG_SIGNAL_NET       = createTextAttributesKey("SYSTEMVERILOG_SIGNAL_NET", DLHC.STATIC_FIELD)
  val SYSTEMVERILOG_SIGNAL_REG       = createTextAttributesKey("SYSTEMVERILOG_SIGNAL_REG", DLHC.INSTANCE_FIELD)
  val SYSTEMVERILOG_SIGNAL_PARAMETER = createTextAttributesKey("SYSTEMVERILOG_SIGNAL_PARAMETER", DLHC.PARAMETER)

  // Module
  val SYSTEMVERILOG_MODULE_IDENTIFIER = createTextAttributesKey("SYSTEMVERILOG_MODULE_IDENTIFIER", DLHC.CLASS_NAME)
  val SYSTEMVERILOG_MODULE_INSTANTIATION =
    createTextAttributesKey("SYSTEMVERILOG_MODULE_INSTANTIATION", DLHC.FUNCTION_DECLARATION)
  val SYSTEMVERILOG_PORT_INPUT      = createTextAttributesKey("SYSTEMVERILOG_PORT_INPUT", SYSTEMVERILOG_SIGNAL_NET)
  val SYSTEMVERILOG_PORT_INOUT      = createTextAttributesKey("SYSTEMVERILOG_PORT_INOUT", SYSTEMVERILOG_SIGNAL_NET)
  val SYSTEMVERILOG_PORT_OUTPUT     = createTextAttributesKey("SYSTEMVERILOG_PORT_OUTPUT", SYSTEMVERILOG_SIGNAL_NET)
  val SYSTEMVERILOG_PORT_OUTPUT_REG = createTextAttributesKey("SYSTEMVERILOG_PORT_OUTPUT_REG", SYSTEMVERILOG_SIGNAL_REG)

  // Comments
  val SYSTEMVERILOG_BLOCK_COMMENT =
    createTextAttributesKey("SYSTEMVERILOG_BLOCK_COMMENT", DLHC.BLOCK_COMMENT)
  val SYSTEMVERILOG_LINE_COMMENT =
    createTextAttributesKey("SYSTEMVERILOG_LINE_COMMENT", DLHC.LINE_COMMENT)

  // Numbers
  val SYSTEMVERILOG_NUMBER = createTextAttributesKey("SYSTEMVERILOG_NUMBER", DLHC.NUMBER)
  // String
  val SYSTEMVERILOG_STRING = createTextAttributesKey("SYSTEMVERILOG_STRING", DLHC.STRING)
  // Keywords
  val SYSTEMVERILOG_DOLLAR_IDENTIFIER =
    createTextAttributesKey("SYSTEMVERILOG_DOLLAR_IDENTIFIER", DLHC.KEYWORD)
  val SYSTEMVERILOG_KEYWORD = createTextAttributesKey("SYSTEMVERILOG_KEYWORD", DLHC.KEYWORD)
  // Identifier
  val SYSTEMVERILOG_IDENTIFIER = createTextAttributesKey("SYSTEMVERILOG_IDENTIFIER", DLHC.IDENTIFIER)
}
