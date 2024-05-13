// Generated from /home/chiro/programs/scaleda/scaleda-kernel/src/main/antlr/tcl/Tcl.g4 by ANTLR 4.13.1
package top.scaleda.tcl.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TclParser}.
 */
public interface TclListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TclParser#source_text}.
	 * @param ctx the parse tree
	 */
	void enterSource_text(TclParser.Source_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#source_text}.
	 * @param ctx the parse tree
	 */
	void exitSource_text(TclParser.Source_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#commandArg}.
	 * @param ctx the parse tree
	 */
	void enterCommandArg(TclParser.CommandArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#commandArg}.
	 * @param ctx the parse tree
	 */
	void exitCommandArg(TclParser.CommandArgContext ctx);
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
	 * Enter a parse tree produced by {@link TclParser#commandName}.
	 * @param ctx the parse tree
	 */
	void enterCommandName(TclParser.CommandNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#commandName}.
	 * @param ctx the parse tree
	 */
	void exitCommandName(TclParser.CommandNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#commandEmpty}.
	 * @param ctx the parse tree
	 */
	void enterCommandEmpty(TclParser.CommandEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#commandEmpty}.
	 * @param ctx the parse tree
	 */
	void exitCommandEmpty(TclParser.CommandEmptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(TclParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(TclParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#commandLine}.
	 * @param ctx the parse tree
	 */
	void enterCommandLine(TclParser.CommandLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#commandLine}.
	 * @param ctx the parse tree
	 */
	void exitCommandLine(TclParser.CommandLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#ifCond}.
	 * @param ctx the parse tree
	 */
	void enterIfCond(TclParser.IfCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#ifCond}.
	 * @param ctx the parse tree
	 */
	void exitIfCond(TclParser.IfCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#ifBody}.
	 * @param ctx the parse tree
	 */
	void enterIfBody(TclParser.IfBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#ifBody}.
	 * @param ctx the parse tree
	 */
	void exitIfBody(TclParser.IfBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elseBody}.
	 * @param ctx the parse tree
	 */
	void enterElseBody(TclParser.ElseBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elseBody}.
	 * @param ctx the parse tree
	 */
	void exitElseBody(TclParser.ElseBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#if}.
	 * @param ctx the parse tree
	 */
	void enterIf(TclParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#if}.
	 * @param ctx the parse tree
	 */
	void exitIf(TclParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#elsePart}.
	 * @param ctx the parse tree
	 */
	void enterElsePart(TclParser.ElsePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#elsePart}.
	 * @param ctx the parse tree
	 */
	void exitElsePart(TclParser.ElsePartContext ctx);
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
	 * Enter a parse tree produced by {@link TclParser#switchExpr}.
	 * @param ctx the parse tree
	 */
	void enterSwitchExpr(TclParser.SwitchExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switchExpr}.
	 * @param ctx the parse tree
	 */
	void exitSwitchExpr(TclParser.SwitchExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switchMatch}.
	 * @param ctx the parse tree
	 */
	void enterSwitchMatch(TclParser.SwitchMatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switchMatch}.
	 * @param ctx the parse tree
	 */
	void exitSwitchMatch(TclParser.SwitchMatchContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switchBody}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBody(TclParser.SwitchBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switchBody}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBody(TclParser.SwitchBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switchDefaultBody}.
	 * @param ctx the parse tree
	 */
	void enterSwitchDefaultBody(TclParser.SwitchDefaultBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switchDefaultBody}.
	 * @param ctx the parse tree
	 */
	void exitSwitchDefaultBody(TclParser.SwitchDefaultBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#switch}.
	 * @param ctx the parse tree
	 */
	void enterSwitch(TclParser.SwitchContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#switch}.
	 * @param ctx the parse tree
	 */
	void exitSwitch(TclParser.SwitchContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#whileBody}.
	 * @param ctx the parse tree
	 */
	void enterWhileBody(TclParser.WhileBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#whileBody}.
	 * @param ctx the parse tree
	 */
	void exitWhileBody(TclParser.WhileBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#whileCond}.
	 * @param ctx the parse tree
	 */
	void enterWhileCond(TclParser.WhileCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#whileCond}.
	 * @param ctx the parse tree
	 */
	void exitWhileCond(TclParser.WhileCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#while}.
	 * @param ctx the parse tree
	 */
	void enterWhile(TclParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#while}.
	 * @param ctx the parse tree
	 */
	void exitWhile(TclParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(TclParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(TclParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#forCond}.
	 * @param ctx the parse tree
	 */
	void enterForCond(TclParser.ForCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#forCond}.
	 * @param ctx the parse tree
	 */
	void exitForCond(TclParser.ForCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#forIncr}.
	 * @param ctx the parse tree
	 */
	void enterForIncr(TclParser.ForIncrContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#forIncr}.
	 * @param ctx the parse tree
	 */
	void exitForIncr(TclParser.ForIncrContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#forBody}.
	 * @param ctx the parse tree
	 */
	void enterForBody(TclParser.ForBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#forBody}.
	 * @param ctx the parse tree
	 */
	void exitForBody(TclParser.ForBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#for}.
	 * @param ctx the parse tree
	 */
	void enterFor(TclParser.ForContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#for}.
	 * @param ctx the parse tree
	 */
	void exitFor(TclParser.ForContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#foreachVar}.
	 * @param ctx the parse tree
	 */
	void enterForeachVar(TclParser.ForeachVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#foreachVar}.
	 * @param ctx the parse tree
	 */
	void exitForeachVar(TclParser.ForeachVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#foreachList}.
	 * @param ctx the parse tree
	 */
	void enterForeachList(TclParser.ForeachListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#foreachList}.
	 * @param ctx the parse tree
	 */
	void exitForeachList(TclParser.ForeachListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#foreachBody}.
	 * @param ctx the parse tree
	 */
	void enterForeachBody(TclParser.ForeachBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#foreachBody}.
	 * @param ctx the parse tree
	 */
	void exitForeachBody(TclParser.ForeachBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#foreach}.
	 * @param ctx the parse tree
	 */
	void enterForeach(TclParser.ForeachContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#foreach}.
	 * @param ctx the parse tree
	 */
	void exitForeach(TclParser.ForeachContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#procIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterProcIdentifier(TclParser.ProcIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#procIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitProcIdentifier(TclParser.ProcIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#procArg}.
	 * @param ctx the parse tree
	 */
	void enterProcArg(TclParser.ProcArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#procArg}.
	 * @param ctx the parse tree
	 */
	void exitProcArg(TclParser.ProcArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#procArgDef}.
	 * @param ctx the parse tree
	 */
	void enterProcArgDef(TclParser.ProcArgDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#procArgDef}.
	 * @param ctx the parse tree
	 */
	void exitProcArgDef(TclParser.ProcArgDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#procBody}.
	 * @param ctx the parse tree
	 */
	void enterProcBody(TclParser.ProcBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#procBody}.
	 * @param ctx the parse tree
	 */
	void exitProcBody(TclParser.ProcBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#proc}.
	 * @param ctx the parse tree
	 */
	void enterProc(TclParser.ProcContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#proc}.
	 * @param ctx the parse tree
	 */
	void exitProc(TclParser.ProcContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#packageIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterPackageIdentifier(TclParser.PackageIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#packageIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitPackageIdentifier(TclParser.PackageIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#packageVersion}.
	 * @param ctx the parse tree
	 */
	void enterPackageVersion(TclParser.PackageVersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#packageVersion}.
	 * @param ctx the parse tree
	 */
	void exitPackageVersion(TclParser.PackageVersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#packageRequire}.
	 * @param ctx the parse tree
	 */
	void enterPackageRequire(TclParser.PackageRequireContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#packageRequire}.
	 * @param ctx the parse tree
	 */
	void exitPackageRequire(TclParser.PackageRequireContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#packageProvide}.
	 * @param ctx the parse tree
	 */
	void enterPackageProvide(TclParser.PackageProvideContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#packageProvide}.
	 * @param ctx the parse tree
	 */
	void exitPackageProvide(TclParser.PackageProvideContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#package}.
	 * @param ctx the parse tree
	 */
	void enterPackage(TclParser.PackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#package}.
	 * @param ctx the parse tree
	 */
	void exitPackage(TclParser.PackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#namespaceIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceIdentifier(TclParser.NamespaceIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#namespaceIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceIdentifier(TclParser.NamespaceIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#namespace}.
	 * @param ctx the parse tree
	 */
	void enterNamespace(TclParser.NamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#namespace}.
	 * @param ctx the parse tree
	 */
	void exitNamespace(TclParser.NamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TclParser#regexp}.
	 * @param ctx the parse tree
	 */
	void enterRegexp(TclParser.RegexpContext ctx);
	/**
	 * Exit a parse tree produced by {@link TclParser#regexp}.
	 * @param ctx the parse tree
	 */
	void exitRegexp(TclParser.RegexpContext ctx);
}