// Generated from java-escape by ANTLR 4.11.1
package top.criwits.scaleda.tcl.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TclParser}.
 */
public interface TclListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio}.
	 * @param ctx the parse tree
	 */
	void enterInicio(TclParser.InicioContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio}.
	 * @param ctx the parse tree
	 */
	void exitInicio(TclParser.InicioContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#declaracion_funcion}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracion_funcion(TclParser.Declaracion_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#declaracion_funcion}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracion_funcion(TclParser.Declaracion_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#args_funcion}.
	 * @param ctx the parse tree
	 */
	void enterArgs_funcion(TclParser.Args_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#args_funcion}.
	 * @param ctx the parse tree
	 */
	void exitArgs_funcion(TclParser.Args_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#cuerpo_funcion}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo_funcion(TclParser.Cuerpo_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#cuerpo_funcion}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo_funcion(TclParser.Cuerpo_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#if_funcion}.
	 * @param ctx the parse tree
	 */
	void enterIf_funcion(TclParser.If_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#if_funcion}.
	 * @param ctx the parse tree
	 */
	void exitIf_funcion(TclParser.If_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elseif_funcion}.
	 * @param ctx the parse tree
	 */
	void enterElseif_funcion(TclParser.Elseif_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elseif_funcion}.
	 * @param ctx the parse tree
	 */
	void exitElseif_funcion(TclParser.Elseif_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#else_funcion}.
	 * @param ctx the parse tree
	 */
	void enterElse_funcion(TclParser.Else_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#else_funcion}.
	 * @param ctx the parse tree
	 */
	void exitElse_funcion(TclParser.Else_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switch_funcion}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_funcion(TclParser.Switch_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switch_funcion}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_funcion(TclParser.Switch_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case_funcion}.
	 * @param ctx the parse tree
	 */
	void enterCase_funcion(TclParser.Case_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case_funcion}.
	 * @param ctx the parse tree
	 */
	void exitCase_funcion(TclParser.Case_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case2_funcion}.
	 * @param ctx the parse tree
	 */
	void enterCase2_funcion(TclParser.Case2_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case2_funcion}.
	 * @param ctx the parse tree
	 */
	void exitCase2_funcion(TclParser.Case2_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#default_funcion}.
	 * @param ctx the parse tree
	 */
	void enterDefault_funcion(TclParser.Default_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#default_funcion}.
	 * @param ctx the parse tree
	 */
	void exitDefault_funcion(TclParser.Default_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#for_funcion}.
	 * @param ctx the parse tree
	 */
	void enterFor_funcion(TclParser.For_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#for_funcion}.
	 * @param ctx the parse tree
	 */
	void exitFor_funcion(TclParser.For_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#while_funcion}.
	 * @param ctx the parse tree
	 */
	void enterWhile_funcion(TclParser.While_funcionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#while_funcion}.
	 * @param ctx the parse tree
	 */
	void exitWhile_funcion(TclParser.While_funcionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#cuerpo_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo_loop_func(TclParser.Cuerpo_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#cuerpo_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo_loop_func(TclParser.Cuerpo_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#if_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterIf_loop_func(TclParser.If_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#if_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitIf_loop_func(TclParser.If_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elseif_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterElseif_loop_func(TclParser.Elseif_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elseif_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitElseif_loop_func(TclParser.Elseif_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#else_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterElse_loop_func(TclParser.Else_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#else_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitElse_loop_func(TclParser.Else_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switch_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_loop_func(TclParser.Switch_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switch_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_loop_func(TclParser.Switch_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterCase_loop_func(TclParser.Case_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitCase_loop_func(TclParser.Case_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case2_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterCase2_loop_func(TclParser.Case2_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case2_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitCase2_loop_func(TclParser.Case2_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#default_loop_func}.
	 * @param ctx the parse tree
	 */
	void enterDefault_loop_func(TclParser.Default_loop_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#default_loop_func}.
	 * @param ctx the parse tree
	 */
	void exitDefault_loop_func(TclParser.Default_loop_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#modulo_ppal}.
	 * @param ctx the parse tree
	 */
	void enterModulo_ppal(TclParser.Modulo_ppalContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#modulo_ppal}.
	 * @param ctx the parse tree
	 */
	void exitModulo_ppal(TclParser.Modulo_ppalContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_if}.
	 * @param ctx the parse tree
	 */
	void enterR_if(TclParser.R_ifContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_if}.
	 * @param ctx the parse tree
	 */
	void exitR_if(TclParser.R_ifContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elseif}.
	 * @param ctx the parse tree
	 */
	void enterElseif(TclParser.ElseifContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elseif}.
	 * @param ctx the parse tree
	 */
	void exitElseif(TclParser.ElseifContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_else}.
	 * @param ctx the parse tree
	 */
	void enterR_else(TclParser.R_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_else}.
	 * @param ctx the parse tree
	 */
	void exitR_else(TclParser.R_elseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_switch}.
	 * @param ctx the parse tree
	 */
	void enterR_switch(TclParser.R_switchContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_switch}.
	 * @param ctx the parse tree
	 */
	void exitR_switch(TclParser.R_switchContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_case}.
	 * @param ctx the parse tree
	 */
	void enterR_case(TclParser.R_caseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_case}.
	 * @param ctx the parse tree
	 */
	void exitR_case(TclParser.R_caseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case2}.
	 * @param ctx the parse tree
	 */
	void enterCase2(TclParser.Case2Context ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case2}.
	 * @param ctx the parse tree
	 */
	void exitCase2(TclParser.Case2Context ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_default}.
	 * @param ctx the parse tree
	 */
	void enterR_default(TclParser.R_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_default}.
	 * @param ctx the parse tree
	 */
	void exitR_default(TclParser.R_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#cuerpo_inst}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo_inst(TclParser.Cuerpo_instContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#cuerpo_inst}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo_inst(TclParser.Cuerpo_instContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_for}.
	 * @param ctx the parse tree
	 */
	void enterR_for(TclParser.R_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_for}.
	 * @param ctx the parse tree
	 */
	void exitR_for(TclParser.R_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_while}.
	 * @param ctx the parse tree
	 */
	void enterR_while(TclParser.R_whileContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_while}.
	 * @param ctx the parse tree
	 */
	void exitR_while(TclParser.R_whileContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#cuerpo_loop}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo_loop(TclParser.Cuerpo_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#cuerpo_loop}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo_loop(TclParser.Cuerpo_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#if_loop}.
	 * @param ctx the parse tree
	 */
	void enterIf_loop(TclParser.If_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#if_loop}.
	 * @param ctx the parse tree
	 */
	void exitIf_loop(TclParser.If_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elseif_loop}.
	 * @param ctx the parse tree
	 */
	void enterElseif_loop(TclParser.Elseif_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elseif_loop}.
	 * @param ctx the parse tree
	 */
	void exitElseif_loop(TclParser.Elseif_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#else_loop}.
	 * @param ctx the parse tree
	 */
	void enterElse_loop(TclParser.Else_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#else_loop}.
	 * @param ctx the parse tree
	 */
	void exitElse_loop(TclParser.Else_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switch_loop}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_loop(TclParser.Switch_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switch_loop}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_loop(TclParser.Switch_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case_loop}.
	 * @param ctx the parse tree
	 */
	void enterCase_loop(TclParser.Case_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case_loop}.
	 * @param ctx the parse tree
	 */
	void exitCase_loop(TclParser.Case_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#case2_loop}.
	 * @param ctx the parse tree
	 */
	void enterCase2_loop(TclParser.Case2_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#case2_loop}.
	 * @param ctx the parse tree
	 */
	void exitCase2_loop(TclParser.Case2_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#default_loop}.
	 * @param ctx the parse tree
	 */
	void enterDefault_loop(TclParser.Default_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#default_loop}.
	 * @param ctx the parse tree
	 */
	void exitDefault_loop(TclParser.Default_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#puts}.
	 * @param ctx the parse tree
	 */
	void enterPuts(TclParser.PutsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#puts}.
	 * @param ctx the parse tree
	 */
	void exitPuts(TclParser.PutsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#gets}.
	 * @param ctx the parse tree
	 */
	void enterGets(TclParser.GetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#gets}.
	 * @param ctx the parse tree
	 */
	void exitGets(TclParser.GetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void enterDeclaracion(TclParser.DeclaracionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#declaracion}.
	 * @param ctx the parse tree
	 */
	void exitDeclaracion(TclParser.DeclaracionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#agrup}.
	 * @param ctx the parse tree
	 */
	void enterAgrup(TclParser.AgrupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#agrup}.
	 * @param ctx the parse tree
	 */
	void exitAgrup(TclParser.AgrupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#aux_agrup}.
	 * @param ctx the parse tree
	 */
	void enterAux_agrup(TclParser.Aux_agrupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#aux_agrup}.
	 * @param ctx the parse tree
	 */
	void exitAux_agrup(TclParser.Aux_agrupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#aux_array}.
	 * @param ctx the parse tree
	 */
	void enterAux_array(TclParser.Aux_arrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#aux_array}.
	 * @param ctx the parse tree
	 */
	void exitAux_array(TclParser.Aux_arrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#param_func}.
	 * @param ctx the parse tree
	 */
	void enterParam_func(TclParser.Param_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#param_func}.
	 * @param ctx the parse tree
	 */
	void exitParam_func(TclParser.Param_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#aux_param}.
	 * @param ctx the parse tree
	 */
	void enterAux_param(TclParser.Aux_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#aux_param}.
	 * @param ctx the parse tree
	 */
	void exitAux_param(TclParser.Aux_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void enterAsignacion(TclParser.AsignacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void exitAsignacion(TclParser.AsignacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#indice}.
	 * @param ctx the parse tree
	 */
	void enterIndice(TclParser.IndiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#indice}.
	 * @param ctx the parse tree
	 */
	void exitIndice(TclParser.IndiceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#val_indice}.
	 * @param ctx the parse tree
	 */
	void enterVal_indice(TclParser.Val_indiceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#val_indice}.
	 * @param ctx the parse tree
	 */
	void exitVal_indice(TclParser.Val_indiceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#valor}.
	 * @param ctx the parse tree
	 */
	void enterValor(TclParser.ValorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#valor}.
	 * @param ctx the parse tree
	 */
	void exitValor(TclParser.ValorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#incremento}.
	 * @param ctx the parse tree
	 */
	void enterIncremento(TclParser.IncrementoContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#incremento}.
	 * @param ctx the parse tree
	 */
	void exitIncremento(TclParser.IncrementoContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_break}.
	 * @param ctx the parse tree
	 */
	void enterR_break(TclParser.R_breakContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_break}.
	 * @param ctx the parse tree
	 */
	void exitR_break(TclParser.R_breakContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_continue}.
	 * @param ctx the parse tree
	 */
	void enterR_continue(TclParser.R_continueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_continue}.
	 * @param ctx the parse tree
	 */
	void exitR_continue(TclParser.R_continueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#r_return}.
	 * @param ctx the parse tree
	 */
	void enterR_return(TclParser.R_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#r_return}.
	 * @param ctx the parse tree
	 */
	void exitR_return(TclParser.R_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#value_return}.
	 * @param ctx the parse tree
	 */
	void enterValue_return(TclParser.Value_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#value_return}.
	 * @param ctx the parse tree
	 */
	void exitValue_return(TclParser.Value_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(TclParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(TclParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#asig_for}.
	 * @param ctx the parse tree
	 */
	void enterAsig_for(TclParser.Asig_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#asig_for}.
	 * @param ctx the parse tree
	 */
	void exitAsig_for(TclParser.Asig_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_if}.
	 * @param ctx the parse tree
	 */
	void enterInicio_if(TclParser.Inicio_ifContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_if}.
	 * @param ctx the parse tree
	 */
	void exitInicio_if(TclParser.Inicio_ifContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_elseif}.
	 * @param ctx the parse tree
	 */
	void enterInicio_elseif(TclParser.Inicio_elseifContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_elseif}.
	 * @param ctx the parse tree
	 */
	void exitInicio_elseif(TclParser.Inicio_elseifContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_else}.
	 * @param ctx the parse tree
	 */
	void enterInicio_else(TclParser.Inicio_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_else}.
	 * @param ctx the parse tree
	 */
	void exitInicio_else(TclParser.Inicio_elseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_switch}.
	 * @param ctx the parse tree
	 */
	void enterInicio_switch(TclParser.Inicio_switchContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_switch}.
	 * @param ctx the parse tree
	 */
	void exitInicio_switch(TclParser.Inicio_switchContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_case}.
	 * @param ctx the parse tree
	 */
	void enterInicio_case(TclParser.Inicio_caseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_case}.
	 * @param ctx the parse tree
	 */
	void exitInicio_case(TclParser.Inicio_caseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_default}.
	 * @param ctx the parse tree
	 */
	void enterInicio_default(TclParser.Inicio_defaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_default}.
	 * @param ctx the parse tree
	 */
	void exitInicio_default(TclParser.Inicio_defaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_for}.
	 * @param ctx the parse tree
	 */
	void enterInicio_for(TclParser.Inicio_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_for}.
	 * @param ctx the parse tree
	 */
	void exitInicio_for(TclParser.Inicio_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#dec_for}.
	 * @param ctx the parse tree
	 */
	void enterDec_for(TclParser.Dec_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#dec_for}.
	 * @param ctx the parse tree
	 */
	void exitDec_for(TclParser.Dec_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#inicio_while}.
	 * @param ctx the parse tree
	 */
	void enterInicio_while(TclParser.Inicio_whileContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#inicio_while}.
	 * @param ctx the parse tree
	 */
	void exitInicio_while(TclParser.Inicio_whileContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#expresion}.
	 * @param ctx the parse tree
	 */
	void enterExpresion(TclParser.ExpresionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#expresion}.
	 * @param ctx the parse tree
	 */
	void exitExpresion(TclParser.ExpresionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_or}.
	 * @param ctx the parse tree
	 */
	void enterExp_or(TclParser.Exp_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_or}.
	 * @param ctx the parse tree
	 */
	void exitExp_or(TclParser.Exp_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_and}.
	 * @param ctx the parse tree
	 */
	void enterExp_and(TclParser.Exp_andContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_and}.
	 * @param ctx the parse tree
	 */
	void exitExp_and(TclParser.Exp_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_ig}.
	 * @param ctx the parse tree
	 */
	void enterExp_ig(TclParser.Exp_igContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_ig}.
	 * @param ctx the parse tree
	 */
	void exitExp_ig(TclParser.Exp_igContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_rel}.
	 * @param ctx the parse tree
	 */
	void enterExp_rel(TclParser.Exp_relContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_rel}.
	 * @param ctx the parse tree
	 */
	void exitExp_rel(TclParser.Exp_relContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_add}.
	 * @param ctx the parse tree
	 */
	void enterExp_add(TclParser.Exp_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_add}.
	 * @param ctx the parse tree
	 */
	void exitExp_add(TclParser.Exp_addContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_mul}.
	 * @param ctx the parse tree
	 */
	void enterExp_mul(TclParser.Exp_mulContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_mul}.
	 * @param ctx the parse tree
	 */
	void exitExp_mul(TclParser.Exp_mulContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_pot}.
	 * @param ctx the parse tree
	 */
	void enterExp_pot(TclParser.Exp_potContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_pot}.
	 * @param ctx the parse tree
	 */
	void exitExp_pot(TclParser.Exp_potContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#exp_una}.
	 * @param ctx the parse tree
	 */
	void enterExp_una(TclParser.Exp_unaContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#exp_una}.
	 * @param ctx the parse tree
	 */
	void exitExp_una(TclParser.Exp_unaContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(TclParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(TclParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#op_ig}.
	 * @param ctx the parse tree
	 */
	void enterOp_ig(TclParser.Op_igContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#op_ig}.
	 * @param ctx the parse tree
	 */
	void exitOp_ig(TclParser.Op_igContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#op_rel}.
	 * @param ctx the parse tree
	 */
	void enterOp_rel(TclParser.Op_relContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#op_rel}.
	 * @param ctx the parse tree
	 */
	void exitOp_rel(TclParser.Op_relContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#op_add}.
	 * @param ctx the parse tree
	 */
	void enterOp_add(TclParser.Op_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#op_add}.
	 * @param ctx the parse tree
	 */
	void exitOp_add(TclParser.Op_addContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#op_mul}.
	 * @param ctx the parse tree
	 */
	void enterOp_mul(TclParser.Op_mulContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#op_mul}.
	 * @param ctx the parse tree
	 */
	void exitOp_mul(TclParser.Op_mulContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#op_una}.
	 * @param ctx the parse tree
	 */
	void enterOp_una(TclParser.Op_unaContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#op_una}.
	 * @param ctx the parse tree
	 */
	void exitOp_una(TclParser.Op_unaContext ctx);
}