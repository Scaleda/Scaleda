package top.scaleda.bluespec;

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
import top.scaleda.bluespec.parser.BluespecLexer;
import top.scaleda.bluespec.parser.BluespecParser;
import top.scaleda.bluespec.psi.BluespecPsiNodeFactory;

public class BluespecParserDefinition implements ParserDefinition {
  public BluespecParserDefinition() {
    PSIElementTypeFactory.defineLanguageIElementTypes(BluespecLanguage.INSTANCE(), BluespecParser.VOCABULARY, BluespecParser.ruleNames);
  }

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new ANTLRLexerAdaptor(BluespecLanguage.INSTANCE(), new BluespecLexer(null));
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new ANTLRParserAdaptor(BluespecLanguage.INSTANCE(), new BluespecParser(null)) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        return ((BluespecParser) parser).top();
      }
    };
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return new IFileElementType(BluespecLanguage.INSTANCE());
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return PSIElementTypeFactory.createTokenSet(
            BluespecLanguage.INSTANCE(),
            BluespecLexer.Whitespace
    );
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return PSIElementTypeFactory.createTokenSet(
            BluespecLanguage.INSTANCE(),
            BluespecLexer.BlockComment,
            BluespecLexer.LineComment
    );
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return PSIElementTypeFactory.createTokenSet(
            BluespecLanguage.INSTANCE(),
            BluespecLexer.StringLiteral
    );
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    // TODO: Keywords should have a space between them
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new BluespecPSIFileRoot(viewProvider);
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return BluespecPsiNodeFactory.create(node);
  }
}
