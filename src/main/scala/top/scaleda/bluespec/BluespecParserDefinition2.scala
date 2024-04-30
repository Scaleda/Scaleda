// package top.scaleda
// package bluespec
//
// import bluespec.parser.{BluespecLexer, BluespecParser}
//
// import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
// import com.intellij.lexer.Lexer
// import com.intellij.openapi.project.Project
// import com.intellij.psi.tree.{IElementType, IFileElementType}
// import com.intellij.psi.{FileViewProvider, PsiFile}
// import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
// import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
// import org.antlr.v4.runtime.Parser
//
// class BluespecParserDefinition2 extends ParserDefinition {
//   override def createLexer(project: Project): Lexer =
//     new ANTLRLexerAdaptor(BluespecLanguage.INSTANCE, new BluespecLexer(null))
//
//   override def createParser(project: Project): PsiParser =
//     new ANTLRParserAdaptor(BluespecLanguage.INSTANCE, new BluespecParser(null)) {
//       override def parse(parser: Parser, root: IElementType) = {
//         parser.asInstanceOf[BluespecParser].top()
//       }
//     }
//
//   override def getWhitespaceTokens = WHITESPACE
//
//   override def getFileNodeType = FILE
//
//   override def getCommentTokens = COMMENTS
//
//   override def getStringLiteralElements = STRING
//
//   override def createElement(node: ASTNode) = psi.BluespecPsiNodeFactory.create(node)
//
//   override def createFile(viewProvider: FileViewProvider): PsiFile =
//     new BluespecPSIFileRoot(viewProvider)
//
//   override def spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements = {
//     // TODO: Keywords should have a space between them
//     (left.getElementType, right.getElementType) match {
//       case _ => ParserDefinition.SpaceRequirements.MAY
//     }
//   }
//
//   private val vocabulary = BluespecParser.VOCABULARY
//   //  val tokenNames = (0 to vocabulary.getMaxTokenType) // LONG RINGS THE ALARM BELL
//   //    .map(x => (x, vocabulary.getLiteralName(x)))
//   //    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
//   //    .map(x => if (x._2 == null) "<INVALID>" else x._2)
//   //    .toArray
//   PSIElementTypeFactory.defineLanguageIElementTypes(BluespecLanguage.INSTANCE, vocabulary, BluespecParser.ruleNames)
//   private val FILE   = new IFileElementType(BluespecLanguage.INSTANCE)
//   private val tokens = PSIElementTypeFactory.getTokenIElementTypes(BluespecLanguage.INSTANCE)
//   // BluespecLogger.Log.info("tokens: " + tokens.toArray.mkString(", "))
//   private val COMMENTS = PSIElementTypeFactory.createTokenSet(
//     BluespecLanguage.INSTANCE,
//     BluespecLexer.BlockComment,
//     BluespecLexer.LineComment
//   )
//   private val WHITESPACE = PSIElementTypeFactory.createTokenSet(
//     BluespecLanguage.INSTANCE,
//     BluespecLexer.Whitespace
//   )
//   private val STRING = PSIElementTypeFactory.createTokenSet(
//     BluespecLanguage.INSTANCE,
//     BluespecLexer.StringLiteral
//   )
// }
