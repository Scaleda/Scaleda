package top.criwits.scaleda
package idea.verilog

import verilog.parser._
import verilog.psi.factory.VerilogPsiNodeFactory

import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.intellij.psi.{FileViewProvider, PsiFile}
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.v4.runtime.Parser

final class VerilogParserDefinition extends ParserDefinition {
  override def createLexer(project: Project): Lexer =
    new ANTLRLexerAdaptor(VerilogLanguage, new VerilogLexer(null))

  override def createParser(project: Project): PsiParser =
    new ANTLRParserAdaptor(VerilogLanguage, new VerilogParser(null)) {
      override def parse(parser: Parser, root: IElementType) = {
        parser.asInstanceOf[VerilogParser].source_text()
      }
    }

  override def getWhitespaceTokens = VerilogParserDefinition.WHITESPACE

  override def getFileNodeType = VerilogParserDefinition.FILE

  override def getCommentTokens = VerilogParserDefinition.COMMENTS

  override def getStringLiteralElements = VerilogParserDefinition.STRING

  override def createElement(node: ASTNode) = VerilogPsiNodeFactory.create(node)

  override def createFile(viewProvider: FileViewProvider): PsiFile = new VerilogPSIFileRoot(viewProvider)

  override def spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode) = SpaceRequirements.MAY
}

object VerilogParserDefinition {
  private val vocabulary = VerilogParser.VOCABULARY
  val tokenNames = (0 until vocabulary.getMaxTokenType)
    .map(x => (x, vocabulary.getLiteralName(x)))
    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
    .map(x => if (x._2 == null) "<INVALID>" else x._2).toArray
  PSIElementTypeFactory.defineLanguageIElementTypes(VerilogLanguage, tokenNames, VerilogParser.ruleNames)
  val FILE = new IFileElementType(VerilogLanguage)
  val COMMENTS = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.Block_comment,
    VerilogLexer.One_line_comment,
  )
  val WHITESPACE = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.White_space,
  )
  val STRING = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.String
  )
}