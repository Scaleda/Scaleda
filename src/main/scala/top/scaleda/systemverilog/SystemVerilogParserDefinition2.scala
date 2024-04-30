// package top.scaleda
// package systemverilog
//
// import systemverilog.parser.{SystemVerilogLexer, SystemVerilogParser}
// import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
// import com.intellij.lexer.Lexer
// import com.intellij.openapi.project.Project
// import com.intellij.psi.tree.{IElementType, IFileElementType}
// import com.intellij.psi.{FileViewProvider, PsiFile}
// import org.antlr.intellij.adaptor.lexer.{ANTLRLexerAdaptor, PSIElementTypeFactory}
// import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
// import org.antlr.v4.runtime.Parser
// import top.scaleda.systemverilog.psi.SystemVerilogPsiNodeFactory
//
// final class SystemVerilogParserDefinition2 extends ParserDefinition {
//   override def createLexer(project: Project): Lexer =
//     new ANTLRLexerAdaptor(SystemVerilogLanguage.INSTANCE, new SystemVerilogLexer(null))
//
//   override def createParser(project: Project): PsiParser =
//     new ANTLRParserAdaptor(SystemVerilogLanguage.INSTANCE, new SystemVerilogParser(null)) {
//       override def parse(parser: Parser, root: IElementType) = {
//         parser.asInstanceOf[SystemVerilogParser].source_text()
//       }
//     }
//
//   override def getWhitespaceTokens = SystemVerilogParserDefinition2.WHITESPACE
//
//   override def getFileNodeType = SystemVerilogParserDefinition2.FILE
//
//   override def getCommentTokens = SystemVerilogParserDefinition2.COMMENTS
//
//   override def getStringLiteralElements = SystemVerilogParserDefinition2.STRING
//
//   override def createElement(node: ASTNode) = SystemVerilogPsiNodeFactory.create(node)
//
//   override def createFile(viewProvider: FileViewProvider): PsiFile =
//     new SystemVerilogPSIFileRoot(viewProvider)
//
//   override def spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements = {
//     // TODO: Keywords should have a space between them
//     (left.getElementType, right.getElementType) match {
//       case _ => ParserDefinition.SpaceRequirements.MAY
//     }
//   }
// }
//
// //noinspection DuplicatedCode
// object SystemVerilogParserDefinition2 {
//   private val vocabulary = SystemVerilogParser.VOCABULARY
// //  val tokenNames = (0 to vocabulary.getMaxTokenType) // LONG RINGS THE ALARM BELL
// //    .map(x => (x, vocabulary.getLiteralName(x)))
// //    .map(x => (x, if (x._2 == null) vocabulary.getSymbolicName(x._1) else x._2))
// //    .map(x => if (x._2 == null) "<INVALID>" else x._2)
// //    .toArray
//   PSIElementTypeFactory.defineLanguageIElementTypes(SystemVerilogLanguage.INSTANCE, vocabulary, SystemVerilogParser.ruleNames)
//   val FILE   = new IFileElementType(SystemVerilogLanguage.INSTANCE)
//   val tokens = PSIElementTypeFactory.getTokenIElementTypes(SystemVerilogLanguage.INSTANCE)
//   // SystemVerilogLogger.Log.info("tokens: " + tokens.toArray.mkString(", "))
//   val COMMENTS = PSIElementTypeFactory.createTokenSet(
//     SystemVerilogLanguage.INSTANCE,
//     SystemVerilogLexer.BLOCK_COMMENT,
//     SystemVerilogLexer.LINE_COMMENT
//   )
//   val WHITESPACE = PSIElementTypeFactory.createTokenSet(
//     SystemVerilogLanguage.INSTANCE,
//     SystemVerilogLexer.WHITE_SPACE
//   )
//   val STRING = PSIElementTypeFactory.createTokenSet(
//     SystemVerilogLanguage.INSTANCE,
//     SystemVerilogLexer.STRING_LITERAL
//   )
// }
