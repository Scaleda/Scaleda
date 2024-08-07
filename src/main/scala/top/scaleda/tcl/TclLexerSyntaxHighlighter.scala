package top.scaleda
package tcl

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}
import top.scaleda.tcl.parser.TclLexer

class TclLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(TclLanguage.INSTANCE, new TclLexer(null))

  override def getTokenHighlights(
      tokenType: IElementType
  ): Array[TextAttributesKey] = {
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t = tokenIElementType.getANTLRTokenType
    import TclLexerSyntaxHighlighter._
    t match {
      case TclLexer.OneLineComment =>
        Array(
          createTextAttributesKey(
            "TCL_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
          )
        )
      case TclLexer.Text =>
        Array(
          createTextAttributesKey(
            "TCL_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
          )
        )
      case n
          if TclKeywords.
            TCL_KEYWORDS
            .map(name => getTypeForLiteralName(name))
            .contains(n) =>
        Array(
          createTextAttributesKey(
            "TCL_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
          )
        )
      case TclLexer.String =>
        Array(
          createTextAttributesKey(
            "TCL_STRING",
            DefaultLanguageHighlighterColors.STRING
          )
        )
      case TclLexer.Identifier =>
        Array(
          createTextAttributesKey(
            "TCL_IDENTIFICADOR",
            DefaultLanguageHighlighterColors.IDENTIFIER
          )
        )
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
