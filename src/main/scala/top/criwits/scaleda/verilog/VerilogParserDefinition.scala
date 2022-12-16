package top.criwits.scaleda
package verilog

import verilog.parser._
import verilog.psi.factory.VerilogPsiNodeFactory

import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.{IElementType, IFileElementType, TokenSet}
import com.intellij.psi.{FileViewProvider, PsiElement, PsiFile}
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory, TokenIElementType}
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

  override def getFileNodeType: IFileElementType = VerilogParserDefinition.FILE

  override def getCommentTokens: TokenSet = VerilogParserDefinition.COMMENTS

  override def getStringLiteralElements: TokenSet = VerilogParserDefinition.STRING

  override def createElement(node: ASTNode): PsiElement = {
    // val elType = node.getElementType
    // if (elType.isInstanceOf[TokenIElementType]) return new ANTLRPsiNode(node)
    // if (!elType.isInstanceOf[RuleIElementType]) return new ANTLRPsiNode(node)
    // val ruleElType = elType.asInstanceOf[RuleIElementType]
    // ruleElType.getRuleIndex match {
    //   case VerilogParser.RULE_
    // }
    VerilogPsiNodeFactory.create(node)
  }

  override def createFile(viewProvider: FileViewProvider): PsiFile = new VerilogPSIFileRoot(viewProvider)
}

object VerilogParserDefinition {
  private val vocabulary = VerilogParser.VOCABULARY
  val tokenNames = (0 until vocabulary.getMaxTokenType)
    .map(x => (x, vocabulary.getLiteralName(x)))
    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
    .map(x => if (x._2 == null) "<INVALID>" else x._2).toArray
  PSIElementTypeFactory.defineLanguageIElementTypes(VerilogLanguage, tokenNames, VerilogParser.ruleNames)
  val tokenIElementTypes = PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage).toArray
  // TODO: check ID =?= SIMPLE_IDENTIFIER
  val ID = tokenIElementTypes(VerilogLexer.SIMPLE_IDENTIFIER)
  val FILE = new IFileElementType(VerilogLanguage)
  val COMMENTS = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.COMMENT,
    VerilogLexer.COMMENTS,
    // TODO: what XXX_NUMBER means?
    VerilogLexer.COMMENT_5,
  )
  val WHITESPACE = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.WHITE_SPACE,
    VerilogLexer.WHITE_SPACE_7,
  )
  val STRING = PSIElementTypeFactory.createTokenSet(
    VerilogLanguage,
    VerilogLexer.STRING
  )
}