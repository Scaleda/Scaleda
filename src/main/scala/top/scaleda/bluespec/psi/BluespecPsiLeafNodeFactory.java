package top.scaleda.bluespec.psi;

import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.jetbrains.annotations.NotNull;
import top.scaleda.bluespec.psi.nodes.IdentifierPsiLeafNode;
// import top.scaleda.bluespec.psi.nodes.expression.number.BinaryNumberPsiLeafNode;
// import top.scaleda.bluespec.psi.nodes.expression.number.DecimalNumberPsiLeafNode;
// import top.scaleda.bluespec.psi.nodes.expression.number.HexNumberPsiLeafNode;
// import top.scaleda.bluespec.psi.nodes.expression.number.OctalNumberPsiLeafNode;
import top.scaleda.bluespec.BluespecLanguage$;
import top.scaleda.bluespec.parser.BluespecLexer;

import java.util.HashMap;
import java.util.Map;

public class BluespecPsiLeafNodeFactory {
  private static final Map<IElementType, Class<? extends ANTLRPsiLeafNode>> map =
          new HashMap<>();

  static {
    map.put(
            getTokenIElementType(BluespecLexer.Identifier),
            IdentifierPsiLeafNode.class
    );
    // map.put(
    //         getTokenIElementType(BluespecLexer.DECIMAL_NUMBER_MODE),
    //         DecimalNumberPsiLeafNode.class
    // );
    // map.put(
    //         getTokenIElementType(BluespecLexer.HEX_NUMBER_MODE),
    //         HexNumberPsiLeafNode.class
    // );
    // map.put(
    //         getTokenIElementType(BluespecLexer.BINARY_NUMBER_MODE),
    //         BinaryNumberPsiLeafNode.class
    // );
    // map.put(
    //         getTokenIElementType(BluespecLexer.OCTAL_NUMBER_MODE),
    //         OctalNumberPsiLeafNode.class
    // );


  }

  public static TokenIElementType getTokenIElementType(int tokenIndex) {
    return PSIElementTypeFactory.getTokenIElementTypes(BluespecLanguage$.MODULE$)
            .get(tokenIndex);
  }

  public static ANTLRPsiLeafNode create(@NotNull IElementType type, @NotNull CharSequence text) {
    if (map.containsKey(type)) {
      try {
        return map.get(type)
                .getConstructor(CharSequence.class)
                .newInstance(text);
      } catch (ReflectiveOperationException ignored) {
      }
    }
    return new ANTLRPsiLeafNode(type, text);
  }

  public static ANTLRPsiLeafNode create(int tokenIndex, @NotNull CharSequence charSequence) {
    return create(getTokenIElementType(tokenIndex), charSequence);
  }

}
