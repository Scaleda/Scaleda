// Generated from /home/chiro/programs/scaleda/scaleda-kernel/src/main/antlr/bluespec/Bluespec.g4 by ANTLR 4.13.1
package top.scaleda.bluespec.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BluespecParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BluespecVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BluespecParser#top}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop(BluespecParser.TopContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(BluespecParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#identifier_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_type(BluespecParser.Identifier_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(BluespecParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#r_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_package(BluespecParser.R_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#non_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNon_package(BluespecParser.Non_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#exportDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportDecl(BluespecParser.ExportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#exportItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportItem(BluespecParser.ExportItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#importDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDecl(BluespecParser.ImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#importItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportItem(BluespecParser.ImportItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#packageStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageStmt(BluespecParser.PackageStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#packageIde}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageIde(BluespecParser.PackageIdeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(BluespecParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typePrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrimary(BluespecParser.TypePrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeIde}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeIde(BluespecParser.TypeIdeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeNat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNat(BluespecParser.TypeNatContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDecl(BluespecParser.InterfaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeDefType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDefType(BluespecParser.TypeDefTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeFormals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFormals(BluespecParser.TypeFormalsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeFormal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFormal(BluespecParser.TypeFormalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMemberDecl(BluespecParser.InterfaceMemberDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodProto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodProto(BluespecParser.MethodProtoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodProtoFormals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodProtoFormals(BluespecParser.MethodProtoFormalsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodProtoFormal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodProtoFormal(BluespecParser.MethodProtoFormalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#subinterfaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubinterfaceDecl(BluespecParser.SubinterfaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleDef(BluespecParser.ModuleDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleProto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleProto(BluespecParser.ModuleProtoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleFormalParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleFormalParams(BluespecParser.ModuleFormalParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleFormalParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleFormalParam(BluespecParser.ModuleFormalParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleFormalArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleFormalArgs(BluespecParser.ModuleFormalArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleStmt(BluespecParser.ModuleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleInst(BluespecParser.ModuleInstContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleApp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleApp(BluespecParser.ModuleAppContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleActualParamArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleActualParamArg(BluespecParser.ModuleActualParamArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleApp2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleApp2(BluespecParser.ModuleApp2Context ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleActualParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleActualParam(BluespecParser.ModuleActualParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleActualArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleActualArgs(BluespecParser.ModuleActualArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#moduleActualArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleActualArg(BluespecParser.ModuleActualArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDef(BluespecParser.MethodDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#implicitCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplicitCond(BluespecParser.ImplicitCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodFormals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodFormals(BluespecParser.MethodFormalsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodFormal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodFormal(BluespecParser.MethodFormalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#subinterfaceDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubinterfaceDef(BluespecParser.SubinterfaceDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceStmt(BluespecParser.InterfaceStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStmt(BluespecParser.ExpressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#r_rule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitR_rule(BluespecParser.R_ruleContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ruleCond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleCond(BluespecParser.RuleCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ruleBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleBody(BluespecParser.RuleBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDef(BluespecParser.TypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefSynonym}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefSynonym(BluespecParser.TypedefSynonymContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefEnum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefEnum(BluespecParser.TypedefEnumContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefEnumElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefEnumElements(BluespecParser.TypedefEnumElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefEnumElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefEnumElement(BluespecParser.TypedefEnumElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefStruct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefStruct(BluespecParser.TypedefStructContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedefTaggedUnion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefTaggedUnion(BluespecParser.TypedefTaggedUnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#structMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructMember(BluespecParser.StructMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#unionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionMember(BluespecParser.UnionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#subStruct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubStruct(BluespecParser.SubStructContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#subUnion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubUnion(BluespecParser.SubUnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(BluespecParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarInit(BluespecParser.VarInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#arrayDims}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDims(BluespecParser.ArrayDimsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssign(BluespecParser.VarAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#lValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLValue(BluespecParser.LValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#regWrite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegWrite(BluespecParser.RegWriteContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#arrayIndexes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndexes(BluespecParser.ArrayIndexesContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndStmt_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndStmt_functionBodyStmt(BluespecParser.BeginEndStmt_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndStmt_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndStmt_actionStmt(BluespecParser.BeginEndStmt_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndStmt_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndStmt_actionValueStmt(BluespecParser.BeginEndStmt_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndStmt_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndStmt_moduleStmt(BluespecParser.BeginEndStmt_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndStmt_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndStmt_expressionStmt(BluespecParser.BeginEndStmt_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#if_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_functionBodyStmt(BluespecParser.If_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#if_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_actionStmt(BluespecParser.If_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#if_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_actionValueStmt(BluespecParser.If_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#if_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_moduleStmt(BluespecParser.If_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#if_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_expressionStmt(BluespecParser.If_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#case_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_functionBodyStmt(BluespecParser.Case_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#case_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_actionStmt(BluespecParser.Case_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#case_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_actionValueStmt(BluespecParser.Case_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#case_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_moduleStmt(BluespecParser.Case_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#case_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expressionStmt(BluespecParser.Case_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem_functionBodyStmt(BluespecParser.CaseItem_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseItem_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem_actionStmt(BluespecParser.CaseItem_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseItem_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem_actionValueStmt(BluespecParser.CaseItem_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseItem_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem_moduleStmt(BluespecParser.CaseItem_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseItem_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseItem_expressionStmt(BluespecParser.CaseItem_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultItem_functionBodyStmt(BluespecParser.DefaultItem_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultItem_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultItem_actionStmt(BluespecParser.DefaultItem_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultItem_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultItem_actionValueStmt(BluespecParser.DefaultItem_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultItem_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultItem_moduleStmt(BluespecParser.DefaultItem_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultItem_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultItem_expressionStmt(BluespecParser.DefaultItem_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#while_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_functionBodyStmt(BluespecParser.While_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#while_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_actionStmt(BluespecParser.While_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#while_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_actionValueStmt(BluespecParser.While_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#while_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_moduleStmt(BluespecParser.While_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#while_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_expressionStmt(BluespecParser.While_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#for_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_functionBodyStmt(BluespecParser.For_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#for_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_actionStmt(BluespecParser.For_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#for_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_actionValueStmt(BluespecParser.For_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#for_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_moduleStmt(BluespecParser.For_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#for_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_expressionStmt(BluespecParser.For_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(BluespecParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forOldInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForOldInit(BluespecParser.ForOldInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#simpleVarAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleVarAssign(BluespecParser.SimpleVarAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forNewInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForNewInit(BluespecParser.ForNewInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#simpleVarDeclAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleVarDeclAssign(BluespecParser.SimpleVarDeclAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForTest(BluespecParser.ForTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forIncr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForIncr(BluespecParser.ForIncrContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varIncr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarIncr(BluespecParser.VarIncrContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDef(BluespecParser.FunctionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionProto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionProto(BluespecParser.FunctionProtoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionFormals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionFormals(BluespecParser.FunctionFormalsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionFormal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionFormal(BluespecParser.FunctionFormalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(BluespecParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBodyStmt(BluespecParser.FunctionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(BluespecParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(BluespecParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#exprPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPrimary(BluespecParser.ExprPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#condExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondExpr(BluespecParser.CondExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#condPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondPredicate(BluespecParser.CondPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#exprOrCondPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOrCondPattern(BluespecParser.ExprOrCondPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#operatorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorExpr(BluespecParser.OperatorExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#unop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnop(BluespecParser.UnopContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#binop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinop(BluespecParser.BinopContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#bitConcat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitConcat(BluespecParser.BitConcatContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#beginEndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginEndExpr(BluespecParser.BeginEndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#actionBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBlock(BluespecParser.ActionBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionStmt(BluespecParser.ActionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#actionValueBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionValueBlock(BluespecParser.ActionValueBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionValueStmt(BluespecParser.ActionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varDeclDo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclDo(BluespecParser.VarDeclDoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#varDo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDo(BluespecParser.VarDoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(BluespecParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(BluespecParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeAssertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAssertion(BluespecParser.TypeAssertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#structExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructExpr(BluespecParser.StructExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#memberBind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberBind(BluespecParser.MemberBindContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#taggedUnionExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTaggedUnionExpr(BluespecParser.TaggedUnionExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceExpr(BluespecParser.InterfaceExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ruleExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleExpr(BluespecParser.RuleExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ruleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleStmt(BluespecParser.RuleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPattern(BluespecParser.PatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#constantPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantPattern(BluespecParser.ConstantPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#taggedUnionPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTaggedUnionPattern(BluespecParser.TaggedUnionPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#structPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructPattern(BluespecParser.StructPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#tuplePattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTuplePattern(BluespecParser.TuplePatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#casePatItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCasePatItem_functionBodyStmt(BluespecParser.CasePatItem_functionBodyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#casePatItem_actionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCasePatItem_actionStmt(BluespecParser.CasePatItem_actionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#casePatItem_actionValueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCasePatItem_actionValueStmt(BluespecParser.CasePatItem_actionValueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#casePatItem_moduleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCasePatItem_moduleStmt(BluespecParser.CasePatItem_moduleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#casePatItem_expressionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCasePatItem_expressionStmt(BluespecParser.CasePatItem_expressionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpr(BluespecParser.CaseExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#caseExprItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExprItem(BluespecParser.CaseExprItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#systemTaskStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemTaskStmt(BluespecParser.SystemTaskStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#displayTaskName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisplayTaskName(BluespecParser.DisplayTaskNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#stringTaskName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringTaskName(BluespecParser.StringTaskNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#systemFunctionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemFunctionCall(BluespecParser.SystemFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#systemTaskCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemTaskCall(BluespecParser.SystemTaskCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#stringAVTaskName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringAVTaskName(BluespecParser.StringAVTaskNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#attributeInstances}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeInstances(BluespecParser.AttributeInstancesContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#attributeInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeInstance(BluespecParser.AttributeInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#attrSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrSpec(BluespecParser.AttrSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#attrName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrName(BluespecParser.AttrNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#provisos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProvisos(BluespecParser.ProvisosContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#proviso}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProviso(BluespecParser.ProvisoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeclassDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeclassDef(BluespecParser.TypeclassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeclassIde}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeclassIde(BluespecParser.TypeclassIdeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypelist(BluespecParser.TypelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedepends}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedepends(BluespecParser.TypedependsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typedepend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedepend(BluespecParser.TypedependContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#overloadedDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverloadedDef(BluespecParser.OverloadedDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#typeclassInstanceDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeclassInstanceDef(BluespecParser.TypeclassInstanceDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#derives}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerives(BluespecParser.DerivesContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#externModuleImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternModuleImport(BluespecParser.ExternModuleImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#importBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportBVIStmt(BluespecParser.ImportBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#enabled_sel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnabled_sel(BluespecParser.Enabled_selContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ready_sel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReady_sel(BluespecParser.Ready_selContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#clocked_by_sel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClocked_by_sel(BluespecParser.Clocked_by_selContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#reset_by_sel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReset_by_sel(BluespecParser.Reset_by_selContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#parameterBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterBVIStmt(BluespecParser.ParameterBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#methodBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBVIStmt(BluespecParser.MethodBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#portBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortBVIStmt(BluespecParser.PortBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#inputClockBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputClockBVIStmt(BluespecParser.InputClockBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#portsDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortsDef(BluespecParser.PortsDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#portId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortId(BluespecParser.PortIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultClockBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultClockBVIStmt(BluespecParser.DefaultClockBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#outputClockBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputClockBVIStmt(BluespecParser.OutputClockBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#inputResetBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputResetBVIStmt(BluespecParser.InputResetBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#clockId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClockId(BluespecParser.ClockIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#defaultResetBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultResetBVIStmt(BluespecParser.DefaultResetBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#outputResetBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputResetBVIStmt(BluespecParser.OutputResetBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ancestorBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAncestorBVIStmt(BluespecParser.AncestorBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#sameFamilyBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSameFamilyBVIStmt(BluespecParser.SameFamilyBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#scheduleBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheduleBVIStmt(BluespecParser.ScheduleBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#operatorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorId(BluespecParser.OperatorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#pathBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathBVIStmt(BluespecParser.PathBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBVIStmt(BluespecParser.InterfaceBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#interfaceBVIMembDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBVIMembDecl(BluespecParser.InterfaceBVIMembDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#inoutBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInoutBVIStmt(BluespecParser.InoutBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#resetId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetId(BluespecParser.ResetIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#noResetBVIStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoResetBVIStmt(BluespecParser.NoResetBVIStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#externCImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternCImport(BluespecParser.ExternCImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#cFuncArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCFuncArgs(BluespecParser.CFuncArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#cFuncArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCFuncArg(BluespecParser.CFuncArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#fsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsmStmt(BluespecParser.FsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#exprFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFsmStmt(BluespecParser.ExprFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#seqFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqFsmStmt(BluespecParser.SeqFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#parFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParFsmStmt(BluespecParser.ParFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#ifFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfFsmStmt(BluespecParser.IfFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#whileFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileFsmStmt(BluespecParser.WhileFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#forFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForFsmStmt(BluespecParser.ForFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#returnFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnFsmStmt(BluespecParser.ReturnFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#repeatFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatFsmStmt(BluespecParser.RepeatFsmStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BluespecParser#loopBodyFsmStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopBodyFsmStmt(BluespecParser.LoopBodyFsmStmtContext ctx);
}