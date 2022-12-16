package top.criwits.scaleda.verilog.psi.factory;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import top.criwits.scaleda.verilog.VerilogLanguage$;
import top.criwits.scaleda.verilog.parser.VerilogParser;
import top.criwits.scaleda.verilog.psi.factory.nodes.*;

import java.util.HashMap;
import java.util.Map;

public class VerilogPsiNodeFactory {

    private static final Language LANGUAGE = VerilogLanguage$.MODULE$;

    private static final Map<IElementType, Class<? extends ANTLRPsiNode>> ruleIElementTypeClassMap =
            new HashMap<>();

    static {
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_identifier),
                IdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_module_or_generate_item_declaration),
                ModuleOrGenerateItemDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_port_declaration),
                PortDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_parameter_declaration),
                ParameterDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_hierarchical_identifier),
                HierarchicalIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_list_of_port_declarations),
                ListOfPortDeclarationsPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_reg_declaration),
                RegDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_net_declaration),
                NetDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_port_identifier),
                PortIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_net_identifier),
                NetIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_module_identifier),
                ModuleIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_module_instantiation),
                ModuleInstantiationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_module_declaration),
                ModuleDeclarationPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_variable_identifier),
                VariableIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_parameter_identifier),
                ParameterIdentifierPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_statement),
                StatementPsiNode.class
        );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_named_port_connection),
                NamedPortConnectionPsiNode.class
        );
        // TODO: directive?
        // ruleIElementTypeClassMap.put(
        //         getRuleIElementType(VerilogParser.RULE_directive),
        //         DirectivePsiNode.class
        // );
        ruleIElementTypeClassMap.put(
                getRuleIElementType(VerilogParser.RULE_source_text),
                SourceTextPsiNode.class
        );
    }

    private static RuleIElementType getRuleIElementType(int ruleIndex) {
        return PSIElementTypeFactory.getRuleIElementTypes(LANGUAGE)
                .get(ruleIndex);
    }

    public static ANTLRPsiNode create(ASTNode astNode) {
        if (ruleIElementTypeClassMap.containsKey(astNode.getElementType())) {
            try {
                return ruleIElementTypeClassMap.get(astNode.getElementType())
                        .getConstructor(ASTNode.class).newInstance(astNode);
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return new ANTLRPsiNode(astNode);
    }
}
