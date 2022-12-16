package top.criwits.scaleda.verilog.psi.factory;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import top.criwits.scaleda.verilog.VerilogLanguage$;
import top.criwits.scaleda.verilog.parser.VerilogLexer;
import top.criwits.scaleda.verilog.psi.factory.nodes.SimpleIdentifierPsiLeafNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VerilogPsiLeafNodeFactory {
    // private static Language LANGUAGE = ;

    private static Map<IElementType, Class<? extends ANTLRPsiLeafNode>> tokenIElementTypeClassMap =
            new HashMap<>();

    static {
        tokenIElementTypeClassMap.put(
                getTokenIElementType(VerilogLexer.SIMPLE_IDENTIFIER),
                SimpleIdentifierPsiLeafNode.class
        );
    }

    public static TokenIElementType getTokenIElementType(int tokenIndex) {
        return PSIElementTypeFactory.getTokenIElementTypes(VerilogLanguage$.MODULE$)
                .get(tokenIndex);
    }

    public static ANTLRPsiLeafNode create(@NotNull IElementType type, @NotNull CharSequence text) {
        if (tokenIElementTypeClassMap.containsKey(type)) {
            try {
                return tokenIElementTypeClassMap.get(type)
                        .getConstructor(CharSequence.class)
                        .newInstance(text);
            } catch (ReflectiveOperationException ignored) {}
        }
        return new ANTLRPsiLeafNode(type, text);
    }

    public static ANTLRPsiLeafNode create(int tokenIndex, @NotNull CharSequence charSequence) {
        return create(getTokenIElementType(tokenIndex), charSequence);
    }

}
