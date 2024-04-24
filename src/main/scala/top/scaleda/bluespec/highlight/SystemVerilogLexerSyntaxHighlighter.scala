package top.scaleda
package bluespec.highlight

import bluespec.parser.BluespecLexer
import bluespec.{BluespecKeywords, BluespecLanguage}

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.editor.{DefaultLanguageHighlighterColors => DLHC}
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

/** Syntax highlighter for Bluespec
  *
  * Registered by [[BluespecSyntaxHighlighterFactory]], providing lexer-based highlighting functionality.
  * Note that grammar-based highlighting is implemented by [[BluespecHighlightingAnnotator]].
  */
class BluespecLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(BluespecLanguage, new BluespecLexer(null))

  override def getTokenHighlights(tokenType: IElementType): Array[TextAttributesKey] = {
    if (!tokenType.isInstanceOf[TokenIElementType]) return Array.empty
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t                 = tokenIElementType.getANTLRTokenType
    import BluespecLexerSyntaxHighlighter._
    t match {
      case BluespecLexer.BlockComment           => Array(BLUESPEC_BLOCK_COMMENT)
      case BluespecLexer.LineComment            => Array(BLUESPEC_LINE_COMMENT)
      case n if n == getTypeForLiteralName(";") => Array(BLUESPEC_COMMA)
      case n if n == getTypeForLiteralName(",") => Array(BLUESPEC_SEMICOLON)
      // case BluespecLexer.DECIMAL_NUMBER_MODE => Array(BLUESPEC_NUMBER)
      // case BluespecLexer.Dollar_identifier => Array(BLUESPEC_DOLLAR_IDENTIFIER)
      case n if BluespecKeywords.BLUESPEC_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(BLUESPEC_KEYWORD)
      case BluespecLexer.StringLiteral => Array(BLUESPEC_STRING)
      case BluespecLexer.Identifier    => Array(BLUESPEC_IDENTIFIER)
      case _                           => Array()
    }
  }
}

object BluespecLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to BluespecLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == BluespecLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }

  // Directive
  val BLUESPEC_DIRECTIVE = createTextAttributesKey("BLUESPEC_DIRECTIVE", DLHC.METADATA)

  // Braces and Operators
  val BLUESPEC_SEMICOLON = createTextAttributesKey("BLUESPEC_SEMICOLON", DLHC.SEMICOLON)
  val BLUESPEC_COMMA     = createTextAttributesKey("BLUESPEC_COMMA", DLHC.COMMA)

  // Signals
  val BLUESPEC_SIGNAL_NET       = createTextAttributesKey("BLUESPEC_SIGNAL_NET", DLHC.STATIC_FIELD)
  val BLUESPEC_SIGNAL_REG       = createTextAttributesKey("BLUESPEC_SIGNAL_REG", DLHC.INSTANCE_FIELD)
  val BLUESPEC_SIGNAL_PARAMETER = createTextAttributesKey("BLUESPEC_SIGNAL_PARAMETER", DLHC.PARAMETER)

  // Module
  val BLUESPEC_MODULE_IDENTIFIER = createTextAttributesKey("BLUESPEC_MODULE_IDENTIFIER", DLHC.CLASS_NAME)
  val BLUESPEC_MODULE_INSTANTIATION =
    createTextAttributesKey("BLUESPEC_MODULE_INSTANTIATION", DLHC.FUNCTION_DECLARATION)
  val BLUESPEC_PORT_INPUT      = createTextAttributesKey("BLUESPEC_PORT_INPUT", BLUESPEC_SIGNAL_NET)
  val BLUESPEC_PORT_INOUT      = createTextAttributesKey("BLUESPEC_PORT_INOUT", BLUESPEC_SIGNAL_NET)
  val BLUESPEC_PORT_OUTPUT     = createTextAttributesKey("BLUESPEC_PORT_OUTPUT", BLUESPEC_SIGNAL_NET)
  val BLUESPEC_PORT_OUTPUT_REG = createTextAttributesKey("BLUESPEC_PORT_OUTPUT_REG", BLUESPEC_SIGNAL_REG)

  // Comments
  val BLUESPEC_BLOCK_COMMENT =
    createTextAttributesKey("BLUESPEC_BLOCK_COMMENT", DLHC.BLOCK_COMMENT)
  val BLUESPEC_LINE_COMMENT =
    createTextAttributesKey("BLUESPEC_LINE_COMMENT", DLHC.LINE_COMMENT)

  // Numbers
  val BLUESPEC_NUMBER = createTextAttributesKey("BLUESPEC_NUMBER", DLHC.NUMBER)
  // String
  val BLUESPEC_STRING = createTextAttributesKey("BLUESPEC_STRING", DLHC.STRING)
  // Keywords
  val BLUESPEC_DOLLAR_IDENTIFIER =
    createTextAttributesKey("BLUESPEC_DOLLAR_IDENTIFIER", DLHC.KEYWORD)
  val BLUESPEC_KEYWORD = createTextAttributesKey("BLUESPEC_KEYWORD", DLHC.KEYWORD)
  // Identifier
  val BLUESPEC_IDENTIFIER = createTextAttributesKey("BLUESPEC_IDENTIFIER", DLHC.IDENTIFIER)
}
