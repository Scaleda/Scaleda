package top.scaleda
package verilog

import verilog.parser._
import verilog.psi.VerilogPsiNodeFactory

import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.intellij.psi.{FileViewProvider, PsiFile}
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree

final class VerilogParserDefinition extends ParserDefinition {
  override def createLexer(project: Project): Lexer =
    new ANTLRLexerAdaptor(VerilogLanguage.INSTANCE, new VerilogLexer(null))

  override def createParser(project: Project): PsiParser =
    new ANTLRParserAdaptor(VerilogLanguage.INSTANCE, new VerilogParser(null)) {
      override def parse(parser: Parser, root: IElementType): ParseTree = {
        if (root.isInstanceOf[IFileElementType]) {
          parser.asInstanceOf[VerilogParser].source_text()
        } else parser.asInstanceOf[VerilogParser].primary()
      }
    }

  override def getWhitespaceTokens = VerilogParserDefinition.WHITESPACE

  override def getFileNodeType = VerilogParserDefinition.FILE

  override def getCommentTokens = VerilogParserDefinition.COMMENTS

  override def getStringLiteralElements = VerilogParserDefinition.STRING

  override def createElement(node: ASTNode) = VerilogPsiNodeFactory.create(node)

  override def createFile(viewProvider: FileViewProvider): PsiFile = new VerilogPSIFileRoot(viewProvider)

  override def spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements = {
    // TODO: Keywords should have a space between them
    (left.getElementType, right.getElementType) match {
      case _ => ParserDefinition.SpaceRequirements.MAY
    }
  }
}

//noinspection DuplicatedCode
object VerilogParserDefinition {
  private val vocabulary = VerilogParser.VOCABULARY
//  val tokenNames = (0 to vocabulary.getMaxTokenType) // LONG RINGS THE ALARM BELL
//    .map(x => (x, vocabulary.getLiteralName(x)))
//    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
//    .map(x => if (x._2 == null) "<INVALID>" else x._2)
//    .toArray
  PSIElementTypeFactory.defineLanguageIElementTypes(VerilogLanguage.INSTANCE, vocabulary, VerilogParser.ruleNames)
  val FILE = new IFileElementType(VerilogLanguage.INSTANCE)
  val COMMENTS = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage.INSTANCE,
    VerilogLexer.Block_comment,
    VerilogLexer.One_line_comment
  )
  val WHITESPACE = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage.INSTANCE,
    VerilogLexer.White_space
  )
  val STRING = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage.INSTANCE,
    VerilogLexer.String
  )
}
