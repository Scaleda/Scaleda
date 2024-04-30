package top.scaleda
package bluespec

import bluespec.parser.BluespecLexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

class BluespecLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(BluespecLanguage.INSTANCE, new BluespecLexer(null))

  override def getTokenHighlights(
      tokenType: IElementType
  ): Array[TextAttributesKey] = {
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t = tokenIElementType.getANTLRTokenType
//    t match {
//      case BluespecLexer.COMMENT | BluespecLexer.COMMENT_INLINE =>
//        Array(
//          createTextAttributesKey(
//            "BLUESPEC_COMMENT",
//            DefaultLanguageHighlighterColors.LINE_COMMENT
//          )
//        )
//      case BluespecLexer.CONST_INTEGER | BluespecLexer.CONST_DOUBLE =>
//        Array(
//          createTextAttributesKey(
//            "BLUESPEC_NUMBER",
//            DefaultLanguageHighlighterColors.NUMBER
//          )
//        )
//      case n
//          if BluespecKeywords.BLUESPEC_KEYWORDS
//            .map(name => getTypeForLiteralName(name))
//            .contains(n) =>
//        Array(
//          createTextAttributesKey(
//            "BLUESPEC_KEYWORD",
//            DefaultLanguageHighlighterColors.KEYWORD
//          )
//        )
//      case BluespecLexer.CONST_STRING =>
//        Array(
//          createTextAttributesKey(
//            "BLUESPEC_STRING",
//            DefaultLanguageHighlighterColors.STRING
//          )
//        )
//      case BluespecLexer.IDENTIFICADOR =>
//        Array(
//          createTextAttributesKey(
//            "BLUESPEC_IDENTIFICADOR",
//            DefaultLanguageHighlighterColors.IDENTIFIER
//          )
//        )
//      case _ => Array()
//    }
    Array()
  }
}

object BluespecLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to BluespecLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == BluespecLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }
}
