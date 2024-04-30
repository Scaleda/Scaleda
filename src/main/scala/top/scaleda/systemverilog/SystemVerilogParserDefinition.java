package top.scaleda.systemverilog;

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
import top.scaleda.systemverilog.parser.SystemVerilogLexer;
import top.scaleda.systemverilog.parser.SystemVerilogParser;
import top.scaleda.systemverilog.psi.SystemVerilogPsiNodeFactory;

public final class SystemVerilogParserDefinition implements ParserDefinition {
  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new ANTLRLexerAdaptor(SystemVerilogLanguage.INSTANCE(), new SystemVerilogLexer(null));
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new ANTLRParserAdaptor(SystemVerilogLanguage.INSTANCE(), new SystemVerilogParser(null)) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        return ((SystemVerilogParser) parser).source_text();
      }
    };
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return SystemVerilogParserDefinition.FILE;
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return SystemVerilogParserDefinition.WHITESPACE;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return SystemVerilogParserDefinition.COMMENTS;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return SystemVerilogParserDefinition.STRING;
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    // TODO: Keywords should have a space between them
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new SystemVerilogPSIFileRoot(viewProvider);
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return SystemVerilogPsiNodeFactory.create(node);
  }

  private static final IFileElementType FILE = new IFileElementType(SystemVerilogLanguage.INSTANCE());
  private static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(
          SystemVerilogLanguage.INSTANCE(),
          SystemVerilogLexer.BLOCK_COMMENT,
          SystemVerilogLexer.LINE_COMMENT
  );
  private static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(
          SystemVerilogLanguage.INSTANCE(),
          SystemVerilogLexer.WHITE_SPACE
  );
  private static final TokenSet STRING = PSIElementTypeFactory.createTokenSet(
          SystemVerilogLanguage.INSTANCE(),
          SystemVerilogLexer.STRING_LITERAL
  );
}
