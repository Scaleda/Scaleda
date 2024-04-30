package top.scaleda.verilog;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import top.scaleda.verilog.parser.VerilogLexer;
import top.scaleda.verilog.parser.VerilogParser;
import top.scaleda.verilog.psi.VerilogPsiNodeFactory;

public final class VerilogParserDefinition implements ParserDefinition {
  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new ANTLRLexerAdaptor(VerilogLanguage.INSTANCE(), new VerilogLexer(null));
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new ANTLRParserAdaptor(VerilogLanguage.INSTANCE(), new VerilogParser(null)) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        if (root instanceof IFileElementType) {
          return ((VerilogParser) parser).source_text();
        } else {
          return ((VerilogParser) parser).primary();
        }
      }
    };
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return VerilogParserDefinition.FILE;
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return VerilogParserDefinition.WHITESPACE;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return VerilogParserDefinition.COMMENTS;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return VerilogParserDefinition.STRING;
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    // TODO: Keywords should have a space between them
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new VerilogPSIFileRoot(viewProvider);
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return VerilogPsiNodeFactory.create(node);
  }

  private static final IFileElementType FILE = new IFileElementType(VerilogLanguage.INSTANCE());
  private static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(
          VerilogLanguage.INSTANCE(),
          VerilogLexer.Block_comment,
          VerilogLexer.One_line_comment
  );
  private static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(
          VerilogLanguage.INSTANCE(),
          VerilogLexer.White_space
  );
  private static final TokenSet STRING = PSIElementTypeFactory.createTokenSet(
          VerilogLanguage.INSTANCE(),
          VerilogLexer.String
  );
}
