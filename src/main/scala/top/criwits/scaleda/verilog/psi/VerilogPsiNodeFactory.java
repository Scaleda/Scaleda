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
import top.criwits.scaleda.verilog.psi.nodes.always.*;
import top.criwits.scaleda.verilog.psi.nodes.block.CaseBodyPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.block.CaseDefaultPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.block.CaseStatementPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.condition.ConditionalStatementPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.block.SeqBlockPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.condition.ConditionalStatementChainPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.condition.ConditionalStatementHeadPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.condition.ConditionalStatementTailPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.expression.*;
import top.criwits.scaleda.verilog.psi.nodes.incompleted.IncompleteConditionPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.instantiation.*;
import top.criwits.scaleda.verilog.psi.nodes.macro.MacroDeclarationPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.macro.MacroIdentifierPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.module.*;
import top.criwits.scaleda.verilog.psi.nodes.signal.*;
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ListOfParamAssignmentsPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ParameterAssignmentPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ParameterDeclarationPsiNode;
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode;

import java.util.HashMap;
import java.util.Map;

public class VerilogPsiNodeFactory {

  private static final Language LANGUAGE = VerilogLanguage$.MODULE$;

  private static final Map<IElementType, Class<? extends ANTLRPsiNode>> map =
      new HashMap<>();

  static {
    // Source file (start symbol)
    map.put(
        getRuleIElementType(VerilogParser.RULE_source_text),
        SourceTextPsiNode.class
    );

    // Module
    /// Module keywords
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_keyword),
        ModuleKeywordPsiNode.class
    );
    /// Module declaration
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_declaration),
        ModuleDeclarationPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_head),
        ModuleHeadPsiNode.class
    );

    /// Module identifier
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_identifier),
        ModuleIdentifierPsiNode.class
    );
    /// Module parameter
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_parameter_port_list),
        ModuleParameterPortListPsiNode.class
    );
    /// Port declarations list
    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_port_declarations),
        ListOfPortDeclarationsPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_ports),
        ListOfPortsPsiNode.class
    );
    /// Port declaration
    map.put(
        getRuleIElementType(VerilogParser.RULE_port_declaration),
        PortDeclarationPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_output_declaration),
        OutputDeclarationPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_input_declaration),
        InputDeclarationPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_inout_declaration),
        InoutDeclarationPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_port_identifiers),
        ListOfPortIdentifiersPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_variable_port_identifiers),
        ListOfVariablePortIdentifiersPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_port),
        PortPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_port_expression),
        PortExpressionPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_port_reference),
        PortReferencePsiNode.class
    );
    /// Port identifier
    map.put(
        getRuleIElementType(VerilogParser.RULE_port_identifier),
        PortIdentifierPsiNode.class
    );
    /// Parameter declaration
    map.put(
        getRuleIElementType(VerilogParser.RULE_parameter_declaration_), // this without ';'
        ParameterDeclarationPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_parameter_identifier),
        ParameterIdentifierPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_param_assignment),
        ParameterAssignmentPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_param_assignments),
        ListOfParamAssignmentsPsiNode.class
    );

    /// Inside module, module or generate item
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_item),
        ModuleItemPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_module_or_generate_item_declaration),
        ModuleOrGenerateItemDeclarationPsiNode.class
    );

    // Always construct
    /// Always block
    map.put(
        getRuleIElementType(VerilogParser.RULE_always_construct),
        AlwaysConstructPsiNode.class
    );
    /// Delay or event control
    map.put(
        getRuleIElementType(VerilogParser.RULE_delay_or_event_control),
        DelayOrEventControlPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_delay_value),
        DelayValuePsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_event_primary),
        EventPrimaryPsiNode.class
    );
    /// Assignment
    map.put(
        getRuleIElementType(VerilogParser.RULE_nonblocking_assignment),
        NonblockingAssignmentPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_blocking_assignment),
        BlockingAssignmentPsiNode.class
    );

    // Signals
    /// Net (wire, tri, etc.)
    map.put(
        getRuleIElementType(VerilogParser.RULE_net_declaration),
        NetDeclarationPsiNode.class
    );

    // conditional statements
    /// if-head
    map.put(
      getRuleIElementType(VerilogParser.RULE_conditional_statement_head),
      ConditionalStatementHeadPsiNode.class
    );
    /// else-if-chain
    map.put(
      getRuleIElementType(VerilogParser.RULE_conditional_statement_else_chain),
      ConditionalStatementChainPsiNode.class
    );
    /// if-tail
    map.put(
      getRuleIElementType(VerilogParser.RULE_conditional_statement_else_tail),
      ConditionalStatementTailPsiNode.class
    );
    /// all if block
    map.put(
      getRuleIElementType(VerilogParser.RULE_conditional_statement),
      ConditionalStatementPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_net_identifier),
        NetIdentifierPsiNode.class
    );
    /// Variables (reg, integer, etc.)
    /// Identifier
    map.put(
        getRuleIElementType(VerilogParser.RULE_identifier),
        IdentifierPsiNode.class
    );

    map.put(getRuleIElementType(VerilogParser.RULE_reg_declaration), VariableDeclarationPsiNode.class);
    map.put(getRuleIElementType(VerilogParser.RULE_integer_declaration), VariableDeclarationPsiNode.class);
    map.put(getRuleIElementType(VerilogParser.RULE_real_declaration), VariableDeclarationPsiNode.class);
    map.put(getRuleIElementType(VerilogParser.RULE_time_declaration), VariableDeclarationPsiNode.class);
    map.put(getRuleIElementType(VerilogParser.RULE_realtime_declaration), VariableDeclarationPsiNode.class);
    map.put(
        getRuleIElementType(VerilogParser.RULE_variable_identifier),
        VariableIdentifierPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_variable_type),
        VariableTypePsiNode.class
    );
    /// Range & Dimension
    map.put(
        getRuleIElementType(VerilogParser.RULE_range_),
        RangePsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_dimension),
        DimensionPsiNode.class
    );


    // Macros
    /// define_directive
    map.put(
        getRuleIElementType(VerilogParser.RULE_define_directive),
        MacroDeclarationPsiNode.class
    );
    /// defined_flag
    map.put(
        getRuleIElementType(VerilogParser.RULE_defined_flag),
        MacroIdentifierPsiNode.class
    );
    /// using_defined_flag
    map.put(
      getRuleIElementType(VerilogParser.RULE_using_defined_flag),
      MacroUsePsiNode.class
    );

    // incomplete statements
    /// incomplete condition statements
    map.put(
      getRuleIElementType(VerilogParser.RULE_incomplete_condition_statement),
      IncompleteConditionPsiNode.class
    );

    // binary operator
    /// or
    map.put(
      getRuleIElementType(VerilogParser.RULE_binary_operator_or),
      BinaryOperatorOrPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_seq_block),
        SeqBlockPsiNode.class
    );

    // case statement
    /// case body
    map.put(
        getRuleIElementType(VerilogParser.RULE_case_body),
        CaseBodyPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_generate_case_body),
        CaseBodyPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_function_case_body),
        CaseBodyPsiNode.class
    );
    /// case statement
    map.put(
        getRuleIElementType(VerilogParser.RULE_case_statement),
        CaseStatementPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_generate_case_statement),
        CaseStatementPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_function_case_statement),
        CaseStatementPsiNode.class
    );
    /// case body default
    map.put(
        getRuleIElementType(VerilogParser.RULE_case_default_item),
        CaseDefaultPsiNode.class
    );



    // Expression
    map.put(
        getRuleIElementType(VerilogParser.RULE_constant_expression),
        ConstantExpressionPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_expression),
        ExpressionPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_term),
        TermPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_primary),
        PrimaryPsiNode.class
    );
    map.put(
        getRuleIElementType(VerilogParser.RULE_number),
        NumberPsiNode.class
    );
    /// concat
    map.put(
        getRuleIElementType(VerilogParser.RULE_concatenation),
        ConcatPsiNode.class
    );



    map.put(
        getRuleIElementType(VerilogParser.RULE_hierarchical_identifier),
        HierarchicalIdentifierPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_module_instantiation),
        ModuleInstantiationPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_module_instance),
        ModuleInstancePsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_name_of_instance),
        NameOfInstancePsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_statement),
        StatementPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_list_of_port_connections),
        ListOfPortConnectionsPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_named_port_connection),
        NamedPortConnectionPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_ordered_port_connection),
        OrderedPortConnectionPsiNode.class
    );

    map.put(
        getRuleIElementType(VerilogParser.RULE_directive),
        DirectivePsiNode.class
    );

  }

  private static RuleIElementType getRuleIElementType(int ruleIndex) {
    return PSIElementTypeFactory.getRuleIElementTypes(LANGUAGE)
        .get(ruleIndex);
  }

  public static ANTLRPsiNode create(ASTNode astNode) {
    if (map.containsKey(astNode.getElementType())) {
      try {
        return map.get(astNode.getElementType())
            .getConstructor(ASTNode.class).newInstance(astNode);
      } catch (ReflectiveOperationException ignored) {
      }
    }
    return new ANTLRPsiNode(astNode);
  }
}
