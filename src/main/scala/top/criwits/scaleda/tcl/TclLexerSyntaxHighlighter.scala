package top.criwits.scaleda
package tcl

import tcl.parser.TclLexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

class TclLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(TclLanguage, new TclLexer(null))

  override def getTokenHighlights(tokenType: IElementType): Array[TextAttributesKey] = {
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t = tokenIElementType.getANTLRTokenType
    import TclLexerSyntaxHighlighter._
    t match {
      case TclLexer.Block_comment => Array(createTextAttributesKey(
        "TCL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT))
      case TclLexer.One_line_comment => Array(createTextAttributesKey(
        "TCL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT))
      case n if n == getTypeForLiteralName(";") => Array(createTextAttributesKey(
        "TCL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON))
      case n if n == getTypeForLiteralName(",") => Array(createTextAttributesKey(
        "TCL_COMMA", DefaultLanguageHighlighterColors.COMMA))
      case
        TclLexer.Binary_number |
        TclLexer.Octal_number |
        TclLexer.Real_number |
        TclLexer.Decimal_number |
        TclLexer.Hex_number
      => Array(createTextAttributesKey(
        "TCL_NUMBER", DefaultLanguageHighlighterColors.NUMBER))
      case TclLexer.Dollar_identifier => Array(createTextAttributesKey(
        "TCL_DOLLAR_IDENTIFIER", DefaultLanguageHighlighterColors.KEYWORD))
      case n if TclKeywords.TCL_KEYWORDS.map(name => getTypeForLiteralName(name)).contains(n) =>
        Array(createTextAttributesKey(
          "TCL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD))
      case TclLexer.String => Array(createTextAttributesKey(
        "TCL_STRING", DefaultLanguageHighlighterColors.STRING))
      case TclLexer.Simple_identifier | TclLexer.Time_identifier | TclLexer.Escaped_identifier => Array(createTextAttributesKey(
        "TCL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER))
      case _ => Array()
    }
  }
}

object TclLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to TclLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == TclLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }
}