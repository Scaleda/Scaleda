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
	 * Visit a parse tree produced by {@link TclParser#identificator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentificator(TclParser.IdentificatorContext ctx);
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
	 * Visit a parse tree produced by {@link TclParser#source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource(TclParser.SourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#declaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion(TclParser.DeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#func_internal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_internal(TclParser.Func_internalContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#func_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_arg(TclParser.Func_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#func_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_args(TclParser.Func_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#func_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_call(TclParser.Func_callContext ctx);
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
	 * Visit a parse tree produced by {@link TclParser#const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst(TclParser.ConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#line_empty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine_empty(TclParser.Line_emptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(TclParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(TclParser.ExprContext ctx);
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
	/**
	 * Visit a parse tree produced by {@link TclParser#comm_str}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComm_str(TclParser.Comm_strContext ctx);
}