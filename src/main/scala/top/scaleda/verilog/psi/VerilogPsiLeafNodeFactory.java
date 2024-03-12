package top.scaleda.verilog.psi;

import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.jetbrains.annotations.NotNull;
import top.scaleda.verilog.VerilogLanguage$;
import top.scaleda.verilog.parser.VerilogLexer;
import top.scaleda.verilog.psi.nodes.SimpleIdentifierPsiLeafNode;
import top.scaleda.verilog.psi.nodes.expression.number.BinaryNumberPsiLeafNode;
import top.scaleda.verilog.psi.nodes.expression.number.DecimalNumberPsiLeafNode;
import top.scaleda.verilog.psi.nodes.expression.number.HexNumberPsiLeafNode;
import top.scaleda.verilog.psi.nodes.expression.number.OctalNumberPsiLeafNode;

import java.util.HashMap;
import java.util.Map;

public class VerilogPsiLeafNodeFactory {
  private static final Map<IElementType, Class<? extends ANTLRPsiLeafNode>> map =
      new HashMap<>();

  static {
    map.put(
        getTokenIElementType(VerilogLexer.Simple_identifier),
        SimpleIdentifierPsiLeafNode.class
    );
    map.put(
        getTokenIElementType(VerilogLexer.Decimal_number),
        DecimalNumberPsiLeafNode.class
    );
    map.put(
        getTokenIElementType(VerilogLexer.Hex_number),
        HexNumberPsiLeafNode.class
    );
    map.put(
        getTokenIElementType(VerilogLexer.Binary_number),
        BinaryNumberPsiLeafNode.class
    );
    map.put(
        getTokenIElementType(VerilogLexer.Octal_number),
        OctalNumberPsiLeafNode.class
    );


  }

  public static TokenIElementType getTokenIElementType(int tokenIndex) {
    return PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage$.MODULE$)
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
