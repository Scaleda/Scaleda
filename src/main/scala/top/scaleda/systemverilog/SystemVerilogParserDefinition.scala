package top.scaleda
package systemverilog

import tcl.parser.{TclLexer, TclParser}
import tcl.psi.factory.TclPsiNodeFactory
import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.intellij.psi.{FileViewProvider, PsiFile}
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.v4.runtime.Parser
import top.scaleda.tcl.parser.{TclLexer, TclParser}
import top.scaleda.tcl.psi.factory.TclPsiNodeFactory

final class TclParserDefinition extends ParserDefinition {
  override def createLexer(project: Project): Lexer =
    new ANTLRLexerAdaptor(SystemVerilogLanguage, new TclLexer(null))

  override def createParser(project: Project): PsiParser =
    new ANTLRParserAdaptor(SystemVerilogLanguage, new TclParser(null)) {
      override def parse(parser: Parser, root: IElementType) = {
        // TODO: check this
        parser.asInstanceOf[TclParser].inicio()
      }
    }

  override def getWhitespaceTokens = TclParserDefinition.WHITESPACE

  override def getFileNodeType = TclParserDefinition.FILE

  override def getCommentTokens = TclParserDefinition.COMMENTS

  override def getStringLiteralElements = TclParserDefinition.STRING

  override def createElement(node: ASTNode) = TclPsiNodeFactory.create(node)

  override def createFile(viewProvider: FileViewProvider): PsiFile =
    new SystemVerilogPSIFileRoot(viewProvider)
}

//noinspection DuplicatedCode
object TclParserDefinition {
  private val vocabulary = TclParser.VOCABULARY
//  val tokenNames = (0 to vocabulary.getMaxTokenType) // LONG RINGS THE ALARM BELL
//    .map(x => (x, vocabulary.getLiteralName(x)))
//    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
//    .map(x => if (x._2 == null) "<INVALID>" else x._2)
//    .toArray
  PSIElementTypeFactory.defineLanguageIElementTypes(
    SystemVerilogLanguage,
    // tokenNames :+ "<INVALID>",
    vocabulary,
    TclParser.ruleNames
  )
  val FILE = new IFileElementType(SystemVerilogLanguage)
  val tokens = PSIElementTypeFactory.getTokenIElementTypes(SystemVerilogLanguage)
  TclLogger.Log.info("tokens: " + tokens.toArray.mkString(", "))
  val COMMENTS = PSIElementTypeFactory.createTokenSet(
    SystemVerilogLanguage,
    TclLexer.COMMENT,
    TclLexer.COMMENT_INLINE,
  )
  val WHITESPACE = PSIElementTypeFactory.createTokenSet(
    SystemVerilogLanguage,
    TclLexer.WS
  )
  val STRING = PSIElementTypeFactory.createTokenSet(
    SystemVerilogLanguage,
    TclLexer.CONST_STRING
  )
}
