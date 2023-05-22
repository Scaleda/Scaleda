// Generated from java-escape by ANTLR 4.11.1
package top.scaleda.tcl.parser;
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
	 * Enter a parse tree produced by {@link TclParser#identificator}.
	 * @param ctx the parse tree
	 */
	void enterIdentificator(TclParser.IdentificatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#identificator}.
	 * @param ctx the parse tree
	 */
	void exitIdentificator(TclParser.IdentificatorContext ctx);
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
	 * Enter a parse tree produced by {@link TclParser#source}.
	 * @param ctx the parse tree
	 */
	void enterSource(TclParser.SourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#source}.
	 * @param ctx the parse tree
	 */
	void exitSource(TclParser.SourceContext ctx);
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
	 * Enter a parse tree produced by {@link TclParser#func_internal}.
	 * @param ctx the parse tree
	 */
	void enterFunc_internal(TclParser.Func_internalContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_internal}.
	 * @param ctx the parse tree
	 */
	void exitFunc_internal(TclParser.Func_internalContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void enterFunc_arg(TclParser.Func_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_arg}.
	 * @param ctx the parse tree
	 */
	void exitFunc_arg(TclParser.Func_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#func_args}.
	 * @param ctx the parse tree
	 */
	void enterFunc_args(TclParser.Func_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_args}.
	 * @param ctx the parse tree
	 */
	void exitFunc_args(TclParser.Func_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#func_call}.
	 * @param ctx the parse tree
	 */
	void enterFunc_call(TclParser.Func_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_call}.
	 * @param ctx the parse tree
	 */
	void exitFunc_call(TclParser.Func_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#func_proc}.
	 * @param ctx the parse tree
	 */
	void enterFunc_proc(TclParser.Func_procContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_proc}.
	 * @param ctx the parse tree
	 */
	void exitFunc_proc(TclParser.Func_procContext ctx);
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
	 * Enter a parse tree produced by {@link TclParser#const}.
	 * @param ctx the parse tree
	 */
	void enterConst(TclParser.ConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#const}.
	 * @param ctx the parse tree
	 */
	void exitConst(TclParser.ConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#line_empty}.
	 * @param ctx the parse tree
	 */
	void enterLine_empty(TclParser.Line_emptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#line_empty}.
	 * @param ctx the parse tree
	 */
	void exitLine_empty(TclParser.Line_emptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(TclParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(TclParser.LineContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link TclParser#comm_str}.
	 * @param ctx the parse tree
	 */
	void enterComm_str(TclParser.Comm_strContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#comm_str}.
	 * @param ctx the parse tree
	 */
	void exitComm_str(TclParser.Comm_strContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#unknown_str}.
	 * @param ctx the parse tree
	 */
	void enterUnknown_str(TclParser.Unknown_strContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#unknown_str}.
	 * @param ctx the parse tree
	 */
	void exitUnknown_str(TclParser.Unknown_strContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#func_name}.
	 * @param ctx the parse tree
	 */
	void enterFunc_name(TclParser.Func_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#func_name}.
	 * @param ctx the parse tree
	 */
	void exitFunc_name(TclParser.Func_nameContext ctx);
}