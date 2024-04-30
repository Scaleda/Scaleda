package top.scaleda.tcl;

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
import top.scaleda.tcl.parser.TclLexer;
import top.scaleda.tcl.parser.TclParser;
import top.scaleda.tcl.psi.factory.TclPsiNodeFactory;

public final class TclParserDefinition implements ParserDefinition {
  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new ANTLRLexerAdaptor(TclLanguage.INSTANCE(), new TclLexer(null));
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new ANTLRParserAdaptor(TclLanguage.INSTANCE(), new TclParser(null)) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        return ((TclParser) parser).inicio();
      }
    };
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return TclParserDefinition.FILE;
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return TclParserDefinition.WHITESPACE;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return TclParserDefinition.COMMENTS;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return TclParserDefinition.STRING;
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new TclPSIFileRoot(viewProvider);
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return TclPsiNodeFactory.create(node);
  }

  private static final IFileElementType FILE = new IFileElementType(TclLanguage.INSTANCE());
  private static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(
          TclLanguage.INSTANCE(),
          TclLexer.COMMENT,
          TclLexer.COMMENT_INLINE
  );
  private static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(
          TclLanguage.INSTANCE(),
          TclLexer.WS
  );
  private static final TokenSet STRING = PSIElementTypeFactory.createTokenSet(
          TclLanguage.INSTANCE(),
          TclLexer.CONST_STRING
  );
}
