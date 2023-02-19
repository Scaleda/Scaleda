package top.criwits.scaleda.verilog.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import top.criwits.scaleda.verilog.VerilogLanguage$;
import top.criwits.scaleda.verilog.parser.VerilogParser;
import top.criwits.scaleda.verilog.psi.nodes.*;
import top.criwits.scaleda.verilog.psi.nodes.always.AlwaysConstructPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.always.DelayOrEventControlPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.instantiation.NamedPortConnectionPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.module.*;
import top.criwits.scaleda.verilog.psi.nodes.signal.*;

import java.util.HashMap;
import java.util.Map;

public class VerilogPsiNodeFactory {

  private static final Language LANGUAGE = VerilogLanguage$.MODULE$;

  private static final Map<IElementType, Class<? extends ANTLRPsiNode>> ruleIElementTypeClassMap =
      new HashMap<>();

  static {
    // Source file (start symbol)
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_source_text),
        SourceTextPsiNode.class
    );

    // Module
    /// Module keywords
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_keyword),
        ModuleKeywordPsiNode.class
    );
    /// Module declaration
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_declaration),
        ModuleDeclarationPsiNode.class
    );
    /// Module identifier
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_identifier),
        ModuleIdentifierPsiNode.class
    );
    /// Module parameter
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_parameter_port_list),
        ModuleParameterPortListPsiNode.class
    );
    /// Port declarations list
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_list_of_port_declarations),
        ListOfPortDeclarationsPsiNode.class
    );
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_list_of_ports),
        ListOfPortsPsiNode.class
    );
    /// Port declaration
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_port_declaration),
        PortDeclarationPsiNode.class
    );
    /// Port identifier
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_port_identifier),
        PortIdentifierPsiNode.class
    );
    /// Parameter declaration
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_parameter_declaration_), // this without ';'
        ParameterDeclarationPsiNode.class
    );
    /// Parameter identifier
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_identifier),
        IdentifierPsiNode.class
    );
    /// Inside module, module or generate item
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_or_generate_item_declaration),
        ModuleOrGenerateItemDeclarationPsiNode.class
    );

    // Always construct
    /// Always block
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_always_construct),
        AlwaysConstructPsiNode.class
    );
    /// Delay or event control
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_delay_or_event_control),
        DelayOrEventControlPsiNode.class
    );

    // Signals
    /// Net (wire, tri, etc.)
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_net_declaration),
        NetDeclarationPsiNode.class
    );

    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_net_identifier),
        NetIdentifierPsiNode.class
    );
    /// Variables (reg, integer, etc.)
    ruleIElementTypeClassMap.put(getRuleIElementType(VerilogParser.RULE_reg_declaration), VariableDeclarationPsiNode.class);
    ruleIElementTypeClassMap.put(getRuleIElementType(VerilogParser.RULE_integer_declaration), VariableDeclarationPsiNode.class);
    ruleIElementTypeClassMap.put(getRuleIElementType(VerilogParser.RULE_real_declaration), VariableDeclarationPsiNode.class);
    ruleIElementTypeClassMap.put(getRuleIElementType(VerilogParser.RULE_time_declaration), VariableDeclarationPsiNode.class);
    ruleIElementTypeClassMap.put(getRuleIElementType(VerilogParser.RULE_realtime_declaration), VariableDeclarationPsiNode.class);
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_variable_identifier),
        VariableIdentifierPsiNode.class
    );

    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_expression),
        ExpressionPsiNode.class
    );

    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_hierarchical_identifier),
        HierarchicalIdentifierPsiNode.class
    );

    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_module_instantiation),
        ModuleInstantiationPsiNode.class
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
    ruleIElementTypeClassMap.put(
        getRuleIElementType(VerilogParser.RULE_directive),
        DirectivePsiNode.class
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
