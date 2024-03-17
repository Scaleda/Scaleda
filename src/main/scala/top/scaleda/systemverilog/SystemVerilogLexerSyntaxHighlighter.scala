package top.scaleda
package systemverilog

import systemverilog.parser.SystemVerilogLexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, TokenIElementType}

class SystemVerilogLexerSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getHighlightingLexer: Lexer =
    new ANTLRLexerAdaptor(SystemVerilogLanguage, new SystemVerilogLexer(null))

  override def getTokenHighlights(
      tokenType: IElementType
  ): Array[TextAttributesKey] = {
    val tokenIElementType = tokenType.asInstanceOf[TokenIElementType]
    val t = tokenIElementType.getANTLRTokenType
    import SystemVerilogLexerSyntaxHighlighter._
//    t match {
//      case SystemVerilogLexer.COMMENT | SystemVerilogLexer.COMMENT_INLINE =>
//        Array(
//          createTextAttributesKey(
//            "SYSTEMVERILOG_COMMENT",
//            DefaultLanguageHighlighterColors.LINE_COMMENT
//          )
//        )
//      case SystemVerilogLexer.CONST_INTEGER | SystemVerilogLexer.CONST_DOUBLE =>
//        Array(
//          createTextAttributesKey(
//            "SYSTEMVERILOG_NUMBER",
//            DefaultLanguageHighlighterColors.NUMBER
//          )
//        )
//      case n
//          if SystemVerilogKeywords.SYSTEMVERILOG_KEYWORDS
//            .map(name => getTypeForLiteralName(name))
//            .contains(n) =>
//        Array(
//          createTextAttributesKey(
//            "SYSTEMVERILOG_KEYWORD",
//            DefaultLanguageHighlighterColors.KEYWORD
//          )
//        )
//      case SystemVerilogLexer.CONST_STRING =>
//        Array(
//          createTextAttributesKey(
//            "SYSTEMVERILOG_STRING",
//            DefaultLanguageHighlighterColors.STRING
//          )
//        )
//      case SystemVerilogLexer.IDENTIFICADOR =>
//        Array(
//          createTextAttributesKey(
//            "SYSTEMVERILOG_IDENTIFICADOR",
//            DefaultLanguageHighlighterColors.IDENTIFIER
//          )
//        )
//      case _ => Array()
//    }
    Array()
  }
}

object SystemVerilogLexerSyntaxHighlighter {
  def getTypeForLiteralName(name: String): Int = {
    for (t <- 0 to SystemVerilogLexer.VOCABULARY.getMaxTokenType) {
      if (("'" + name + "'") == SystemVerilogLexer.VOCABULARY.getLiteralName(t)) return t
    }
    0
  }
}
