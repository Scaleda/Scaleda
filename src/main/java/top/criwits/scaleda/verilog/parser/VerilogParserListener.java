// Generated from java-escape by ANTLR 4.11.1
package top.criwits.scaleda.verilog.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link VerilogParser}.
 */
public interface VerilogParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link VerilogParser#source_text}.
	 * @param ctx the parse tree
	 */
	void enterSource_text(VerilogParser.Source_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#source_text}.
	 * @param ctx the parse tree
	 */
	void exitSource_text(VerilogParser.Source_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(VerilogParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(VerilogParser.DirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#timescale_directive}.
	 * @param ctx the parse tree
	 */
	void enterTimescale_directive(VerilogParser.Timescale_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#timescale_directive}.
	 * @param ctx the parse tree
	 */
	void exitTimescale_directive(VerilogParser.Timescale_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#defined_flag}.
	 * @param ctx the parse tree
	 */
	void enterDefined_flag(VerilogParser.Defined_flagContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#defined_flag}.
	 * @param ctx the parse tree
	 */
	void exitDefined_flag(VerilogParser.Defined_flagContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#create_defined_flag}.
	 * @param ctx the parse tree
	 */
	void enterCreate_defined_flag(VerilogParser.Create_defined_flagContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#create_defined_flag}.
	 * @param ctx the parse tree
	 */
	void exitCreate_defined_flag(VerilogParser.Create_defined_flagContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#create_defined_term}.
	 * @param ctx the parse tree
	 */
	void enterCreate_defined_term(VerilogParser.Create_defined_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#create_defined_term}.
	 * @param ctx the parse tree
	 */
	void exitCreate_defined_term(VerilogParser.Create_defined_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#using_defined_flag}.
	 * @param ctx the parse tree
	 */
	void enterUsing_defined_flag(VerilogParser.Using_defined_flagContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#using_defined_flag}.
	 * @param ctx the parse tree
	 */
	void exitUsing_defined_flag(VerilogParser.Using_defined_flagContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#define_directive}.
	 * @param ctx the parse tree
	 */
	void enterDefine_directive(VerilogParser.Define_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#define_directive}.
	 * @param ctx the parse tree
	 */
	void exitDefine_directive(VerilogParser.Define_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#ifdef_directive}.
	 * @param ctx the parse tree
	 */
	void enterIfdef_directive(VerilogParser.Ifdef_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#ifdef_directive}.
	 * @param ctx the parse tree
	 */
	void exitIfdef_directive(VerilogParser.Ifdef_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#ifndef_directive}.
	 * @param ctx the parse tree
	 */
	void enterIfndef_directive(VerilogParser.Ifndef_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#ifndef_directive}.
	 * @param ctx the parse tree
	 */
	void exitIfndef_directive(VerilogParser.Ifndef_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#else_directive}.
	 * @param ctx the parse tree
	 */
	void enterElse_directive(VerilogParser.Else_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#else_directive}.
	 * @param ctx the parse tree
	 */
	void exitElse_directive(VerilogParser.Else_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#elsif_directive}.
	 * @param ctx the parse tree
	 */
	void enterElsif_directive(VerilogParser.Elsif_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#elsif_directive}.
	 * @param ctx the parse tree
	 */
	void exitElsif_directive(VerilogParser.Elsif_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#endif_directive}.
	 * @param ctx the parse tree
	 */
	void enterEndif_directive(VerilogParser.Endif_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#endif_directive}.
	 * @param ctx the parse tree
	 */
	void exitEndif_directive(VerilogParser.Endif_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#undef_directive}.
	 * @param ctx the parse tree
	 */
	void enterUndef_directive(VerilogParser.Undef_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#undef_directive}.
	 * @param ctx the parse tree
	 */
	void exitUndef_directive(VerilogParser.Undef_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#include_directive}.
	 * @param ctx the parse tree
	 */
	void enterInclude_directive(VerilogParser.Include_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#include_directive}.
	 * @param ctx the parse tree
	 */
	void exitInclude_directive(VerilogParser.Include_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#default_nettype_directive}.
	 * @param ctx the parse tree
	 */
	void enterDefault_nettype_directive(VerilogParser.Default_nettype_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#default_nettype_directive}.
	 * @param ctx the parse tree
	 */
	void exitDefault_nettype_directive(VerilogParser.Default_nettype_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#config_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConfig_declaration(VerilogParser.Config_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#config_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConfig_declaration(VerilogParser.Config_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#design_statement}.
	 * @param ctx the parse tree
	 */
	void enterDesign_statement(VerilogParser.Design_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#design_statement}.
	 * @param ctx the parse tree
	 */
	void exitDesign_statement(VerilogParser.Design_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#config_rule_statement}.
	 * @param ctx the parse tree
	 */
	void enterConfig_rule_statement(VerilogParser.Config_rule_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#config_rule_statement}.
	 * @param ctx the parse tree
	 */
	void exitConfig_rule_statement(VerilogParser.Config_rule_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#default_clause}.
	 * @param ctx the parse tree
	 */
	void enterDefault_clause(VerilogParser.Default_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#default_clause}.
	 * @param ctx the parse tree
	 */
	void exitDefault_clause(VerilogParser.Default_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#inst_clause}.
	 * @param ctx the parse tree
	 */
	void enterInst_clause(VerilogParser.Inst_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#inst_clause}.
	 * @param ctx the parse tree
	 */
	void exitInst_clause(VerilogParser.Inst_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#inst_name}.
	 * @param ctx the parse tree
	 */
	void enterInst_name(VerilogParser.Inst_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#inst_name}.
	 * @param ctx the parse tree
	 */
	void exitInst_name(VerilogParser.Inst_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#liblist_clause}.
	 * @param ctx the parse tree
	 */
	void enterLiblist_clause(VerilogParser.Liblist_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#liblist_clause}.
	 * @param ctx the parse tree
	 */
	void exitLiblist_clause(VerilogParser.Liblist_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#cell_clause}.
	 * @param ctx the parse tree
	 */
	void enterCell_clause(VerilogParser.Cell_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#cell_clause}.
	 * @param ctx the parse tree
	 */
	void exitCell_clause(VerilogParser.Cell_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#use_clause}.
	 * @param ctx the parse tree
	 */
	void enterUse_clause(VerilogParser.Use_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#use_clause}.
	 * @param ctx the parse tree
	 */
	void exitUse_clause(VerilogParser.Use_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(VerilogParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(VerilogParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_declaration}.
	 * @param ctx the parse tree
	 */
	void enterModule_declaration(VerilogParser.Module_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_declaration}.
	 * @param ctx the parse tree
	 */
	void exitModule_declaration(VerilogParser.Module_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_head}.
	 * @param ctx the parse tree
	 */
	void enterModule_head(VerilogParser.Module_headContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_head}.
	 * @param ctx the parse tree
	 */
	void exitModule_head(VerilogParser.Module_headContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_keyword}.
	 * @param ctx the parse tree
	 */
	void enterModule_keyword(VerilogParser.Module_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_keyword}.
	 * @param ctx the parse tree
	 */
	void exitModule_keyword(VerilogParser.Module_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
	 * @param ctx the parse tree
	 */
	void enterModule_parameter_port_list(VerilogParser.Module_parameter_port_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
	 * @param ctx the parse tree
	 */
	void exitModule_parameter_port_list(VerilogParser.Module_parameter_port_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_ports}.
	 * @param ctx the parse tree
	 */
	void enterList_of_ports(VerilogParser.List_of_portsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_ports}.
	 * @param ctx the parse tree
	 */
	void exitList_of_ports(VerilogParser.List_of_portsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
	 * @param ctx the parse tree
	 */
	void enterList_of_port_declarations(VerilogParser.List_of_port_declarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
	 * @param ctx the parse tree
	 */
	void exitList_of_port_declarations(VerilogParser.List_of_port_declarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#port}.
	 * @param ctx the parse tree
	 */
	void enterPort(VerilogParser.PortContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#port}.
	 * @param ctx the parse tree
	 */
	void exitPort(VerilogParser.PortContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#port_expression}.
	 * @param ctx the parse tree
	 */
	void enterPort_expression(VerilogParser.Port_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#port_expression}.
	 * @param ctx the parse tree
	 */
	void exitPort_expression(VerilogParser.Port_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#port_reference}.
	 * @param ctx the parse tree
	 */
	void enterPort_reference(VerilogParser.Port_referenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#port_reference}.
	 * @param ctx the parse tree
	 */
	void exitPort_reference(VerilogParser.Port_referenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#port_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPort_declaration(VerilogParser.Port_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#port_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPort_declaration(VerilogParser.Port_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_item}.
	 * @param ctx the parse tree
	 */
	void enterModule_item(VerilogParser.Module_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_item}.
	 * @param ctx the parse tree
	 */
	void exitModule_item(VerilogParser.Module_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item}.
	 * @param ctx the parse tree
	 */
	void enterModule_or_generate_item(VerilogParser.Module_or_generate_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item}.
	 * @param ctx the parse tree
	 */
	void exitModule_or_generate_item(VerilogParser.Module_or_generate_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
	 * @param ctx the parse tree
	 */
	void enterModule_or_generate_item_declaration(VerilogParser.Module_or_generate_item_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
	 * @param ctx the parse tree
	 */
	void exitModule_or_generate_item_declaration(VerilogParser.Module_or_generate_item_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parameter_override}.
	 * @param ctx the parse tree
	 */
	void enterParameter_override(VerilogParser.Parameter_overrideContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parameter_override}.
	 * @param ctx the parse tree
	 */
	void exitParameter_override(VerilogParser.Parameter_overrideContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterLocal_parameter_declaration(VerilogParser.Local_parameter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitLocal_parameter_declaration(VerilogParser.Local_parameter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declaration(VerilogParser.Parameter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parameter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declaration(VerilogParser.Parameter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parameter_declaration_}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declaration_(VerilogParser.Parameter_declaration_Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parameter_declaration_}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declaration_(VerilogParser.Parameter_declaration_Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specparam_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSpecparam_declaration(VerilogParser.Specparam_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specparam_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSpecparam_declaration(VerilogParser.Specparam_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#inout_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInout_declaration(VerilogParser.Inout_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#inout_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInout_declaration(VerilogParser.Inout_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#input_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInput_declaration(VerilogParser.Input_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#input_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInput_declaration(VerilogParser.Input_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#output_declaration}.
	 * @param ctx the parse tree
	 */
	void enterOutput_declaration(VerilogParser.Output_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#output_declaration}.
	 * @param ctx the parse tree
	 */
	void exitOutput_declaration(VerilogParser.Output_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEvent_declaration(VerilogParser.Event_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEvent_declaration(VerilogParser.Event_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_declaration}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_declaration(VerilogParser.Genvar_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_declaration}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_declaration(VerilogParser.Genvar_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#integer_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInteger_declaration(VerilogParser.Integer_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#integer_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInteger_declaration(VerilogParser.Integer_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#time_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTime_declaration(VerilogParser.Time_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#time_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTime_declaration(VerilogParser.Time_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#real_declaration}.
	 * @param ctx the parse tree
	 */
	void enterReal_declaration(VerilogParser.Real_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#real_declaration}.
	 * @param ctx the parse tree
	 */
	void exitReal_declaration(VerilogParser.Real_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#realtime_declaration}.
	 * @param ctx the parse tree
	 */
	void enterRealtime_declaration(VerilogParser.Realtime_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#realtime_declaration}.
	 * @param ctx the parse tree
	 */
	void exitRealtime_declaration(VerilogParser.Realtime_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#reg_declaration}.
	 * @param ctx the parse tree
	 */
	void enterReg_declaration(VerilogParser.Reg_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#reg_declaration}.
	 * @param ctx the parse tree
	 */
	void exitReg_declaration(VerilogParser.Reg_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNet_declaration(VerilogParser.Net_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNet_declaration(VerilogParser.Net_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_type}.
	 * @param ctx the parse tree
	 */
	void enterNet_type(VerilogParser.Net_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_type}.
	 * @param ctx the parse tree
	 */
	void exitNet_type(VerilogParser.Net_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#output_variable_type}.
	 * @param ctx the parse tree
	 */
	void enterOutput_variable_type(VerilogParser.Output_variable_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#output_variable_type}.
	 * @param ctx the parse tree
	 */
	void exitOutput_variable_type(VerilogParser.Output_variable_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#real_type}.
	 * @param ctx the parse tree
	 */
	void enterReal_type(VerilogParser.Real_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#real_type}.
	 * @param ctx the parse tree
	 */
	void exitReal_type(VerilogParser.Real_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_type}.
	 * @param ctx the parse tree
	 */
	void enterVariable_type(VerilogParser.Variable_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_type}.
	 * @param ctx the parse tree
	 */
	void exitVariable_type(VerilogParser.Variable_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#drive_strength}.
	 * @param ctx the parse tree
	 */
	void enterDrive_strength(VerilogParser.Drive_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#drive_strength}.
	 * @param ctx the parse tree
	 */
	void exitDrive_strength(VerilogParser.Drive_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#strength0}.
	 * @param ctx the parse tree
	 */
	void enterStrength0(VerilogParser.Strength0Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#strength0}.
	 * @param ctx the parse tree
	 */
	void exitStrength0(VerilogParser.Strength0Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#strength1}.
	 * @param ctx the parse tree
	 */
	void enterStrength1(VerilogParser.Strength1Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#strength1}.
	 * @param ctx the parse tree
	 */
	void exitStrength1(VerilogParser.Strength1Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#charge_strength}.
	 * @param ctx the parse tree
	 */
	void enterCharge_strength(VerilogParser.Charge_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#charge_strength}.
	 * @param ctx the parse tree
	 */
	void exitCharge_strength(VerilogParser.Charge_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delay3}.
	 * @param ctx the parse tree
	 */
	void enterDelay3(VerilogParser.Delay3Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delay3}.
	 * @param ctx the parse tree
	 */
	void exitDelay3(VerilogParser.Delay3Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delay2}.
	 * @param ctx the parse tree
	 */
	void enterDelay2(VerilogParser.Delay2Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delay2}.
	 * @param ctx the parse tree
	 */
	void exitDelay2(VerilogParser.Delay2Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delay_value}.
	 * @param ctx the parse tree
	 */
	void enterDelay_value(VerilogParser.Delay_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delay_value}.
	 * @param ctx the parse tree
	 */
	void exitDelay_value(VerilogParser.Delay_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_event_identifiers(VerilogParser.List_of_event_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_event_identifiers(VerilogParser.List_of_event_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_net_identifiers(VerilogParser.List_of_net_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_net_identifiers(VerilogParser.List_of_net_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_genvar_identifiers(VerilogParser.List_of_genvar_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_genvar_identifiers(VerilogParser.List_of_genvar_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_port_identifiers(VerilogParser.List_of_port_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_port_identifiers(VerilogParser.List_of_port_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_net_decl_assignments(VerilogParser.List_of_net_decl_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_net_decl_assignments(VerilogParser.List_of_net_decl_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_param_assignments(VerilogParser.List_of_param_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_param_assignments(VerilogParser.List_of_param_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_specparam_assignments(VerilogParser.List_of_specparam_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_specparam_assignments(VerilogParser.List_of_specparam_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_real_identifiers(VerilogParser.List_of_real_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_real_identifiers(VerilogParser.List_of_real_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_variable_identifiers(VerilogParser.List_of_variable_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_variable_identifiers(VerilogParser.List_of_variable_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_variable_port_identifiers(VerilogParser.List_of_variable_port_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_variable_port_identifiers(VerilogParser.List_of_variable_port_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_decl_assignment}.
	 * @param ctx the parse tree
	 */
	void enterNet_decl_assignment(VerilogParser.Net_decl_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_decl_assignment}.
	 * @param ctx the parse tree
	 */
	void exitNet_decl_assignment(VerilogParser.Net_decl_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#param_assignment}.
	 * @param ctx the parse tree
	 */
	void enterParam_assignment(VerilogParser.Param_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#param_assignment}.
	 * @param ctx the parse tree
	 */
	void exitParam_assignment(VerilogParser.Param_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specparam_assignment}.
	 * @param ctx the parse tree
	 */
	void enterSpecparam_assignment(VerilogParser.Specparam_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specparam_assignment}.
	 * @param ctx the parse tree
	 */
	void exitSpecparam_assignment(VerilogParser.Specparam_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
	 * @param ctx the parse tree
	 */
	void enterPulse_control_specparam(VerilogParser.Pulse_control_specparamContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
	 * @param ctx the parse tree
	 */
	void exitPulse_control_specparam(VerilogParser.Pulse_control_specparamContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#error_limit_value}.
	 * @param ctx the parse tree
	 */
	void enterError_limit_value(VerilogParser.Error_limit_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#error_limit_value}.
	 * @param ctx the parse tree
	 */
	void exitError_limit_value(VerilogParser.Error_limit_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#reject_limit_value}.
	 * @param ctx the parse tree
	 */
	void enterReject_limit_value(VerilogParser.Reject_limit_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#reject_limit_value}.
	 * @param ctx the parse tree
	 */
	void exitReject_limit_value(VerilogParser.Reject_limit_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#limit_value}.
	 * @param ctx the parse tree
	 */
	void enterLimit_value(VerilogParser.Limit_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#limit_value}.
	 * @param ctx the parse tree
	 */
	void exitLimit_value(VerilogParser.Limit_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#dimension}.
	 * @param ctx the parse tree
	 */
	void enterDimension(VerilogParser.DimensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#dimension}.
	 * @param ctx the parse tree
	 */
	void exitDimension(VerilogParser.DimensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#range_}.
	 * @param ctx the parse tree
	 */
	void enterRange_(VerilogParser.Range_Context ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#range_}.
	 * @param ctx the parse tree
	 */
	void exitRange_(VerilogParser.Range_Context ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_declaration}.
	 * @param ctx the parse tree
	 */
	void enterFunction_declaration(VerilogParser.Function_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_declaration}.
	 * @param ctx the parse tree
	 */
	void exitFunction_declaration(VerilogParser.Function_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_item_declaration}.
	 * @param ctx the parse tree
	 */
	void enterFunction_item_declaration(VerilogParser.Function_item_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_item_declaration}.
	 * @param ctx the parse tree
	 */
	void exitFunction_item_declaration(VerilogParser.Function_item_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_port_list}.
	 * @param ctx the parse tree
	 */
	void enterFunction_port_list(VerilogParser.Function_port_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_port_list}.
	 * @param ctx the parse tree
	 */
	void exitFunction_port_list(VerilogParser.Function_port_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_port}.
	 * @param ctx the parse tree
	 */
	void enterFunction_port(VerilogParser.Function_portContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_port}.
	 * @param ctx the parse tree
	 */
	void exitFunction_port(VerilogParser.Function_portContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#range_or_type}.
	 * @param ctx the parse tree
	 */
	void enterRange_or_type(VerilogParser.Range_or_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#range_or_type}.
	 * @param ctx the parse tree
	 */
	void exitRange_or_type(VerilogParser.Range_or_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTask_declaration(VerilogParser.Task_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTask_declaration(VerilogParser.Task_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_item_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTask_item_declaration(VerilogParser.Task_item_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_item_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTask_item_declaration(VerilogParser.Task_item_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_port_list}.
	 * @param ctx the parse tree
	 */
	void enterTask_port_list(VerilogParser.Task_port_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_port_list}.
	 * @param ctx the parse tree
	 */
	void exitTask_port_list(VerilogParser.Task_port_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_port_item}.
	 * @param ctx the parse tree
	 */
	void enterTask_port_item(VerilogParser.Task_port_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_port_item}.
	 * @param ctx the parse tree
	 */
	void exitTask_port_item(VerilogParser.Task_port_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tf_decl_header}.
	 * @param ctx the parse tree
	 */
	void enterTf_decl_header(VerilogParser.Tf_decl_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tf_decl_header}.
	 * @param ctx the parse tree
	 */
	void exitTf_decl_header(VerilogParser.Tf_decl_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tf_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTf_declaration(VerilogParser.Tf_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tf_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTf_declaration(VerilogParser.Tf_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_port_type}.
	 * @param ctx the parse tree
	 */
	void enterTask_port_type(VerilogParser.Task_port_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_port_type}.
	 * @param ctx the parse tree
	 */
	void exitTask_port_type(VerilogParser.Task_port_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#block_item_declaration}.
	 * @param ctx the parse tree
	 */
	void enterBlock_item_declaration(VerilogParser.Block_item_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#block_item_declaration}.
	 * @param ctx the parse tree
	 */
	void exitBlock_item_declaration(VerilogParser.Block_item_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#block_reg_declaration}.
	 * @param ctx the parse tree
	 */
	void enterBlock_reg_declaration(VerilogParser.Block_reg_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#block_reg_declaration}.
	 * @param ctx the parse tree
	 */
	void exitBlock_reg_declaration(VerilogParser.Block_reg_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
	 * @param ctx the parse tree
	 */
	void enterList_of_block_variable_identifiers(VerilogParser.List_of_block_variable_identifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
	 * @param ctx the parse tree
	 */
	void exitList_of_block_variable_identifiers(VerilogParser.List_of_block_variable_identifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#block_variable_type}.
	 * @param ctx the parse tree
	 */
	void enterBlock_variable_type(VerilogParser.Block_variable_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#block_variable_type}.
	 * @param ctx the parse tree
	 */
	void exitBlock_variable_type(VerilogParser.Block_variable_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#gate_instantiation}.
	 * @param ctx the parse tree
	 */
	void enterGate_instantiation(VerilogParser.Gate_instantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#gate_instantiation}.
	 * @param ctx the parse tree
	 */
	void exitGate_instantiation(VerilogParser.Gate_instantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
	 * @param ctx the parse tree
	 */
	void enterCmos_switch_instance(VerilogParser.Cmos_switch_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
	 * @param ctx the parse tree
	 */
	void exitCmos_switch_instance(VerilogParser.Cmos_switch_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#enable_gate_instance}.
	 * @param ctx the parse tree
	 */
	void enterEnable_gate_instance(VerilogParser.Enable_gate_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#enable_gate_instance}.
	 * @param ctx the parse tree
	 */
	void exitEnable_gate_instance(VerilogParser.Enable_gate_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#mos_switch_instance}.
	 * @param ctx the parse tree
	 */
	void enterMos_switch_instance(VerilogParser.Mos_switch_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#mos_switch_instance}.
	 * @param ctx the parse tree
	 */
	void exitMos_switch_instance(VerilogParser.Mos_switch_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
	 * @param ctx the parse tree
	 */
	void enterN_input_gate_instance(VerilogParser.N_input_gate_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
	 * @param ctx the parse tree
	 */
	void exitN_input_gate_instance(VerilogParser.N_input_gate_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
	 * @param ctx the parse tree
	 */
	void enterN_output_gate_instance(VerilogParser.N_output_gate_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
	 * @param ctx the parse tree
	 */
	void exitN_output_gate_instance(VerilogParser.N_output_gate_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pass_switch_instance}.
	 * @param ctx the parse tree
	 */
	void enterPass_switch_instance(VerilogParser.Pass_switch_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pass_switch_instance}.
	 * @param ctx the parse tree
	 */
	void exitPass_switch_instance(VerilogParser.Pass_switch_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
	 * @param ctx the parse tree
	 */
	void enterPass_enable_switch_instance(VerilogParser.Pass_enable_switch_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
	 * @param ctx the parse tree
	 */
	void exitPass_enable_switch_instance(VerilogParser.Pass_enable_switch_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pull_gate_instance}.
	 * @param ctx the parse tree
	 */
	void enterPull_gate_instance(VerilogParser.Pull_gate_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pull_gate_instance}.
	 * @param ctx the parse tree
	 */
	void exitPull_gate_instance(VerilogParser.Pull_gate_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
	 * @param ctx the parse tree
	 */
	void enterName_of_gate_instance(VerilogParser.Name_of_gate_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
	 * @param ctx the parse tree
	 */
	void exitName_of_gate_instance(VerilogParser.Name_of_gate_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pulldown_strength}.
	 * @param ctx the parse tree
	 */
	void enterPulldown_strength(VerilogParser.Pulldown_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pulldown_strength}.
	 * @param ctx the parse tree
	 */
	void exitPulldown_strength(VerilogParser.Pulldown_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pullup_strength}.
	 * @param ctx the parse tree
	 */
	void enterPullup_strength(VerilogParser.Pullup_strengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pullup_strength}.
	 * @param ctx the parse tree
	 */
	void exitPullup_strength(VerilogParser.Pullup_strengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#enable_terminal}.
	 * @param ctx the parse tree
	 */
	void enterEnable_terminal(VerilogParser.Enable_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#enable_terminal}.
	 * @param ctx the parse tree
	 */
	void exitEnable_terminal(VerilogParser.Enable_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
	 * @param ctx the parse tree
	 */
	void enterNcontrol_terminal(VerilogParser.Ncontrol_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
	 * @param ctx the parse tree
	 */
	void exitNcontrol_terminal(VerilogParser.Ncontrol_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
	 * @param ctx the parse tree
	 */
	void enterPcontrol_terminal(VerilogParser.Pcontrol_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
	 * @param ctx the parse tree
	 */
	void exitPcontrol_terminal(VerilogParser.Pcontrol_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#input_terminal}.
	 * @param ctx the parse tree
	 */
	void enterInput_terminal(VerilogParser.Input_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#input_terminal}.
	 * @param ctx the parse tree
	 */
	void exitInput_terminal(VerilogParser.Input_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#inout_terminal}.
	 * @param ctx the parse tree
	 */
	void enterInout_terminal(VerilogParser.Inout_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#inout_terminal}.
	 * @param ctx the parse tree
	 */
	void exitInout_terminal(VerilogParser.Inout_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#output_terminal}.
	 * @param ctx the parse tree
	 */
	void enterOutput_terminal(VerilogParser.Output_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#output_terminal}.
	 * @param ctx the parse tree
	 */
	void exitOutput_terminal(VerilogParser.Output_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#cmos_switchtype}.
	 * @param ctx the parse tree
	 */
	void enterCmos_switchtype(VerilogParser.Cmos_switchtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#cmos_switchtype}.
	 * @param ctx the parse tree
	 */
	void exitCmos_switchtype(VerilogParser.Cmos_switchtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#enable_gatetype}.
	 * @param ctx the parse tree
	 */
	void enterEnable_gatetype(VerilogParser.Enable_gatetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#enable_gatetype}.
	 * @param ctx the parse tree
	 */
	void exitEnable_gatetype(VerilogParser.Enable_gatetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#mos_switchtype}.
	 * @param ctx the parse tree
	 */
	void enterMos_switchtype(VerilogParser.Mos_switchtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#mos_switchtype}.
	 * @param ctx the parse tree
	 */
	void exitMos_switchtype(VerilogParser.Mos_switchtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#n_input_gatetype}.
	 * @param ctx the parse tree
	 */
	void enterN_input_gatetype(VerilogParser.N_input_gatetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#n_input_gatetype}.
	 * @param ctx the parse tree
	 */
	void exitN_input_gatetype(VerilogParser.N_input_gatetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#n_output_gatetype}.
	 * @param ctx the parse tree
	 */
	void enterN_output_gatetype(VerilogParser.N_output_gatetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#n_output_gatetype}.
	 * @param ctx the parse tree
	 */
	void exitN_output_gatetype(VerilogParser.N_output_gatetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
	 * @param ctx the parse tree
	 */
	void enterPass_en_switchtype(VerilogParser.Pass_en_switchtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
	 * @param ctx the parse tree
	 */
	void exitPass_en_switchtype(VerilogParser.Pass_en_switchtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pass_switchtype}.
	 * @param ctx the parse tree
	 */
	void enterPass_switchtype(VerilogParser.Pass_switchtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pass_switchtype}.
	 * @param ctx the parse tree
	 */
	void exitPass_switchtype(VerilogParser.Pass_switchtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_instantiation}.
	 * @param ctx the parse tree
	 */
	void enterModule_instantiation(VerilogParser.Module_instantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_instantiation}.
	 * @param ctx the parse tree
	 */
	void exitModule_instantiation(VerilogParser.Module_instantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
	 * @param ctx the parse tree
	 */
	void enterParameter_value_assignment(VerilogParser.Parameter_value_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
	 * @param ctx the parse tree
	 */
	void exitParameter_value_assignment(VerilogParser.Parameter_value_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_parameter_assignments(VerilogParser.List_of_parameter_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_parameter_assignments(VerilogParser.List_of_parameter_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
	 * @param ctx the parse tree
	 */
	void enterOrdered_parameter_assignment(VerilogParser.Ordered_parameter_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
	 * @param ctx the parse tree
	 */
	void exitOrdered_parameter_assignment(VerilogParser.Ordered_parameter_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
	 * @param ctx the parse tree
	 */
	void enterNamed_parameter_assignment(VerilogParser.Named_parameter_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
	 * @param ctx the parse tree
	 */
	void exitNamed_parameter_assignment(VerilogParser.Named_parameter_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_instance}.
	 * @param ctx the parse tree
	 */
	void enterModule_instance(VerilogParser.Module_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_instance}.
	 * @param ctx the parse tree
	 */
	void exitModule_instance(VerilogParser.Module_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#name_of_instance}.
	 * @param ctx the parse tree
	 */
	void enterName_of_instance(VerilogParser.Name_of_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#name_of_instance}.
	 * @param ctx the parse tree
	 */
	void exitName_of_instance(VerilogParser.Name_of_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_port_connections}.
	 * @param ctx the parse tree
	 */
	void enterList_of_port_connections(VerilogParser.List_of_port_connectionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_port_connections}.
	 * @param ctx the parse tree
	 */
	void exitList_of_port_connections(VerilogParser.List_of_port_connectionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#ordered_port_connection}.
	 * @param ctx the parse tree
	 */
	void enterOrdered_port_connection(VerilogParser.Ordered_port_connectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#ordered_port_connection}.
	 * @param ctx the parse tree
	 */
	void exitOrdered_port_connection(VerilogParser.Ordered_port_connectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#named_port_connection}.
	 * @param ctx the parse tree
	 */
	void enterNamed_port_connection(VerilogParser.Named_port_connectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#named_port_connection}.
	 * @param ctx the parse tree
	 */
	void exitNamed_port_connection(VerilogParser.Named_port_connectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generated_instantiation}.
	 * @param ctx the parse tree
	 */
	void enterGenerated_instantiation(VerilogParser.Generated_instantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generated_instantiation}.
	 * @param ctx the parse tree
	 */
	void exitGenerated_instantiation(VerilogParser.Generated_instantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_item_or_null}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_item_or_null(VerilogParser.Generate_item_or_nullContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_item_or_null}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_item_or_null(VerilogParser.Generate_item_or_nullContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_item}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_item(VerilogParser.Generate_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_item}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_item(VerilogParser.Generate_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_conditional_statement}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_conditional_statement(VerilogParser.Generate_conditional_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_conditional_statement}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_conditional_statement(VerilogParser.Generate_conditional_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_case_statement(VerilogParser.Generate_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_case_statement(VerilogParser.Generate_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_case_body}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_case_body(VerilogParser.Generate_case_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_case_body}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_case_body(VerilogParser.Generate_case_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_case_item}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_case_item(VerilogParser.Genvar_case_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_case_item}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_case_item(VerilogParser.Genvar_case_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_loop_statement}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_loop_statement(VerilogParser.Generate_loop_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_loop_statement}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_loop_statement(VerilogParser.Generate_loop_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_assignment}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_assignment(VerilogParser.Genvar_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_assignment}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_assignment(VerilogParser.Genvar_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_block}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_block(VerilogParser.Generate_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_block}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_block(VerilogParser.Generate_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#continuous_assign}.
	 * @param ctx the parse tree
	 */
	void enterContinuous_assign(VerilogParser.Continuous_assignContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#continuous_assign}.
	 * @param ctx the parse tree
	 */
	void exitContinuous_assign(VerilogParser.Continuous_assignContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
	 * @param ctx the parse tree
	 */
	void enterList_of_net_assignments(VerilogParser.List_of_net_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
	 * @param ctx the parse tree
	 */
	void exitList_of_net_assignments(VerilogParser.List_of_net_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_assignment}.
	 * @param ctx the parse tree
	 */
	void enterNet_assignment(VerilogParser.Net_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_assignment}.
	 * @param ctx the parse tree
	 */
	void exitNet_assignment(VerilogParser.Net_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#initial_construct}.
	 * @param ctx the parse tree
	 */
	void enterInitial_construct(VerilogParser.Initial_constructContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#initial_construct}.
	 * @param ctx the parse tree
	 */
	void exitInitial_construct(VerilogParser.Initial_constructContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#always_construct}.
	 * @param ctx the parse tree
	 */
	void enterAlways_construct(VerilogParser.Always_constructContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#always_construct}.
	 * @param ctx the parse tree
	 */
	void exitAlways_construct(VerilogParser.Always_constructContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void enterBlocking_assignment(VerilogParser.Blocking_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void exitBlocking_assignment(VerilogParser.Blocking_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
	 * @param ctx the parse tree
	 */
	void enterNonblocking_assignment(VerilogParser.Nonblocking_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
	 * @param ctx the parse tree
	 */
	void exitNonblocking_assignment(VerilogParser.Nonblocking_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
	 * @param ctx the parse tree
	 */
	void enterProcedural_continuous_assignments(VerilogParser.Procedural_continuous_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
	 * @param ctx the parse tree
	 */
	void exitProcedural_continuous_assignments(VerilogParser.Procedural_continuous_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void enterFunction_blocking_assignment(VerilogParser.Function_blocking_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_blocking_assignment}.
	 * @param ctx the parse tree
	 */
	void exitFunction_blocking_assignment(VerilogParser.Function_blocking_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_statement_or_null}.
	 * @param ctx the parse tree
	 */
	void enterFunction_statement_or_null(VerilogParser.Function_statement_or_nullContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_statement_or_null}.
	 * @param ctx the parse tree
	 */
	void exitFunction_statement_or_null(VerilogParser.Function_statement_or_nullContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_seq_block}.
	 * @param ctx the parse tree
	 */
	void enterFunction_seq_block(VerilogParser.Function_seq_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_seq_block}.
	 * @param ctx the parse tree
	 */
	void exitFunction_seq_block(VerilogParser.Function_seq_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_assignment}.
	 * @param ctx the parse tree
	 */
	void enterVariable_assignment(VerilogParser.Variable_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_assignment}.
	 * @param ctx the parse tree
	 */
	void exitVariable_assignment(VerilogParser.Variable_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#par_block}.
	 * @param ctx the parse tree
	 */
	void enterPar_block(VerilogParser.Par_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#par_block}.
	 * @param ctx the parse tree
	 */
	void exitPar_block(VerilogParser.Par_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#seq_block}.
	 * @param ctx the parse tree
	 */
	void enterSeq_block(VerilogParser.Seq_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#seq_block}.
	 * @param ctx the parse tree
	 */
	void exitSeq_block(VerilogParser.Seq_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(VerilogParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(VerilogParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#statement_or_null}.
	 * @param ctx the parse tree
	 */
	void enterStatement_or_null(VerilogParser.Statement_or_nullContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#statement_or_null}.
	 * @param ctx the parse tree
	 */
	void exitStatement_or_null(VerilogParser.Statement_or_nullContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunction_statement(VerilogParser.Function_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunction_statement(VerilogParser.Function_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delay_or_event_control}.
	 * @param ctx the parse tree
	 */
	void enterDelay_or_event_control(VerilogParser.Delay_or_event_controlContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delay_or_event_control}.
	 * @param ctx the parse tree
	 */
	void exitDelay_or_event_control(VerilogParser.Delay_or_event_controlContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delay_control}.
	 * @param ctx the parse tree
	 */
	void enterDelay_control(VerilogParser.Delay_controlContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delay_control}.
	 * @param ctx the parse tree
	 */
	void exitDelay_control(VerilogParser.Delay_controlContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#disable_statement}.
	 * @param ctx the parse tree
	 */
	void enterDisable_statement(VerilogParser.Disable_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#disable_statement}.
	 * @param ctx the parse tree
	 */
	void exitDisable_statement(VerilogParser.Disable_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_control}.
	 * @param ctx the parse tree
	 */
	void enterEvent_control(VerilogParser.Event_controlContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_control}.
	 * @param ctx the parse tree
	 */
	void exitEvent_control(VerilogParser.Event_controlContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_trigger}.
	 * @param ctx the parse tree
	 */
	void enterEvent_trigger(VerilogParser.Event_triggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_trigger}.
	 * @param ctx the parse tree
	 */
	void exitEvent_trigger(VerilogParser.Event_triggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_expression}.
	 * @param ctx the parse tree
	 */
	void enterEvent_expression(VerilogParser.Event_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_expression}.
	 * @param ctx the parse tree
	 */
	void exitEvent_expression(VerilogParser.Event_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_primary}.
	 * @param ctx the parse tree
	 */
	void enterEvent_primary(VerilogParser.Event_primaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_primary}.
	 * @param ctx the parse tree
	 */
	void exitEvent_primary(VerilogParser.Event_primaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
	 * @param ctx the parse tree
	 */
	void enterProcedural_timing_control_statement(VerilogParser.Procedural_timing_control_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
	 * @param ctx the parse tree
	 */
	void exitProcedural_timing_control_statement(VerilogParser.Procedural_timing_control_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#wait_statement}.
	 * @param ctx the parse tree
	 */
	void enterWait_statement(VerilogParser.Wait_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#wait_statement}.
	 * @param ctx the parse tree
	 */
	void exitWait_statement(VerilogParser.Wait_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void enterConditional_statement(VerilogParser.Conditional_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void exitConditional_statement(VerilogParser.Conditional_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#if_else_if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_else_if_statement(VerilogParser.If_else_if_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#if_else_if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_else_if_statement(VerilogParser.If_else_if_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_conditional_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunction_conditional_statement(VerilogParser.Function_conditional_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_conditional_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunction_conditional_statement(VerilogParser.Function_conditional_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_if_else_if_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunction_if_else_if_statement(VerilogParser.Function_if_else_if_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_if_else_if_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunction_if_else_if_statement(VerilogParser.Function_if_else_if_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCase_statement(VerilogParser.Case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCase_statement(VerilogParser.Case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#case_body}.
	 * @param ctx the parse tree
	 */
	void enterCase_body(VerilogParser.Case_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#case_body}.
	 * @param ctx the parse tree
	 */
	void exitCase_body(VerilogParser.Case_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#case_item}.
	 * @param ctx the parse tree
	 */
	void enterCase_item(VerilogParser.Case_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#case_item}.
	 * @param ctx the parse tree
	 */
	void exitCase_item(VerilogParser.Case_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunction_case_statement(VerilogParser.Function_case_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunction_case_statement(VerilogParser.Function_case_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_case_body}.
	 * @param ctx the parse tree
	 */
	void enterFunction_case_body(VerilogParser.Function_case_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_case_body}.
	 * @param ctx the parse tree
	 */
	void exitFunction_case_body(VerilogParser.Function_case_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_case_item}.
	 * @param ctx the parse tree
	 */
	void enterFunction_case_item(VerilogParser.Function_case_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_case_item}.
	 * @param ctx the parse tree
	 */
	void exitFunction_case_item(VerilogParser.Function_case_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_loop_statement}.
	 * @param ctx the parse tree
	 */
	void enterFunction_loop_statement(VerilogParser.Function_loop_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_loop_statement}.
	 * @param ctx the parse tree
	 */
	void exitFunction_loop_statement(VerilogParser.Function_loop_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void enterLoop_statement(VerilogParser.Loop_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#loop_statement}.
	 * @param ctx the parse tree
	 */
	void exitLoop_statement(VerilogParser.Loop_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#system_task_enable}.
	 * @param ctx the parse tree
	 */
	void enterSystem_task_enable(VerilogParser.System_task_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#system_task_enable}.
	 * @param ctx the parse tree
	 */
	void exitSystem_task_enable(VerilogParser.System_task_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_enable}.
	 * @param ctx the parse tree
	 */
	void enterTask_enable(VerilogParser.Task_enableContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_enable}.
	 * @param ctx the parse tree
	 */
	void exitTask_enable(VerilogParser.Task_enableContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specify_block}.
	 * @param ctx the parse tree
	 */
	void enterSpecify_block(VerilogParser.Specify_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specify_block}.
	 * @param ctx the parse tree
	 */
	void exitSpecify_block(VerilogParser.Specify_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specify_item}.
	 * @param ctx the parse tree
	 */
	void enterSpecify_item(VerilogParser.Specify_itemContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specify_item}.
	 * @param ctx the parse tree
	 */
	void exitSpecify_item(VerilogParser.Specify_itemContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPulsestyle_declaration(VerilogParser.Pulsestyle_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPulsestyle_declaration(VerilogParser.Pulsestyle_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
	 * @param ctx the parse tree
	 */
	void enterShowcancelled_declaration(VerilogParser.Showcancelled_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
	 * @param ctx the parse tree
	 */
	void exitShowcancelled_declaration(VerilogParser.Showcancelled_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#path_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPath_declaration(VerilogParser.Path_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#path_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPath_declaration(VerilogParser.Path_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#simple_path_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSimple_path_declaration(VerilogParser.Simple_path_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#simple_path_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSimple_path_declaration(VerilogParser.Simple_path_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parallel_path_description}.
	 * @param ctx the parse tree
	 */
	void enterParallel_path_description(VerilogParser.Parallel_path_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parallel_path_description}.
	 * @param ctx the parse tree
	 */
	void exitParallel_path_description(VerilogParser.Parallel_path_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#full_path_description}.
	 * @param ctx the parse tree
	 */
	void enterFull_path_description(VerilogParser.Full_path_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#full_path_description}.
	 * @param ctx the parse tree
	 */
	void exitFull_path_description(VerilogParser.Full_path_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
	 * @param ctx the parse tree
	 */
	void enterList_of_path_inputs(VerilogParser.List_of_path_inputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
	 * @param ctx the parse tree
	 */
	void exitList_of_path_inputs(VerilogParser.List_of_path_inputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
	 * @param ctx the parse tree
	 */
	void enterList_of_path_outputs(VerilogParser.List_of_path_outputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
	 * @param ctx the parse tree
	 */
	void exitList_of_path_outputs(VerilogParser.List_of_path_outputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
	 * @param ctx the parse tree
	 */
	void enterSpecify_input_terminal_descriptor(VerilogParser.Specify_input_terminal_descriptorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
	 * @param ctx the parse tree
	 */
	void exitSpecify_input_terminal_descriptor(VerilogParser.Specify_input_terminal_descriptorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
	 * @param ctx the parse tree
	 */
	void enterSpecify_output_terminal_descriptor(VerilogParser.Specify_output_terminal_descriptorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
	 * @param ctx the parse tree
	 */
	void exitSpecify_output_terminal_descriptor(VerilogParser.Specify_output_terminal_descriptorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#input_identifier}.
	 * @param ctx the parse tree
	 */
	void enterInput_identifier(VerilogParser.Input_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#input_identifier}.
	 * @param ctx the parse tree
	 */
	void exitInput_identifier(VerilogParser.Input_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#output_identifier}.
	 * @param ctx the parse tree
	 */
	void enterOutput_identifier(VerilogParser.Output_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#output_identifier}.
	 * @param ctx the parse tree
	 */
	void exitOutput_identifier(VerilogParser.Output_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#path_delay_value}.
	 * @param ctx the parse tree
	 */
	void enterPath_delay_value(VerilogParser.Path_delay_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#path_delay_value}.
	 * @param ctx the parse tree
	 */
	void exitPath_delay_value(VerilogParser.Path_delay_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
	 * @param ctx the parse tree
	 */
	void enterList_of_path_delay_expressions(VerilogParser.List_of_path_delay_expressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
	 * @param ctx the parse tree
	 */
	void exitList_of_path_delay_expressions(VerilogParser.List_of_path_delay_expressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT_path_delay_expression(VerilogParser.T_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT_path_delay_expression(VerilogParser.T_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTrise_path_delay_expression(VerilogParser.Trise_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTrise_path_delay_expression(VerilogParser.Trise_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTfall_path_delay_expression(VerilogParser.Tfall_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTfall_path_delay_expression(VerilogParser.Tfall_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTz_path_delay_expression(VerilogParser.Tz_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTz_path_delay_expression(VerilogParser.Tz_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT01_path_delay_expression(VerilogParser.T01_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT01_path_delay_expression(VerilogParser.T01_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT10_path_delay_expression(VerilogParser.T10_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT10_path_delay_expression(VerilogParser.T10_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT0z_path_delay_expression(VerilogParser.T0z_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT0z_path_delay_expression(VerilogParser.T0z_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTz1_path_delay_expression(VerilogParser.Tz1_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTz1_path_delay_expression(VerilogParser.Tz1_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT1z_path_delay_expression(VerilogParser.T1z_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT1z_path_delay_expression(VerilogParser.T1z_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTz0_path_delay_expression(VerilogParser.Tz0_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTz0_path_delay_expression(VerilogParser.Tz0_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT0x_path_delay_expression(VerilogParser.T0x_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT0x_path_delay_expression(VerilogParser.T0x_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTx1_path_delay_expression(VerilogParser.Tx1_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTx1_path_delay_expression(VerilogParser.Tx1_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterT1x_path_delay_expression(VerilogParser.T1x_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitT1x_path_delay_expression(VerilogParser.T1x_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTx0_path_delay_expression(VerilogParser.Tx0_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTx0_path_delay_expression(VerilogParser.Tx0_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTxz_path_delay_expression(VerilogParser.Txz_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTxz_path_delay_expression(VerilogParser.Txz_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterTzx_path_delay_expression(VerilogParser.Tzx_path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitTzx_path_delay_expression(VerilogParser.Tzx_path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void enterPath_delay_expression(VerilogParser.Path_delay_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#path_delay_expression}.
	 * @param ctx the parse tree
	 */
	void exitPath_delay_expression(VerilogParser.Path_delay_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEdge_sensitive_path_declaration(VerilogParser.Edge_sensitive_path_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEdge_sensitive_path_declaration(VerilogParser.Edge_sensitive_path_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
	 * @param ctx the parse tree
	 */
	void enterParallel_edge_sensitive_path_description(VerilogParser.Parallel_edge_sensitive_path_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
	 * @param ctx the parse tree
	 */
	void exitParallel_edge_sensitive_path_description(VerilogParser.Parallel_edge_sensitive_path_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
	 * @param ctx the parse tree
	 */
	void enterFull_edge_sensitive_path_description(VerilogParser.Full_edge_sensitive_path_descriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
	 * @param ctx the parse tree
	 */
	void exitFull_edge_sensitive_path_description(VerilogParser.Full_edge_sensitive_path_descriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#data_source_expression}.
	 * @param ctx the parse tree
	 */
	void enterData_source_expression(VerilogParser.Data_source_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#data_source_expression}.
	 * @param ctx the parse tree
	 */
	void exitData_source_expression(VerilogParser.Data_source_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#edge_identifier}.
	 * @param ctx the parse tree
	 */
	void enterEdge_identifier(VerilogParser.Edge_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#edge_identifier}.
	 * @param ctx the parse tree
	 */
	void exitEdge_identifier(VerilogParser.Edge_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
	 * @param ctx the parse tree
	 */
	void enterState_dependent_path_declaration(VerilogParser.State_dependent_path_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
	 * @param ctx the parse tree
	 */
	void exitState_dependent_path_declaration(VerilogParser.State_dependent_path_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#polarity_operator}.
	 * @param ctx the parse tree
	 */
	void enterPolarity_operator(VerilogParser.Polarity_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#polarity_operator}.
	 * @param ctx the parse tree
	 */
	void exitPolarity_operator(VerilogParser.Polarity_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#checktime_condition}.
	 * @param ctx the parse tree
	 */
	void enterChecktime_condition(VerilogParser.Checktime_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#checktime_condition}.
	 * @param ctx the parse tree
	 */
	void exitChecktime_condition(VerilogParser.Checktime_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delayed_data}.
	 * @param ctx the parse tree
	 */
	void enterDelayed_data(VerilogParser.Delayed_dataContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delayed_data}.
	 * @param ctx the parse tree
	 */
	void exitDelayed_data(VerilogParser.Delayed_dataContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#delayed_reference}.
	 * @param ctx the parse tree
	 */
	void enterDelayed_reference(VerilogParser.Delayed_referenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#delayed_reference}.
	 * @param ctx the parse tree
	 */
	void exitDelayed_reference(VerilogParser.Delayed_referenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#end_edge_offset}.
	 * @param ctx the parse tree
	 */
	void enterEnd_edge_offset(VerilogParser.End_edge_offsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#end_edge_offset}.
	 * @param ctx the parse tree
	 */
	void exitEnd_edge_offset(VerilogParser.End_edge_offsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_based_flag}.
	 * @param ctx the parse tree
	 */
	void enterEvent_based_flag(VerilogParser.Event_based_flagContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_based_flag}.
	 * @param ctx the parse tree
	 */
	void exitEvent_based_flag(VerilogParser.Event_based_flagContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#notify_reg}.
	 * @param ctx the parse tree
	 */
	void enterNotify_reg(VerilogParser.Notify_regContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#notify_reg}.
	 * @param ctx the parse tree
	 */
	void exitNotify_reg(VerilogParser.Notify_regContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#remain_active_flag}.
	 * @param ctx the parse tree
	 */
	void enterRemain_active_flag(VerilogParser.Remain_active_flagContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#remain_active_flag}.
	 * @param ctx the parse tree
	 */
	void exitRemain_active_flag(VerilogParser.Remain_active_flagContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#stamptime_condition}.
	 * @param ctx the parse tree
	 */
	void enterStamptime_condition(VerilogParser.Stamptime_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#stamptime_condition}.
	 * @param ctx the parse tree
	 */
	void exitStamptime_condition(VerilogParser.Stamptime_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#start_edge_offset}.
	 * @param ctx the parse tree
	 */
	void enterStart_edge_offset(VerilogParser.Start_edge_offsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#start_edge_offset}.
	 * @param ctx the parse tree
	 */
	void exitStart_edge_offset(VerilogParser.Start_edge_offsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#threshold}.
	 * @param ctx the parse tree
	 */
	void enterThreshold(VerilogParser.ThresholdContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#threshold}.
	 * @param ctx the parse tree
	 */
	void exitThreshold(VerilogParser.ThresholdContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#timing_check_limit}.
	 * @param ctx the parse tree
	 */
	void enterTiming_check_limit(VerilogParser.Timing_check_limitContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#timing_check_limit}.
	 * @param ctx the parse tree
	 */
	void exitTiming_check_limit(VerilogParser.Timing_check_limitContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConcatenation(VerilogParser.ConcatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConcatenation(VerilogParser.ConcatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConstant_concatenation(VerilogParser.Constant_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConstant_concatenation(VerilogParser.Constant_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterConstant_multiple_concatenation(VerilogParser.Constant_multiple_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitConstant_multiple_concatenation(VerilogParser.Constant_multiple_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_concatenation(VerilogParser.Module_path_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_concatenation(VerilogParser.Module_path_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_multiple_concatenation(VerilogParser.Module_path_multiple_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_multiple_concatenation(VerilogParser.Module_path_multiple_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterMultiple_concatenation(VerilogParser.Multiple_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#multiple_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitMultiple_concatenation(VerilogParser.Multiple_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterNet_concatenation(VerilogParser.Net_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitNet_concatenation(VerilogParser.Net_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_concatenation_value}.
	 * @param ctx the parse tree
	 */
	void enterNet_concatenation_value(VerilogParser.Net_concatenation_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_concatenation_value}.
	 * @param ctx the parse tree
	 */
	void exitNet_concatenation_value(VerilogParser.Net_concatenation_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_concatenation}.
	 * @param ctx the parse tree
	 */
	void enterVariable_concatenation(VerilogParser.Variable_concatenationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_concatenation}.
	 * @param ctx the parse tree
	 */
	void exitVariable_concatenation(VerilogParser.Variable_concatenationContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_concatenation_value}.
	 * @param ctx the parse tree
	 */
	void enterVariable_concatenation_value(VerilogParser.Variable_concatenation_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_concatenation_value}.
	 * @param ctx the parse tree
	 */
	void exitVariable_concatenation_value(VerilogParser.Variable_concatenation_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_function_call}.
	 * @param ctx the parse tree
	 */
	void enterConstant_function_call(VerilogParser.Constant_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_function_call}.
	 * @param ctx the parse tree
	 */
	void exitConstant_function_call(VerilogParser.Constant_function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(VerilogParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(VerilogParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#system_function_call}.
	 * @param ctx the parse tree
	 */
	void enterSystem_function_call(VerilogParser.System_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#system_function_call}.
	 * @param ctx the parse tree
	 */
	void exitSystem_function_call(VerilogParser.System_function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_function_call}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_function_call(VerilogParser.Genvar_function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_function_call}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_function_call(VerilogParser.Genvar_function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#base_expression}.
	 * @param ctx the parse tree
	 */
	void enterBase_expression(VerilogParser.Base_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#base_expression}.
	 * @param ctx the parse tree
	 */
	void exitBase_expression(VerilogParser.Base_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_base_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_base_expression(VerilogParser.Constant_base_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_base_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_base_expression(VerilogParser.Constant_base_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_expression(VerilogParser.Constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_expression(VerilogParser.Constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_mintypmax_expression(VerilogParser.Constant_mintypmax_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_mintypmax_expression(VerilogParser.Constant_mintypmax_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_range_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstant_range_expression(VerilogParser.Constant_range_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_range_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstant_range_expression(VerilogParser.Constant_range_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterDimension_constant_expression(VerilogParser.Dimension_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitDimension_constant_expression(VerilogParser.Dimension_constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(VerilogParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(VerilogParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(VerilogParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(VerilogParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterLsb_constant_expression(VerilogParser.Lsb_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitLsb_constant_expression(VerilogParser.Lsb_constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void enterMintypmax_expression(VerilogParser.Mintypmax_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void exitMintypmax_expression(VerilogParser.Mintypmax_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_conditional_expression}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_conditional_expression(VerilogParser.Module_path_conditional_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_conditional_expression}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_conditional_expression(VerilogParser.Module_path_conditional_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_expression}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_expression(VerilogParser.Module_path_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_expression}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_expression(VerilogParser.Module_path_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_mintypmax_expression(VerilogParser.Module_path_mintypmax_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_mintypmax_expression(VerilogParser.Module_path_mintypmax_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#msb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterMsb_constant_expression(VerilogParser.Msb_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#msb_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitMsb_constant_expression(VerilogParser.Msb_constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#range_expression}.
	 * @param ctx the parse tree
	 */
	void enterRange_expression(VerilogParser.Range_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#range_expression}.
	 * @param ctx the parse tree
	 */
	void exitRange_expression(VerilogParser.Range_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#width_constant_expression}.
	 * @param ctx the parse tree
	 */
	void enterWidth_constant_expression(VerilogParser.Width_constant_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#width_constant_expression}.
	 * @param ctx the parse tree
	 */
	void exitWidth_constant_expression(VerilogParser.Width_constant_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#constant_primary}.
	 * @param ctx the parse tree
	 */
	void enterConstant_primary(VerilogParser.Constant_primaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#constant_primary}.
	 * @param ctx the parse tree
	 */
	void exitConstant_primary(VerilogParser.Constant_primaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_path_primary}.
	 * @param ctx the parse tree
	 */
	void enterModule_path_primary(VerilogParser.Module_path_primaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_path_primary}.
	 * @param ctx the parse tree
	 */
	void exitModule_path_primary(VerilogParser.Module_path_primaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(VerilogParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(VerilogParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_lvalue}.
	 * @param ctx the parse tree
	 */
	void enterNet_lvalue(VerilogParser.Net_lvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_lvalue}.
	 * @param ctx the parse tree
	 */
	void exitNet_lvalue(VerilogParser.Net_lvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_lvalue}.
	 * @param ctx the parse tree
	 */
	void enterVariable_lvalue(VerilogParser.Variable_lvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_lvalue}.
	 * @param ctx the parse tree
	 */
	void exitVariable_lvalue(VerilogParser.Variable_lvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(VerilogParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(VerilogParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#binary_operator}.
	 * @param ctx the parse tree
	 */
	void enterBinary_operator(VerilogParser.Binary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#binary_operator}.
	 * @param ctx the parse tree
	 */
	void exitBinary_operator(VerilogParser.Binary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_module_path_operator(VerilogParser.Unary_module_path_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_module_path_operator(VerilogParser.Unary_module_path_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#binary_module_path_operator}.
	 * @param ctx the parse tree
	 */
	void enterBinary_module_path_operator(VerilogParser.Binary_module_path_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#binary_module_path_operator}.
	 * @param ctx the parse tree
	 */
	void exitBinary_module_path_operator(VerilogParser.Binary_module_path_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(VerilogParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(VerilogParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#attribute_instance}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_instance(VerilogParser.Attribute_instanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#attribute_instance}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_instance(VerilogParser.Attribute_instanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#attr_spec}.
	 * @param ctx the parse tree
	 */
	void enterAttr_spec(VerilogParser.Attr_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#attr_spec}.
	 * @param ctx the parse tree
	 */
	void exitAttr_spec(VerilogParser.Attr_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void enterAttr_name(VerilogParser.Attr_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#attr_name}.
	 * @param ctx the parse tree
	 */
	void exitAttr_name(VerilogParser.Attr_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void enterArrayed_identifier(VerilogParser.Arrayed_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void exitArrayed_identifier(VerilogParser.Arrayed_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#block_identifier}.
	 * @param ctx the parse tree
	 */
	void enterBlock_identifier(VerilogParser.Block_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#block_identifier}.
	 * @param ctx the parse tree
	 */
	void exitBlock_identifier(VerilogParser.Block_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#cell_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCell_identifier(VerilogParser.Cell_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#cell_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCell_identifier(VerilogParser.Cell_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#config_identifier}.
	 * @param ctx the parse tree
	 */
	void enterConfig_identifier(VerilogParser.Config_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#config_identifier}.
	 * @param ctx the parse tree
	 */
	void exitConfig_identifier(VerilogParser.Config_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#escaped_arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void enterEscaped_arrayed_identifier(VerilogParser.Escaped_arrayed_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#escaped_arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void exitEscaped_arrayed_identifier(VerilogParser.Escaped_arrayed_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#escaped_hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void enterEscaped_hierarchical_identifier(VerilogParser.Escaped_hierarchical_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#escaped_hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void exitEscaped_hierarchical_identifier(VerilogParser.Escaped_hierarchical_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#event_identifier}.
	 * @param ctx the parse tree
	 */
	void enterEvent_identifier(VerilogParser.Event_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#event_identifier}.
	 * @param ctx the parse tree
	 */
	void exitEvent_identifier(VerilogParser.Event_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#function_identifier}.
	 * @param ctx the parse tree
	 */
	void enterFunction_identifier(VerilogParser.Function_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#function_identifier}.
	 * @param ctx the parse tree
	 */
	void exitFunction_identifier(VerilogParser.Function_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterGate_instance_identifier(VerilogParser.Gate_instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitGate_instance_identifier(VerilogParser.Gate_instance_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#generate_block_identifier}.
	 * @param ctx the parse tree
	 */
	void enterGenerate_block_identifier(VerilogParser.Generate_block_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#generate_block_identifier}.
	 * @param ctx the parse tree
	 */
	void exitGenerate_block_identifier(VerilogParser.Generate_block_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_function_identifier}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_function_identifier(VerilogParser.Genvar_function_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_function_identifier}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_function_identifier(VerilogParser.Genvar_function_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#genvar_identifier}.
	 * @param ctx the parse tree
	 */
	void enterGenvar_identifier(VerilogParser.Genvar_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#genvar_identifier}.
	 * @param ctx the parse tree
	 */
	void exitGenvar_identifier(VerilogParser.Genvar_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_block_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_block_identifier(VerilogParser.Hierarchical_block_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_block_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_block_identifier(VerilogParser.Hierarchical_block_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_event_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_event_identifier(VerilogParser.Hierarchical_event_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_event_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_event_identifier(VerilogParser.Hierarchical_event_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_function_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_function_identifier(VerilogParser.Hierarchical_function_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_function_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_function_identifier(VerilogParser.Hierarchical_function_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_identifier(VerilogParser.Hierarchical_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_identifier(VerilogParser.Hierarchical_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_net_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_net_identifier(VerilogParser.Hierarchical_net_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_net_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_net_identifier(VerilogParser.Hierarchical_net_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_variable_identifier(VerilogParser.Hierarchical_variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_variable_identifier(VerilogParser.Hierarchical_variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#hierarchical_task_identifier}.
	 * @param ctx the parse tree
	 */
	void enterHierarchical_task_identifier(VerilogParser.Hierarchical_task_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#hierarchical_task_identifier}.
	 * @param ctx the parse tree
	 */
	void exitHierarchical_task_identifier(VerilogParser.Hierarchical_task_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(VerilogParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(VerilogParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#inout_port_identifier}.
	 * @param ctx the parse tree
	 */
	void enterInout_port_identifier(VerilogParser.Inout_port_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#inout_port_identifier}.
	 * @param ctx the parse tree
	 */
	void exitInout_port_identifier(VerilogParser.Inout_port_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#input_port_identifier}.
	 * @param ctx the parse tree
	 */
	void enterInput_port_identifier(VerilogParser.Input_port_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#input_port_identifier}.
	 * @param ctx the parse tree
	 */
	void exitInput_port_identifier(VerilogParser.Input_port_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterInstance_identifier(VerilogParser.Instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitInstance_identifier(VerilogParser.Instance_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#library_identifier}.
	 * @param ctx the parse tree
	 */
	void enterLibrary_identifier(VerilogParser.Library_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#library_identifier}.
	 * @param ctx the parse tree
	 */
	void exitLibrary_identifier(VerilogParser.Library_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#memory_identifier}.
	 * @param ctx the parse tree
	 */
	void enterMemory_identifier(VerilogParser.Memory_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#memory_identifier}.
	 * @param ctx the parse tree
	 */
	void exitMemory_identifier(VerilogParser.Memory_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_identifier}.
	 * @param ctx the parse tree
	 */
	void enterModule_identifier(VerilogParser.Module_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_identifier}.
	 * @param ctx the parse tree
	 */
	void exitModule_identifier(VerilogParser.Module_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#module_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterModule_instance_identifier(VerilogParser.Module_instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#module_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitModule_instance_identifier(VerilogParser.Module_instance_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#net_identifier}.
	 * @param ctx the parse tree
	 */
	void enterNet_identifier(VerilogParser.Net_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#net_identifier}.
	 * @param ctx the parse tree
	 */
	void exitNet_identifier(VerilogParser.Net_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#output_port_identifier}.
	 * @param ctx the parse tree
	 */
	void enterOutput_port_identifier(VerilogParser.Output_port_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#output_port_identifier}.
	 * @param ctx the parse tree
	 */
	void exitOutput_port_identifier(VerilogParser.Output_port_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#parameter_identifier}.
	 * @param ctx the parse tree
	 */
	void enterParameter_identifier(VerilogParser.Parameter_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#parameter_identifier}.
	 * @param ctx the parse tree
	 */
	void exitParameter_identifier(VerilogParser.Parameter_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#port_identifier}.
	 * @param ctx the parse tree
	 */
	void enterPort_identifier(VerilogParser.Port_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#port_identifier}.
	 * @param ctx the parse tree
	 */
	void exitPort_identifier(VerilogParser.Port_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#real_identifier}.
	 * @param ctx the parse tree
	 */
	void enterReal_identifier(VerilogParser.Real_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#real_identifier}.
	 * @param ctx the parse tree
	 */
	void exitReal_identifier(VerilogParser.Real_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#simple_arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSimple_arrayed_identifier(VerilogParser.Simple_arrayed_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#simple_arrayed_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSimple_arrayed_identifier(VerilogParser.Simple_arrayed_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#simple_hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSimple_hierarchical_identifier(VerilogParser.Simple_hierarchical_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#simple_hierarchical_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSimple_hierarchical_identifier(VerilogParser.Simple_hierarchical_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#specparam_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSpecparam_identifier(VerilogParser.Specparam_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#specparam_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSpecparam_identifier(VerilogParser.Specparam_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#system_function_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSystem_function_identifier(VerilogParser.System_function_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#system_function_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSystem_function_identifier(VerilogParser.System_function_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#system_task_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSystem_task_identifier(VerilogParser.System_task_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#system_task_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSystem_task_identifier(VerilogParser.System_task_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#task_identifier}.
	 * @param ctx the parse tree
	 */
	void enterTask_identifier(VerilogParser.Task_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#task_identifier}.
	 * @param ctx the parse tree
	 */
	void exitTask_identifier(VerilogParser.Task_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#terminal_identifier}.
	 * @param ctx the parse tree
	 */
	void enterTerminal_identifier(VerilogParser.Terminal_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#terminal_identifier}.
	 * @param ctx the parse tree
	 */
	void exitTerminal_identifier(VerilogParser.Terminal_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#text_macro_identifier}.
	 * @param ctx the parse tree
	 */
	void enterText_macro_identifier(VerilogParser.Text_macro_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#text_macro_identifier}.
	 * @param ctx the parse tree
	 */
	void exitText_macro_identifier(VerilogParser.Text_macro_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#topmodule_identifier}.
	 * @param ctx the parse tree
	 */
	void enterTopmodule_identifier(VerilogParser.Topmodule_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#topmodule_identifier}.
	 * @param ctx the parse tree
	 */
	void exitTopmodule_identifier(VerilogParser.Topmodule_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#udp_identifier}.
	 * @param ctx the parse tree
	 */
	void enterUdp_identifier(VerilogParser.Udp_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#udp_identifier}.
	 * @param ctx the parse tree
	 */
	void exitUdp_identifier(VerilogParser.Udp_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void enterUdp_instance_identifier(VerilogParser.Udp_instance_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
	 * @param ctx the parse tree
	 */
	void exitUdp_instance_identifier(VerilogParser.Udp_instance_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier(VerilogParser.Variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier(VerilogParser.Variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#simple_hierarchical_branch}.
	 * @param ctx the parse tree
	 */
	void enterSimple_hierarchical_branch(VerilogParser.Simple_hierarchical_branchContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#simple_hierarchical_branch}.
	 * @param ctx the parse tree
	 */
	void exitSimple_hierarchical_branch(VerilogParser.Simple_hierarchical_branchContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#escaped_hierarchical_branch}.
	 * @param ctx the parse tree
	 */
	void enterEscaped_hierarchical_branch(VerilogParser.Escaped_hierarchical_branchContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#escaped_hierarchical_branch}.
	 * @param ctx the parse tree
	 */
	void exitEscaped_hierarchical_branch(VerilogParser.Escaped_hierarchical_branchContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#incomplete_statement}.
	 * @param ctx the parse tree
	 */
	void enterIncomplete_statement(VerilogParser.Incomplete_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#incomplete_statement}.
	 * @param ctx the parse tree
	 */
	void exitIncomplete_statement(VerilogParser.Incomplete_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VerilogParser#incomplete_condition_statement}.
	 * @param ctx the parse tree
	 */
	void enterIncomplete_condition_statement(VerilogParser.Incomplete_condition_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VerilogParser#incomplete_condition_statement}.
	 * @param ctx the parse tree
	 */
	void exitIncomplete_condition_statement(VerilogParser.Incomplete_condition_statementContext ctx);
}