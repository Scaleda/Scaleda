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
  public TclParserDefinition() {
    PSIElementTypeFactory.defineLanguageIElementTypes(TclLanguage.INSTANCE(), TclParser.VOCABULARY, TclParser.ruleNames);
  }

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new ANTLRLexerAdaptor(TclLanguage.INSTANCE(), new TclLexer(null));
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new ANTLRParserAdaptor(TclLanguage.INSTANCE(), new TclParser(null)) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        return ((TclParser) parser).source_text();
      }
    };
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return new IFileElementType(TclLanguage.INSTANCE());
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return PSIElementTypeFactory.createTokenSet(
            TclLanguage.INSTANCE(),
            TclLexer.WhiteSpace
    );
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return PSIElementTypeFactory.createTokenSet(
            TclLanguage.INSTANCE(),
            TclLexer.OneLineComment
    );
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return PSIElementTypeFactory.createTokenSet(
            TclLanguage.INSTANCE(),
            TclLexer.String
    );
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
}
