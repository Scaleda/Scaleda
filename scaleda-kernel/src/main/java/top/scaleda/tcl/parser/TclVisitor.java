// Generated from /home/chiro/programs/scaleda/scaleda-kernel/src/main/antlr/tcl/Tcl.g4 by ANTLR 4.13.1
package top.scaleda.tcl.parser;
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
	 * Visit a parse tree produced by {@link TclParser#operatorAB}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorAB(TclParser.OperatorABContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#operatorA}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorA(TclParser.OperatorAContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#matchPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchPattern(TclParser.MatchPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#source_text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource_text(TclParser.Source_textContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#commandArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandArg(TclParser.CommandArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(TclParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#commandName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandName(TclParser.CommandNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#commandEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandEmpty(TclParser.CommandEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(TclParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#commandLine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandLine(TclParser.CommandLineContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#ifCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfCond(TclParser.IfCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#ifBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBody(TclParser.IfBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBody(TclParser.ElseBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(TclParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elsePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElsePart(TclParser.ElsePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#elseif}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif(TclParser.ElseifContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switchExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchExpr(TclParser.SwitchExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switchMatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchMatch(TclParser.SwitchMatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switchBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBody(TclParser.SwitchBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switchDefaultBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchDefaultBody(TclParser.SwitchDefaultBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#switch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch(TclParser.SwitchContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#whileBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileBody(TclParser.WhileBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#whileCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileCond(TclParser.WhileCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#while}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(TclParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(TclParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#forCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForCond(TclParser.ForCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#forIncr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForIncr(TclParser.ForIncrContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#forBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForBody(TclParser.ForBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor(TclParser.ForContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#foreachVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachVar(TclParser.ForeachVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#foreachList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachList(TclParser.ForeachListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#foreachBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachBody(TclParser.ForeachBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#foreach}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeach(TclParser.ForeachContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#procIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcIdentifier(TclParser.ProcIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#procArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcArg(TclParser.ProcArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#procArgDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcArgDef(TclParser.ProcArgDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#procBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcBody(TclParser.ProcBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#proc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProc(TclParser.ProcContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#packageIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageIdentifier(TclParser.PackageIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#packageVersion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageVersion(TclParser.PackageVersionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#packageRequire}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageRequire(TclParser.PackageRequireContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#packageProvide}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageProvide(TclParser.PackageProvideContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage(TclParser.PackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#namespaceIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceIdentifier(TclParser.NamespaceIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#namespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespace(TclParser.NamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TclParser#regexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexp(TclParser.RegexpContext ctx);
}