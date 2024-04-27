package top.scaleda.bluespec.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import top.scaleda.bluespec.psi.nodes.IdentifierPsiNode;
import top.scaleda.bluespec.psi.nodes.SourceTextPsiNode;
// import top.scaleda.bluespec.psi.nodes.always.AlwaysConstructPsiNode;
// import top.scaleda.bluespec.psi.nodes.always.DelayOrEventControlPsiNode;
// import top.scaleda.bluespec.psi.nodes.always.DelayValuePsiNode;
// import top.scaleda.bluespec.psi.nodes.always.EventExpressionPsiNode;
// import top.scaleda.bluespec.psi.nodes.assignments.BlockingAssignmentPsiNode;
// import top.scaleda.bluespec.psi.nodes.assignments.NetAssignmentPsiNode;
// import top.scaleda.bluespec.psi.nodes.assignments.NonblockingAssignmentPsiNode;
// import top.scaleda.bluespec.psi.nodes.assignments.ProceduralContinuousAssignmentPsiNode;
// import top.scaleda.bluespec.psi.nodes.block.CaseBodyPsiNode;
// import top.scaleda.bluespec.psi.nodes.block.CaseStatementPsiNode;
// import top.scaleda.bluespec.psi.nodes.block.SeqBlockPsiNode;
// import top.scaleda.bluespec.psi.nodes.clazz.*;
// import top.scaleda.bluespec.psi.nodes.condition.ConditionalStatementChainPsiNode;
// import top.scaleda.bluespec.psi.nodes.condition.ConditionalStatementHeadPsiNode;
// import top.scaleda.bluespec.psi.nodes.condition.ConditionalStatementPsiNode;
// import top.scaleda.bluespec.psi.nodes.condition.ConditionalStatementTailPsiNode;
// import top.scaleda.bluespec.psi.nodes.expression.*;
// import top.scaleda.bluespec.psi.nodes.function.FunctionDeclarationPsiNode;
// import top.scaleda.bluespec.psi.nodes.initial.InitialPsiNode;
// import top.scaleda.bluespec.psi.nodes.instantiation.*;
import top.scaleda.bluespec.psi.nodes.module.*;
// import top.scaleda.bluespec.psi.nodes.parameter.*;
// import top.scaleda.bluespec.psi.nodes.signal.*;
// import top.scaleda.bluespec.psi.nodes.task.TaskDeclarationPsiNode;
import top.scaleda.bluespec.BluespecLanguage$;
import top.scaleda.bluespec.parser.BluespecParser;

import java.util.HashMap;
import java.util.Map;

public class BluespecPsiNodeFactory {

  private static final Language LANGUAGE = BluespecLanguage$.MODULE$;

  private static final Map<IElementType, Class<? extends ANTLRPsiNode>> map = new HashMap<>();

  static {
    // Source file (start symbol)
    map.put(getRuleIElementType(BluespecParser.RULE_top), SourceTextPsiNode.class);

    // Module
    /// Module keywords
    // map.put(getRuleIElementType(BluespecParser.RULE_module_keyword), ModuleKeywordPsiNode.class);
    /// Module declaration
    map.put(getRuleIElementType(BluespecParser.RULE_moduleProto), ModuleDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_module_header), ModuleHeadPsiNode.class);
    /// Module identifier
    // map.put(getRuleIElementType(BluespecParser.RULE_module_identifier), ModuleIdentifierPsiNode.class);
    /// Module parameter
    // map.put(getRuleIElementType(BluespecParser.RULE_parameter_port_list), ModuleParameterPortListPsiNode.class);
    /// Port declarations list
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_port_declarations), ListOfPortDeclarationsPsiNode.class);
    /// Port declaration
    // map.put(getRuleIElementType(BluespecParser.RULE_port_declaration), PortDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_output_declaration), OutputDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_input_declaration), InputDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_inout_declaration), InoutDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_port_identifiers), ListOfPortIdentifiersPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_variable_port_identifiers), ListOfVariablePortIdentifiersPsiNode.class);

    // map.put(getRuleIElementType(BluespecParser.RULE_port), PortPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_port_expression), PortExpressionPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_port_reference), PortReferencePsiNode.class);
    /// Port identifier
    // map.put(getRuleIElementType(BluespecParser.RULE_port_identifier), PortIdentifierPsiNode.class);
    /// Parameter declaration
    // map.put(getRuleIElementType(BluespecParser.RULE_parameter_declaration), ParameterDeclarationPsiNode.class);
    /// module functions
    // map.put(getRuleIElementType(BluespecParser.RULE_function_declaration), FunctionDeclarationPsiNode.class);

    // map.put(getRuleIElementType(BluespecParser.RULE_parameter_identifier), ParameterIdentifierPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_param_assignment), ParamAssignmentPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_param_assignments), ListOfParamAssignmentsPsiNode.class);

    /// Inside module, module or generate item
    // map.put(getRuleIElementType(BluespecParser.RULE_module_item), ModuleItemPsiNode.class);

    // Class
    // map.put(getRuleIElementType(BluespecParser.RULE_class_identifier), ClassIdentifierPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_declaration), ClassDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_item), ClassItemPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_property), ClassPropertyPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_constraint), ClassConstraintPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_constructor_declaration), ClassConstructorPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_method), ClassMethodPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_parameter_port_declaration), ParameterPortDeclarationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_extension), ClassExtensionPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_class_implementation), ClassImplementationPsiNode.class);

    // Task
    // map.put(getRuleIElementType(BluespecParser.RULE_task_declaration), TaskDeclarationPsiNode.class);

    // // Always construct
    // /// Always block
    // map.put(getRuleIElementType(BluespecParser.RULE_always_construct), AlwaysConstructPsiNode.class);
    // /// Delay or event control
    // map.put(getRuleIElementType(BluespecParser.RULE_delay_or_event_control), DelayOrEventControlPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_delay_value), DelayValuePsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_event_expression), EventExpressionPsiNode.class);
    // /// Assignment
    // map.put(getRuleIElementType(BluespecParser.RULE_nonblocking_assignment), NonblockingAssignmentPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_blocking_assignment), BlockingAssignmentPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_procedural_continuous_assignment), ProceduralContinuousAssignmentPsiNode.class);
    // /// Assign
    // map.put(getRuleIElementType(BluespecParser.RULE_net_assignment), NetAssignmentPsiNode.class);
    // /// Initial
    // map.put(getRuleIElementType(BluespecParser.RULE_initial_construct), InitialPsiNode.class);
    //
    // // Signals
    // /// Net (wire, tri, etc.)
    // map.put(getRuleIElementType(BluespecParser.RULE_net_declaration), NetDeclarationPsiNode.class);
    //
    // // conditional statements
    // /// if-head
    // map.put(getRuleIElementType(BluespecParser.RULE_conditional_statement_head), ConditionalStatementHeadPsiNode.class);
    // /// else-if-chain
    // map.put(getRuleIElementType(BluespecParser.RULE_conditional_statement_else_chain), ConditionalStatementChainPsiNode.class);
    // /// if-tail
    // map.put(getRuleIElementType(BluespecParser.RULE_conditional_statement_else_tail), ConditionalStatementTailPsiNode.class);
    // /// all if block
    // map.put(getRuleIElementType(BluespecParser.RULE_conditional_statement), ConditionalStatementPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_net_identifier), NetIdentifierPsiNode.class);
    // /// Variables (reg, integer, etc.)
    // /// Identifier
    map.put(getRuleIElementType(BluespecParser.RULE_identifier), IdentifierPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_data_type), VariableDeclarationPsiNode.class);
    // // map.put(getRuleIElementType(BluespecParser.RULE_variable_identifier), VariableIdentifierPsiNode.class);
    // // map.put(getRuleIElementType(BluespecParser.RULE_variable_type), VariableTypePsiNode.class);
    // /// Range
    // map.put(getRuleIElementType(BluespecParser.RULE_value_range), RangeValuePsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_covergroup_value_range), CovergroupValuePsiNode.class);


    // Macros
    /// define_directive
    // map.put(
    //     getRuleIElementType(BluespecParser.RULE_define_directive),
    //     MacroDeclarationPsiNode.class
    // );
    // /// defined_flag
    // map.put(
    //     getRuleIElementType(BluespecParser.RULE_defined_flag),
    //     MacroIdentifierPsiNode.class
    // );
    // /// using_defined_flag
    // map.put(
    //   getRuleIElementType(BluespecParser.RULE_using_defined_flag),
    //   MacroUsePsiNode.class
    // );

    // incomplete statements
    /// incomplete condition statements
    // map.put(getRuleIElementType(BluespecParser.RULE_incomplete_condition_statement), IncompleteConditionPsiNode.class);

    // map.put(getRuleIElementType(BluespecParser.RULE_seq_block), SeqBlockPsiNode.class);

    // // case statement
    // /// case body
    // map.put(getRuleIElementType(BluespecParser.RULE_case_body_1), CaseBodyPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_case_body_2), CaseBodyPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_case_body_3), CaseBodyPsiNode.class);
    //
    //
    // /// case statement
    // map.put(getRuleIElementType(BluespecParser.RULE_case_statement), CaseStatementPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_randcase_statement), CaseStatementPsiNode.class);

    // /// case body default
    // map.put(
    //     getRuleIElementType(BluespecParser.RULE_case_default_item),
    //     CaseDefaultPsiNode.class
    // );


    // Expression
    // map.put(getRuleIElementType(BluespecParser.RULE_constant_expression), ConstantExpressionPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_expression), ExpressionPsiNode.class);
    // map.put(
    //     getRuleIElementType(BluespecParser.RULE_term),
    //     TermPsiNode.class
    // );
    // map.put(getRuleIElementType(BluespecParser.RULE_primary), PrimaryPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_number), NumberPsiNode.class);
    /// concat
    // map.put(getRuleIElementType(BluespecParser.RULE_concatenation), ConcatPsiNode.class);


    // map.put(getRuleIElementType(BluespecParser.RULE_hierarchical_identifier), HierarchicalIdentifierPsiNode.class);

    /// Module Instantiation
    // map.put(getRuleIElementType(BluespecParser.RULE_module_program_interface_instantiation), ModuleInstantiationPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_parameter_value_assignment), ParameterValueAssignmentPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_parameter_assignments), ListOfParameterAssignmentsPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_ordered_parameter_assignment), OrderedParameterAssignmentPsiNode.class);
    // map.put(getRuleIElementType(BluespecParser.RULE_named_parameter_assignment), NamedParameterAssignmentPsiNode.class);


    // map.put(getRuleIElementType(BluespecParser.RULE_hierarchical_instance), HierarchicalInstancePsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_name_of_instance), NameOfInstancePsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_statement_item), StatementPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_list_of_port_connections), ListOfPortConnectionsPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_named_port_connection), NamedPortConnectionPsiNode.class);
    //
    // map.put(getRuleIElementType(BluespecParser.RULE_ordered_port_connection), OrderedPortConnectionPsiNode.class);

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
