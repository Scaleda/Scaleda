package top.scaleda.systemverilog.psi;

import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.jetbrains.annotations.NotNull;
import top.scaleda.systemverilog.SystemVerilogLanguage$;
import top.scaleda.systemverilog.parser.SystemVerilogLexer;
import top.scaleda.systemverilog.psi.nodes.SimpleIdentifierPsiLeafNode;
import top.scaleda.systemverilog.psi.nodes.expression.number.BinaryNumberPsiLeafNode;
import top.scaleda.systemverilog.psi.nodes.expression.number.DecimalNumberPsiLeafNode;
import top.scaleda.systemverilog.psi.nodes.expression.number.HexNumberPsiLeafNode;
import top.scaleda.systemverilog.psi.nodes.expression.number.OctalNumberPsiLeafNode;

import java.util.HashMap;
import java.util.Map;

public class SystemVerilogPsiLeafNodeFactory {
  private static final Map<IElementType, Class<? extends ANTLRPsiLeafNode>> map =
          new HashMap<>();

  static {
    map.put(
            getTokenIElementType(SystemVerilogLexer.SIMPLE_IDENTIFIER),
            SimpleIdentifierPsiLeafNode.class
    );
    map.put(
            getTokenIElementType(SystemVerilogLexer.DECIMAL_NUMBER_MODE),
            DecimalNumberPsiLeafNode.class
    );
    map.put(
            getTokenIElementType(SystemVerilogLexer.HEX_NUMBER_MODE),
            HexNumberPsiLeafNode.class
    );
    map.put(
            getTokenIElementType(SystemVerilogLexer.BINARY_NUMBER_MODE),
            BinaryNumberPsiLeafNode.class
    );
    map.put(
            getTokenIElementType(SystemVerilogLexer.OCTAL_NUMBER_MODE),
            OctalNumberPsiLeafNode.class
    );


  }

  public static TokenIElementType getTokenIElementType(int tokenIndex) {
    return PSIElementTypeFactory.getTokenIElementTypes(SystemVerilogLanguage$.MODULE$.INSTANCE())
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
