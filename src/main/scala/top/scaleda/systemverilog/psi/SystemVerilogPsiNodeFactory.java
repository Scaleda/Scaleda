package top.scaleda.systemverilog.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import top.scaleda.systemverilog.SystemVerilogLanguage$;
import top.scaleda.systemverilog.parser.SystemVerilogParser;
import top.scaleda.systemverilog.psi.nodes.IdentifierPsiNode;
import top.scaleda.systemverilog.psi.nodes.SourceTextPsiNode;
import top.scaleda.systemverilog.psi.nodes.StatementPsiNode;
import top.scaleda.systemverilog.psi.nodes.always.DelayOrEventControlPsiNode;
import top.scaleda.systemverilog.psi.nodes.always.DelayValuePsiNode;
import top.scaleda.systemverilog.psi.nodes.always.EventExpressionPsiNode;
import top.scaleda.systemverilog.psi.nodes.assignments.BlockingAssignmentPsiNode;
import top.scaleda.systemverilog.psi.nodes.assignments.NetAssignmentPsiNode;
import top.scaleda.systemverilog.psi.nodes.assignments.NonblockingAssignmentPsiNode;
import top.scaleda.systemverilog.psi.nodes.block.CaseBodyPsiNode;
import top.scaleda.systemverilog.psi.nodes.block.CaseStatementPsiNode;
import top.scaleda.systemverilog.psi.nodes.block.SeqBlockPsiNode;
import top.scaleda.systemverilog.psi.nodes.clazz.*;
import top.scaleda.systemverilog.psi.nodes.condition.ConditionalStatementChainPsiNode;
import top.scaleda.systemverilog.psi.nodes.condition.ConditionalStatementHeadPsiNode;
import top.scaleda.systemverilog.psi.nodes.condition.ConditionalStatementPsiNode;
import top.scaleda.systemverilog.psi.nodes.condition.ConditionalStatementTailPsiNode;
import top.scaleda.systemverilog.psi.nodes.expression.*;
import top.scaleda.systemverilog.psi.nodes.incompleted.IncompleteConditionPsiNode;
import top.scaleda.systemverilog.psi.nodes.instantiation.*;
import top.scaleda.systemverilog.psi.nodes.module.*;
import top.scaleda.systemverilog.psi.nodes.parameter.*;
import top.scaleda.systemverilog.psi.nodes.signal.*;
import top.scaleda.systemverilog.psi.nodes.task.TaskDeclarationPsiNode;

import java.util.HashMap;
import java.util.Map;

public class SystemVerilogPsiNodeFactory {

  private static final Language LANGUAGE = SystemVerilogLanguage$.MODULE$;

  private static final Map<IElementType, Class<? extends ANTLRPsiNode>> map = new HashMap<>();

  static {
    // Source file (start symbol)
    map.put(getRuleIElementType(SystemVerilogParser.RULE_source_text), SourceTextPsiNode.class);

    // Module
    /// Module keywords
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_keyword), ModuleKeywordPsiNode.class);
    /// Module declaration
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_declaration), ModuleDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_header), ModuleHeadPsiNode.class);

    /// Module identifier
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_identifier), ModuleIdentifierPsiNode.class);
    /// Module parameter
    map.put(getRuleIElementType(SystemVerilogParser.RULE_parameter_port_list), ModuleParameterPortListPsiNode.class);
    /// Port declarations list
    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_port_declarations), ListOfPortDeclarationsPsiNode.class);
    /// Port declaration
    map.put(getRuleIElementType(SystemVerilogParser.RULE_port_declaration), PortDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_output_declaration), OutputDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_input_declaration), InputDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_inout_declaration), InoutDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_port_identifiers), ListOfPortIdentifiersPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_variable_port_identifiers), ListOfVariablePortIdentifiersPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_port), PortPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_port_expression), PortExpressionPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_port_reference), PortReferencePsiNode.class);
    /// Port identifier
    map.put(getRuleIElementType(SystemVerilogParser.RULE_port_identifier), PortIdentifierPsiNode.class);
    /// Parameter declaration
    map.put(getRuleIElementType(SystemVerilogParser.RULE_parameter_declaration), ParameterDeclarationPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_parameter_identifier), ParameterIdentifierPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_param_assignment), ParamAssignmentPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_param_assignments), ListOfParamAssignmentsPsiNode.class);

    /// Inside module, module or generate item
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_item), ModuleItemPsiNode.class);

    // Class
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_identifier), ClassIdentifierPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_declaration), ClassDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_item), ClassItemPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_property), ClassPropertyPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_constraint), ClassConstraintPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_constructor_declaration), ClassConstructorPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_method), ClassMethodPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_parameter_port_declaration), ParameterPortDeclarationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_extension), ClassExtensionPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_class_implementation), ClassImplementationPsiNode.class);

    // Task
    map.put(getRuleIElementType(SystemVerilogParser.RULE_task_declaration), TaskDeclarationPsiNode.class);

    // Always construct
    /// Always block
    // map.put(
    //     getRuleIElementType(SystemVerilogParser.RULE_always_construct),
    //     AlwaysConstructPsiNode.class
    // );
    /// Delay or event control
    map.put(getRuleIElementType(SystemVerilogParser.RULE_delay_or_event_control), DelayOrEventControlPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_delay_value), DelayValuePsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_event_expression), EventExpressionPsiNode.class);
    /// Assignment
    map.put(getRuleIElementType(SystemVerilogParser.RULE_nonblocking_assignment), NonblockingAssignmentPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_blocking_assignment), BlockingAssignmentPsiNode.class);
    /// Assign
    map.put(getRuleIElementType(SystemVerilogParser.RULE_net_assignment), NetAssignmentPsiNode.class);

    // Signals
    /// Net (wire, tri, etc.)
    map.put(getRuleIElementType(SystemVerilogParser.RULE_net_declaration), NetDeclarationPsiNode.class);

    // conditional statements
    /// if-head
    map.put(getRuleIElementType(SystemVerilogParser.RULE_conditional_statement_head), ConditionalStatementHeadPsiNode.class);
    /// else-if-chain
    map.put(getRuleIElementType(SystemVerilogParser.RULE_conditional_statement_else_chain), ConditionalStatementChainPsiNode.class);
    /// if-tail
    map.put(getRuleIElementType(SystemVerilogParser.RULE_conditional_statement_else_tail), ConditionalStatementTailPsiNode.class);
    /// all if block
    map.put(getRuleIElementType(SystemVerilogParser.RULE_conditional_statement), ConditionalStatementPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_net_identifier), NetIdentifierPsiNode.class);
    /// Variables (reg, integer, etc.)
    /// Identifier
    map.put(getRuleIElementType(SystemVerilogParser.RULE_identifier), IdentifierPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_data_type), VariableDeclarationPsiNode.class);
    // map.put(getRuleIElementType(SystemVerilogParser.RULE_variable_identifier), VariableIdentifierPsiNode.class);
    // map.put(getRuleIElementType(SystemVerilogParser.RULE_variable_type), VariableTypePsiNode.class);
    /// Range
    map.put(getRuleIElementType(SystemVerilogParser.RULE_value_range), RangeValuePsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_covergroup_value_range), CovergroupValuePsiNode.class);


    // Macros
    /// define_directive
    // map.put(
    //     getRuleIElementType(SystemVerilogParser.RULE_define_directive),
    //     MacroDeclarationPsiNode.class
    // );
    // /// defined_flag
    // map.put(
    //     getRuleIElementType(SystemVerilogParser.RULE_defined_flag),
    //     MacroIdentifierPsiNode.class
    // );
    // /// using_defined_flag
    // map.put(
    //   getRuleIElementType(SystemVerilogParser.RULE_using_defined_flag),
    //   MacroUsePsiNode.class
    // );

    // incomplete statements
    /// incomplete condition statements
    map.put(getRuleIElementType(SystemVerilogParser.RULE_incomplete_condition_statement), IncompleteConditionPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_seq_block), SeqBlockPsiNode.class);

    // case statement
    /// case body
    map.put(getRuleIElementType(SystemVerilogParser.RULE_case_body_1), CaseBodyPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_case_body_2), CaseBodyPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_case_body_3), CaseBodyPsiNode.class);


    /// case statement
    map.put(getRuleIElementType(SystemVerilogParser.RULE_case_statement), CaseStatementPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_randcase_statement), CaseStatementPsiNode.class);

    // /// case body default
    // map.put(
    //     getRuleIElementType(SystemVerilogParser.RULE_case_default_item),
    //     CaseDefaultPsiNode.class
    // );


    // Expression
    map.put(getRuleIElementType(SystemVerilogParser.RULE_constant_expression), ConstantExpressionPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_expression), ExpressionPsiNode.class);
    // map.put(
    //     getRuleIElementType(SystemVerilogParser.RULE_term),
    //     TermPsiNode.class
    // );
    map.put(getRuleIElementType(SystemVerilogParser.RULE_primary), PrimaryPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_number), NumberPsiNode.class);
    /// concat
    map.put(getRuleIElementType(SystemVerilogParser.RULE_concatenation), ConcatPsiNode.class);


    map.put(getRuleIElementType(SystemVerilogParser.RULE_hierarchical_identifier), HierarchicalIdentifierPsiNode.class);

    /// Module Instantiation
    map.put(getRuleIElementType(SystemVerilogParser.RULE_module_program_interface_instantiation), ModuleInstantiationPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_parameter_value_assignment), ParameterValueAssignmentPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_parameter_assignments), ListOfParameterAssignmentsPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_ordered_parameter_assignment), OrderedParameterAssignmentPsiNode.class);
    map.put(getRuleIElementType(SystemVerilogParser.RULE_named_parameter_assignment), NamedParameterAssignmentPsiNode.class);


    map.put(getRuleIElementType(SystemVerilogParser.RULE_hierarchical_instance), HierarchicalInstancePsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_name_of_instance), NameOfInstancePsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_statement_item), StatementPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_list_of_port_connections), ListOfPortConnectionsPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_named_port_connection), NamedPortConnectionPsiNode.class);

    map.put(getRuleIElementType(SystemVerilogParser.RULE_ordered_port_connection), OrderedPortConnectionPsiNode.class);

  }

  private static RuleIElementType getRuleIElementType(int ruleIndex) {
    return PSIElementTypeFactory.getRuleIElementTypes(LANGUAGE).get(ruleIndex);
  }

  public static ANTLRPsiNode create(ASTNode astNode) {
    if (map.containsKey(astNode.getElementType())) {
      try {
        return map.get(astNode.getElementType()).getConstructor(ASTNode.class).newInstance(astNode);
      } catch (ReflectiveOperationException ignored) {
      }
    }
    return new ANTLRPsiNode(astNode);
  }
}
