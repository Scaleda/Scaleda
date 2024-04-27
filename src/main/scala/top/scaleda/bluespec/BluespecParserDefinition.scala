package top.scaleda
package bluespec

import bluespec.parser.{BluespecLexer, BluespecParser}

import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.intellij.psi.{FileViewProvider, PsiFile}
import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.v4.runtime.Parser

final class BluespecParserDefinition extends ParserDefinition {
  override def createLexer(project: Project): Lexer =
    new ANTLRLexerAdaptor(BluespecLanguage, new BluespecLexer(null))

  override def createParser(project: Project): PsiParser =
    new ANTLRParserAdaptor(BluespecLanguage, new BluespecParser(null)) {
      override def parse(parser: Parser, root: IElementType) = {
        parser.asInstanceOf[BluespecParser].top()
      }
    }

  override def getWhitespaceTokens = BluespecParserDefinition.WHITESPACE

  override def getFileNodeType = BluespecParserDefinition.FILE

  override def getCommentTokens = BluespecParserDefinition.COMMENTS

  override def getStringLiteralElements = BluespecParserDefinition.STRING

  override def createElement(node: ASTNode) = psi.BluespecPsiNodeFactory.create(node)

  override def createFile(viewProvider: FileViewProvider): PsiFile =
    new BluespecPSIFileRoot(viewProvider)

  override def spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements = {
    // TODO: Keywords should have a space between them
    (left.getElementType, right.getElementType) match {
      case _ => ParserDefinition.SpaceRequirements.MAY
    }
  }


}

//noinspection DuplicatedCode
object BluespecParserDefinition {
  private val vocabulary = BluespecParser.VOCABULARY
//  val tokenNames = (0 to vocabulary.getMaxTokenType) // LONG RINGS THE ALARM BELL
//    .map(x => (x, vocabulary.getLiteralName(x)))
//    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
//    .map(x => if (x._2 == null) "<INVALID>" else x._2)
//    .toArray
  PSIElementTypeFactory.defineLanguageIElementTypes(BluespecLanguage, vocabulary, BluespecParser.ruleNames)
  val FILE   = new IFileElementType(BluespecLanguage)
  val tokens = PSIElementTypeFactory.getTokenIElementTypes(BluespecLanguage)
  // BluespecLogger.Log.info("tokens: " + tokens.toArray.mkString(", "))
  val COMMENTS = PSIElementTypeFactory.createTokenSet(
    BluespecLanguage,
    BluespecLexer.BlockComment,
    BluespecLexer.LineComment
  )
  val WHITESPACE = PSIElementTypeFactory.createTokenSet(
    BluespecLanguage,
    BluespecLexer.Whitespace
  )
  val STRING = PSIElementTypeFactory.createTokenSet(
    BluespecLanguage,
    BluespecLexer.StringLiteral
  )
}
