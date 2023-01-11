// Generated from java-escape by ANTLR 4.11.1
package top.criwits.scaleda.tcl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TclParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TclVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio(TclParser.InicioContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#declaracion_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion_funcion(TclParser.Declaracion_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#args_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs_funcion(TclParser.Args_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#cuerpo_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCuerpo_funcion(TclParser.Cuerpo_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#if_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_funcion(TclParser.If_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseif_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_funcion(TclParser.Elseif_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#else_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_funcion(TclParser.Else_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switch_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_funcion(TclParser.Switch_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_funcion(TclParser.Case_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case2_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase2_funcion(TclParser.Case2_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#default_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_funcion(TclParser.Default_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#for_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_funcion(TclParser.For_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#while_funcion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_funcion(TclParser.While_funcionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#cuerpo_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCuerpo_loop_func(TclParser.Cuerpo_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#if_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_loop_func(TclParser.If_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseif_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_loop_func(TclParser.Elseif_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#else_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_loop_func(TclParser.Else_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switch_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_loop_func(TclParser.Switch_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_loop_func(TclParser.Case_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case2_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase2_loop_func(TclParser.Case2_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#default_loop_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_loop_func(TclParser.Default_loop_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#modulo_ppal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModulo_ppal(TclParser.Modulo_ppalContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_if(TclParser.R_ifContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif(TclParser.ElseifContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_else}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_else(TclParser.R_elseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_switch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_switch(TclParser.R_switchContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_case}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_case(TclParser.R_caseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase2(TclParser.Case2Context ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_default(TclParser.R_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#cuerpo_inst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCuerpo_inst(TclParser.Cuerpo_instContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_for(TclParser.R_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_while}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_while(TclParser.R_whileContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#cuerpo_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCuerpo_loop(TclParser.Cuerpo_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#if_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_loop(TclParser.If_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseif_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_loop(TclParser.Elseif_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#else_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_loop(TclParser.Else_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switch_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_loop(TclParser.Switch_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_loop(TclParser.Case_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#case2_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase2_loop(TclParser.Case2_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#default_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_loop(TclParser.Default_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#puts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPuts(TclParser.PutsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#gets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGets(TclParser.GetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#declaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion(TclParser.DeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#agrup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAgrup(TclParser.AgrupContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#aux_agrup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAux_agrup(TclParser.Aux_agrupContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#aux_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAux_array(TclParser.Aux_arrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#param_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_func(TclParser.Param_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#aux_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAux_param(TclParser.Aux_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(TclParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#indice}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndice(TclParser.IndiceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#val_indice}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVal_indice(TclParser.Val_indiceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#valor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValor(TclParser.ValorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#incremento}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncremento(TclParser.IncrementoContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_break}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_break(TclParser.R_breakContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_continue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_continue(TclParser.R_continueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#r_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_return(TclParser.R_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#value_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_return(TclParser.Value_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(TclParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#asig_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsig_for(TclParser.Asig_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_if(TclParser.Inicio_ifContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_elseif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_elseif(TclParser.Inicio_elseifContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_else}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_else(TclParser.Inicio_elseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_switch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_switch(TclParser.Inicio_switchContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_case}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_case(TclParser.Inicio_caseContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_default(TclParser.Inicio_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_for(TclParser.Inicio_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#dec_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDec_for(TclParser.Dec_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#inicio_while}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicio_while(TclParser.Inicio_whileContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpresion(TclParser.ExpresionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_or(TclParser.Exp_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_and(TclParser.Exp_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_ig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_ig(TclParser.Exp_igContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_rel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_rel(TclParser.Exp_relContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_add(TclParser.Exp_addContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_mul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_mul(TclParser.Exp_mulContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_pot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_pot(TclParser.Exp_potContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#exp_una}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_una(TclParser.Exp_unaContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(TclParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#op_ig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_ig(TclParser.Op_igContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#op_rel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_rel(TclParser.Op_relContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#op_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_add(TclParser.Op_addContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#op_mul}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_mul(TclParser.Op_mulContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#op_una}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp_una(TclParser.Op_unaContext ctx);
}