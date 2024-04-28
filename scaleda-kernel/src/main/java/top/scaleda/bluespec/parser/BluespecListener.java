// Generated from /home/chiro/programs/scaleda/scaleda-kernel/src/main/antlr/bluespec/Bluespec.g4 by ANTLR 4.13.1
package top.scaleda.bluespec.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BluespecParser}.
 */
public interface BluespecListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BluespecParser#top}.
	 * @param ctx the parse tree
	 */
	void enterTop(BluespecParser.TopContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#top}.
	 * @param ctx the parse tree
	 */
	void exitTop(BluespecParser.TopContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(BluespecParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(BluespecParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#identifier_type}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_type(BluespecParser.Identifier_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#identifier_type}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_type(BluespecParser.Identifier_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(BluespecParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(BluespecParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#r_package}.
	 * @param ctx the parse tree
	 */
	void enterR_package(BluespecParser.R_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#r_package}.
	 * @param ctx the parse tree
	 */
	void exitR_package(BluespecParser.R_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#non_package}.
	 * @param ctx the parse tree
	 */
	void enterNon_package(BluespecParser.Non_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#non_package}.
	 * @param ctx the parse tree
	 */
	void exitNon_package(BluespecParser.Non_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#exportDecl}.
	 * @param ctx the parse tree
	 */
	void enterExportDecl(BluespecParser.ExportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#exportDecl}.
	 * @param ctx the parse tree
	 */
	void exitExportDecl(BluespecParser.ExportDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#exportItem}.
	 * @param ctx the parse tree
	 */
	void enterExportItem(BluespecParser.ExportItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#exportItem}.
	 * @param ctx the parse tree
	 */
	void exitExportItem(BluespecParser.ExportItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void enterImportDecl(BluespecParser.ImportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void exitImportDecl(BluespecParser.ImportDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#importItem}.
	 * @param ctx the parse tree
	 */
	void enterImportItem(BluespecParser.ImportItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#importItem}.
	 * @param ctx the parse tree
	 */
	void exitImportItem(BluespecParser.ImportItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#packageStmt}.
	 * @param ctx the parse tree
	 */
	void enterPackageStmt(BluespecParser.PackageStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#packageStmt}.
	 * @param ctx the parse tree
	 */
	void exitPackageStmt(BluespecParser.PackageStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#packageIde}.
	 * @param ctx the parse tree
	 */
	void enterPackageIde(BluespecParser.PackageIdeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#packageIde}.
	 * @param ctx the parse tree
	 */
	void exitPackageIde(BluespecParser.PackageIdeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(BluespecParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(BluespecParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typePrimary}.
	 * @param ctx the parse tree
	 */
	void enterTypePrimary(BluespecParser.TypePrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typePrimary}.
	 * @param ctx the parse tree
	 */
	void exitTypePrimary(BluespecParser.TypePrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeIde}.
	 * @param ctx the parse tree
	 */
	void enterTypeIde(BluespecParser.TypeIdeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeIde}.
	 * @param ctx the parse tree
	 */
	void exitTypeIde(BluespecParser.TypeIdeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeNat}.
	 * @param ctx the parse tree
	 */
	void enterTypeNat(BluespecParser.TypeNatContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeNat}.
	 * @param ctx the parse tree
	 */
	void exitTypeNat(BluespecParser.TypeNatContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDecl(BluespecParser.InterfaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDecl(BluespecParser.InterfaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeDefType}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefType(BluespecParser.TypeDefTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeDefType}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefType(BluespecParser.TypeDefTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeFormals}.
	 * @param ctx the parse tree
	 */
	void enterTypeFormals(BluespecParser.TypeFormalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeFormals}.
	 * @param ctx the parse tree
	 */
	void exitTypeFormals(BluespecParser.TypeFormalsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeFormal}.
	 * @param ctx the parse tree
	 */
	void enterTypeFormal(BluespecParser.TypeFormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeFormal}.
	 * @param ctx the parse tree
	 */
	void exitTypeFormal(BluespecParser.TypeFormalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDecl(BluespecParser.InterfaceMemberDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDecl(BluespecParser.InterfaceMemberDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodProto}.
	 * @param ctx the parse tree
	 */
	void enterMethodProto(BluespecParser.MethodProtoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodProto}.
	 * @param ctx the parse tree
	 */
	void exitMethodProto(BluespecParser.MethodProtoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodProtoFormals}.
	 * @param ctx the parse tree
	 */
	void enterMethodProtoFormals(BluespecParser.MethodProtoFormalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodProtoFormals}.
	 * @param ctx the parse tree
	 */
	void exitMethodProtoFormals(BluespecParser.MethodProtoFormalsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodProtoFormal}.
	 * @param ctx the parse tree
	 */
	void enterMethodProtoFormal(BluespecParser.MethodProtoFormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodProtoFormal}.
	 * @param ctx the parse tree
	 */
	void exitMethodProtoFormal(BluespecParser.MethodProtoFormalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#subinterfaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterSubinterfaceDecl(BluespecParser.SubinterfaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#subinterfaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitSubinterfaceDecl(BluespecParser.SubinterfaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleDef}.
	 * @param ctx the parse tree
	 */
	void enterModuleDef(BluespecParser.ModuleDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleDef}.
	 * @param ctx the parse tree
	 */
	void exitModuleDef(BluespecParser.ModuleDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterModuleIdentifier(BluespecParser.ModuleIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitModuleIdentifier(BluespecParser.ModuleIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleProto}.
	 * @param ctx the parse tree
	 */
	void enterModuleProto(BluespecParser.ModuleProtoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleProto}.
	 * @param ctx the parse tree
	 */
	void exitModuleProto(BluespecParser.ModuleProtoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleFormalParams}.
	 * @param ctx the parse tree
	 */
	void enterModuleFormalParams(BluespecParser.ModuleFormalParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleFormalParams}.
	 * @param ctx the parse tree
	 */
	void exitModuleFormalParams(BluespecParser.ModuleFormalParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleFormalParam}.
	 * @param ctx the parse tree
	 */
	void enterModuleFormalParam(BluespecParser.ModuleFormalParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleFormalParam}.
	 * @param ctx the parse tree
	 */
	void exitModuleFormalParam(BluespecParser.ModuleFormalParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleFormalArgs}.
	 * @param ctx the parse tree
	 */
	void enterModuleFormalArgs(BluespecParser.ModuleFormalArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleFormalArgs}.
	 * @param ctx the parse tree
	 */
	void exitModuleFormalArgs(BluespecParser.ModuleFormalArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterModuleStmt(BluespecParser.ModuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitModuleStmt(BluespecParser.ModuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	void enterModuleInst(BluespecParser.ModuleInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	void exitModuleInst(BluespecParser.ModuleInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleApp}.
	 * @param ctx the parse tree
	 */
	void enterModuleApp(BluespecParser.ModuleAppContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleApp}.
	 * @param ctx the parse tree
	 */
	void exitModuleApp(BluespecParser.ModuleAppContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleActualParamArg}.
	 * @param ctx the parse tree
	 */
	void enterModuleActualParamArg(BluespecParser.ModuleActualParamArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleActualParamArg}.
	 * @param ctx the parse tree
	 */
	void exitModuleActualParamArg(BluespecParser.ModuleActualParamArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleApp2}.
	 * @param ctx the parse tree
	 */
	void enterModuleApp2(BluespecParser.ModuleApp2Context ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleApp2}.
	 * @param ctx the parse tree
	 */
	void exitModuleApp2(BluespecParser.ModuleApp2Context ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleActualParam}.
	 * @param ctx the parse tree
	 */
	void enterModuleActualParam(BluespecParser.ModuleActualParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleActualParam}.
	 * @param ctx the parse tree
	 */
	void exitModuleActualParam(BluespecParser.ModuleActualParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleActualArgs}.
	 * @param ctx the parse tree
	 */
	void enterModuleActualArgs(BluespecParser.ModuleActualArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleActualArgs}.
	 * @param ctx the parse tree
	 */
	void exitModuleActualArgs(BluespecParser.ModuleActualArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#moduleActualArg}.
	 * @param ctx the parse tree
	 */
	void enterModuleActualArg(BluespecParser.ModuleActualArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#moduleActualArg}.
	 * @param ctx the parse tree
	 */
	void exitModuleActualArg(BluespecParser.ModuleActualArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodDef}.
	 * @param ctx the parse tree
	 */
	void enterMethodDef(BluespecParser.MethodDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodDef}.
	 * @param ctx the parse tree
	 */
	void exitMethodDef(BluespecParser.MethodDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#implicitCond}.
	 * @param ctx the parse tree
	 */
	void enterImplicitCond(BluespecParser.ImplicitCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#implicitCond}.
	 * @param ctx the parse tree
	 */
	void exitImplicitCond(BluespecParser.ImplicitCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodFormals}.
	 * @param ctx the parse tree
	 */
	void enterMethodFormals(BluespecParser.MethodFormalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodFormals}.
	 * @param ctx the parse tree
	 */
	void exitMethodFormals(BluespecParser.MethodFormalsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodFormal}.
	 * @param ctx the parse tree
	 */
	void enterMethodFormal(BluespecParser.MethodFormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodFormal}.
	 * @param ctx the parse tree
	 */
	void exitMethodFormal(BluespecParser.MethodFormalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#subinterfaceDef}.
	 * @param ctx the parse tree
	 */
	void enterSubinterfaceDef(BluespecParser.SubinterfaceDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#subinterfaceDef}.
	 * @param ctx the parse tree
	 */
	void exitSubinterfaceDef(BluespecParser.SubinterfaceDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceStmt}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceStmt(BluespecParser.InterfaceStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceStmt}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceStmt(BluespecParser.InterfaceStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStmt(BluespecParser.ExpressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStmt(BluespecParser.ExpressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#r_rule}.
	 * @param ctx the parse tree
	 */
	void enterR_rule(BluespecParser.R_ruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#r_rule}.
	 * @param ctx the parse tree
	 */
	void exitR_rule(BluespecParser.R_ruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ruleCond}.
	 * @param ctx the parse tree
	 */
	void enterRuleCond(BluespecParser.RuleCondContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ruleCond}.
	 * @param ctx the parse tree
	 */
	void exitRuleCond(BluespecParser.RuleCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ruleBody}.
	 * @param ctx the parse tree
	 */
	void enterRuleBody(BluespecParser.RuleBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ruleBody}.
	 * @param ctx the parse tree
	 */
	void exitRuleBody(BluespecParser.RuleBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeDef(BluespecParser.TypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeDef(BluespecParser.TypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefSynonym}.
	 * @param ctx the parse tree
	 */
	void enterTypedefSynonym(BluespecParser.TypedefSynonymContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefSynonym}.
	 * @param ctx the parse tree
	 */
	void exitTypedefSynonym(BluespecParser.TypedefSynonymContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefEnum}.
	 * @param ctx the parse tree
	 */
	void enterTypedefEnum(BluespecParser.TypedefEnumContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefEnum}.
	 * @param ctx the parse tree
	 */
	void exitTypedefEnum(BluespecParser.TypedefEnumContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefEnumElements}.
	 * @param ctx the parse tree
	 */
	void enterTypedefEnumElements(BluespecParser.TypedefEnumElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefEnumElements}.
	 * @param ctx the parse tree
	 */
	void exitTypedefEnumElements(BluespecParser.TypedefEnumElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefEnumElement}.
	 * @param ctx the parse tree
	 */
	void enterTypedefEnumElement(BluespecParser.TypedefEnumElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefEnumElement}.
	 * @param ctx the parse tree
	 */
	void exitTypedefEnumElement(BluespecParser.TypedefEnumElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefStruct}.
	 * @param ctx the parse tree
	 */
	void enterTypedefStruct(BluespecParser.TypedefStructContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefStruct}.
	 * @param ctx the parse tree
	 */
	void exitTypedefStruct(BluespecParser.TypedefStructContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedefTaggedUnion}.
	 * @param ctx the parse tree
	 */
	void enterTypedefTaggedUnion(BluespecParser.TypedefTaggedUnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedefTaggedUnion}.
	 * @param ctx the parse tree
	 */
	void exitTypedefTaggedUnion(BluespecParser.TypedefTaggedUnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#structMember}.
	 * @param ctx the parse tree
	 */
	void enterStructMember(BluespecParser.StructMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#structMember}.
	 * @param ctx the parse tree
	 */
	void exitStructMember(BluespecParser.StructMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void enterUnionMember(BluespecParser.UnionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void exitUnionMember(BluespecParser.UnionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#subStruct}.
	 * @param ctx the parse tree
	 */
	void enterSubStruct(BluespecParser.SubStructContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#subStruct}.
	 * @param ctx the parse tree
	 */
	void exitSubStruct(BluespecParser.SubStructContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#subUnion}.
	 * @param ctx the parse tree
	 */
	void enterSubUnion(BluespecParser.SubUnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#subUnion}.
	 * @param ctx the parse tree
	 */
	void exitSubUnion(BluespecParser.SubUnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(BluespecParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(BluespecParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varInit}.
	 * @param ctx the parse tree
	 */
	void enterVarInit(BluespecParser.VarInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varInit}.
	 * @param ctx the parse tree
	 */
	void exitVarInit(BluespecParser.VarInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#arrayDims}.
	 * @param ctx the parse tree
	 */
	void enterArrayDims(BluespecParser.ArrayDimsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#arrayDims}.
	 * @param ctx the parse tree
	 */
	void exitArrayDims(BluespecParser.ArrayDimsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void enterVarAssign(BluespecParser.VarAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void exitVarAssign(BluespecParser.VarAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#lValue}.
	 * @param ctx the parse tree
	 */
	void enterLValue(BluespecParser.LValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#lValue}.
	 * @param ctx the parse tree
	 */
	void exitLValue(BluespecParser.LValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#regWrite}.
	 * @param ctx the parse tree
	 */
	void enterRegWrite(BluespecParser.RegWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#regWrite}.
	 * @param ctx the parse tree
	 */
	void exitRegWrite(BluespecParser.RegWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#arrayIndexes}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndexes(BluespecParser.ArrayIndexesContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#arrayIndexes}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndexes(BluespecParser.ArrayIndexesContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndStmt_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndStmt_functionBodyStmt(BluespecParser.BeginEndStmt_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndStmt_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndStmt_functionBodyStmt(BluespecParser.BeginEndStmt_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndStmt_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndStmt_actionStmt(BluespecParser.BeginEndStmt_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndStmt_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndStmt_actionStmt(BluespecParser.BeginEndStmt_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndStmt_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndStmt_actionValueStmt(BluespecParser.BeginEndStmt_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndStmt_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndStmt_actionValueStmt(BluespecParser.BeginEndStmt_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndStmt_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndStmt_moduleStmt(BluespecParser.BeginEndStmt_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndStmt_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndStmt_moduleStmt(BluespecParser.BeginEndStmt_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndStmt_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndStmt_expressionStmt(BluespecParser.BeginEndStmt_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndStmt_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndStmt_expressionStmt(BluespecParser.BeginEndStmt_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#if_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_functionBodyStmt(BluespecParser.If_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#if_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_functionBodyStmt(BluespecParser.If_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#if_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_actionStmt(BluespecParser.If_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#if_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_actionStmt(BluespecParser.If_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#if_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_actionValueStmt(BluespecParser.If_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#if_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_actionValueStmt(BluespecParser.If_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#if_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_moduleStmt(BluespecParser.If_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#if_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_moduleStmt(BluespecParser.If_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#if_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_expressionStmt(BluespecParser.If_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#if_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_expressionStmt(BluespecParser.If_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#case_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_functionBodyStmt(BluespecParser.Case_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#case_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_functionBodyStmt(BluespecParser.Case_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#case_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_actionStmt(BluespecParser.Case_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#case_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_actionStmt(BluespecParser.Case_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#case_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_actionValueStmt(BluespecParser.Case_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#case_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_actionValueStmt(BluespecParser.Case_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#case_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_moduleStmt(BluespecParser.Case_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#case_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_moduleStmt(BluespecParser.Case_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#case_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCase_expressionStmt(BluespecParser.Case_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#case_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCase_expressionStmt(BluespecParser.Case_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem_functionBodyStmt(BluespecParser.CaseItem_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem_functionBodyStmt(BluespecParser.CaseItem_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem_actionStmt(BluespecParser.CaseItem_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem_actionStmt(BluespecParser.CaseItem_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem_actionValueStmt(BluespecParser.CaseItem_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem_actionValueStmt(BluespecParser.CaseItem_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem_moduleStmt(BluespecParser.CaseItem_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem_moduleStmt(BluespecParser.CaseItem_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCaseItem_expressionStmt(BluespecParser.CaseItem_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCaseItem_expressionStmt(BluespecParser.CaseItem_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultItem_functionBodyStmt(BluespecParser.DefaultItem_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultItem_functionBodyStmt(BluespecParser.DefaultItem_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultItem_actionStmt(BluespecParser.DefaultItem_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultItem_actionStmt(BluespecParser.DefaultItem_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultItem_actionValueStmt(BluespecParser.DefaultItem_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultItem_actionValueStmt(BluespecParser.DefaultItem_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultItem_moduleStmt(BluespecParser.DefaultItem_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultItem_moduleStmt(BluespecParser.DefaultItem_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultItem_expressionStmt(BluespecParser.DefaultItem_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultItem_expressionStmt(BluespecParser.DefaultItem_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#while_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_functionBodyStmt(BluespecParser.While_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#while_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_functionBodyStmt(BluespecParser.While_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#while_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_actionStmt(BluespecParser.While_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#while_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_actionStmt(BluespecParser.While_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#while_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_actionValueStmt(BluespecParser.While_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#while_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_actionValueStmt(BluespecParser.While_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#while_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_moduleStmt(BluespecParser.While_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#while_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_moduleStmt(BluespecParser.While_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#while_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_expressionStmt(BluespecParser.While_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#while_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_expressionStmt(BluespecParser.While_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#for_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_functionBodyStmt(BluespecParser.For_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#for_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_functionBodyStmt(BluespecParser.For_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#for_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_actionStmt(BluespecParser.For_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#for_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_actionStmt(BluespecParser.For_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#for_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_actionValueStmt(BluespecParser.For_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#for_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_actionValueStmt(BluespecParser.For_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#for_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_moduleStmt(BluespecParser.For_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#for_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_moduleStmt(BluespecParser.For_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#for_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_expressionStmt(BluespecParser.For_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#for_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_expressionStmt(BluespecParser.For_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(BluespecParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(BluespecParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forOldInit}.
	 * @param ctx the parse tree
	 */
	void enterForOldInit(BluespecParser.ForOldInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forOldInit}.
	 * @param ctx the parse tree
	 */
	void exitForOldInit(BluespecParser.ForOldInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#simpleVarAssign}.
	 * @param ctx the parse tree
	 */
	void enterSimpleVarAssign(BluespecParser.SimpleVarAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#simpleVarAssign}.
	 * @param ctx the parse tree
	 */
	void exitSimpleVarAssign(BluespecParser.SimpleVarAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forNewInit}.
	 * @param ctx the parse tree
	 */
	void enterForNewInit(BluespecParser.ForNewInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forNewInit}.
	 * @param ctx the parse tree
	 */
	void exitForNewInit(BluespecParser.ForNewInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#simpleVarDeclAssign}.
	 * @param ctx the parse tree
	 */
	void enterSimpleVarDeclAssign(BluespecParser.SimpleVarDeclAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#simpleVarDeclAssign}.
	 * @param ctx the parse tree
	 */
	void exitSimpleVarDeclAssign(BluespecParser.SimpleVarDeclAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forTest}.
	 * @param ctx the parse tree
	 */
	void enterForTest(BluespecParser.ForTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forTest}.
	 * @param ctx the parse tree
	 */
	void exitForTest(BluespecParser.ForTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forIncr}.
	 * @param ctx the parse tree
	 */
	void enterForIncr(BluespecParser.ForIncrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forIncr}.
	 * @param ctx the parse tree
	 */
	void exitForIncr(BluespecParser.ForIncrContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varIncr}.
	 * @param ctx the parse tree
	 */
	void enterVarIncr(BluespecParser.VarIncrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varIncr}.
	 * @param ctx the parse tree
	 */
	void exitVarIncr(BluespecParser.VarIncrContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDef(BluespecParser.FunctionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDef(BluespecParser.FunctionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionProto}.
	 * @param ctx the parse tree
	 */
	void enterFunctionProto(BluespecParser.FunctionProtoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionProto}.
	 * @param ctx the parse tree
	 */
	void exitFunctionProto(BluespecParser.FunctionProtoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionFormals}.
	 * @param ctx the parse tree
	 */
	void enterFunctionFormals(BluespecParser.FunctionFormalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionFormals}.
	 * @param ctx the parse tree
	 */
	void exitFunctionFormals(BluespecParser.FunctionFormalsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionFormal}.
	 * @param ctx the parse tree
	 */
	void enterFunctionFormal(BluespecParser.FunctionFormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionFormal}.
	 * @param ctx the parse tree
	 */
	void exitFunctionFormal(BluespecParser.FunctionFormalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(BluespecParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(BluespecParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBodyStmt(BluespecParser.FunctionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBodyStmt(BluespecParser.FunctionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(BluespecParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(BluespecParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(BluespecParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(BluespecParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#exprPrimary}.
	 * @param ctx the parse tree
	 */
	void enterExprPrimary(BluespecParser.ExprPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#exprPrimary}.
	 * @param ctx the parse tree
	 */
	void exitExprPrimary(BluespecParser.ExprPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#condExpr}.
	 * @param ctx the parse tree
	 */
	void enterCondExpr(BluespecParser.CondExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#condExpr}.
	 * @param ctx the parse tree
	 */
	void exitCondExpr(BluespecParser.CondExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#condPredicate}.
	 * @param ctx the parse tree
	 */
	void enterCondPredicate(BluespecParser.CondPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#condPredicate}.
	 * @param ctx the parse tree
	 */
	void exitCondPredicate(BluespecParser.CondPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#exprOrCondPattern}.
	 * @param ctx the parse tree
	 */
	void enterExprOrCondPattern(BluespecParser.ExprOrCondPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#exprOrCondPattern}.
	 * @param ctx the parse tree
	 */
	void exitExprOrCondPattern(BluespecParser.ExprOrCondPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#operatorExpr}.
	 * @param ctx the parse tree
	 */
	void enterOperatorExpr(BluespecParser.OperatorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#operatorExpr}.
	 * @param ctx the parse tree
	 */
	void exitOperatorExpr(BluespecParser.OperatorExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#unop}.
	 * @param ctx the parse tree
	 */
	void enterUnop(BluespecParser.UnopContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#unop}.
	 * @param ctx the parse tree
	 */
	void exitUnop(BluespecParser.UnopContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#binop}.
	 * @param ctx the parse tree
	 */
	void enterBinop(BluespecParser.BinopContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#binop}.
	 * @param ctx the parse tree
	 */
	void exitBinop(BluespecParser.BinopContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#bitConcat}.
	 * @param ctx the parse tree
	 */
	void enterBitConcat(BluespecParser.BitConcatContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#bitConcat}.
	 * @param ctx the parse tree
	 */
	void exitBitConcat(BluespecParser.BitConcatContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#beginEndExpr}.
	 * @param ctx the parse tree
	 */
	void enterBeginEndExpr(BluespecParser.BeginEndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#beginEndExpr}.
	 * @param ctx the parse tree
	 */
	void exitBeginEndExpr(BluespecParser.BeginEndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#actionBlock}.
	 * @param ctx the parse tree
	 */
	void enterActionBlock(BluespecParser.ActionBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#actionBlock}.
	 * @param ctx the parse tree
	 */
	void exitActionBlock(BluespecParser.ActionBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterActionStmt(BluespecParser.ActionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitActionStmt(BluespecParser.ActionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#actionValueBlock}.
	 * @param ctx the parse tree
	 */
	void enterActionValueBlock(BluespecParser.ActionValueBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#actionValueBlock}.
	 * @param ctx the parse tree
	 */
	void exitActionValueBlock(BluespecParser.ActionValueBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterActionValueStmt(BluespecParser.ActionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitActionValueStmt(BluespecParser.ActionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varDeclDo}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclDo(BluespecParser.VarDeclDoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varDeclDo}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclDo(BluespecParser.VarDeclDoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#varDo}.
	 * @param ctx the parse tree
	 */
	void enterVarDo(BluespecParser.VarDoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#varDo}.
	 * @param ctx the parse tree
	 */
	void exitVarDo(BluespecParser.VarDoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(BluespecParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(BluespecParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(BluespecParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(BluespecParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeAssertion}.
	 * @param ctx the parse tree
	 */
	void enterTypeAssertion(BluespecParser.TypeAssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeAssertion}.
	 * @param ctx the parse tree
	 */
	void exitTypeAssertion(BluespecParser.TypeAssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#structExpr}.
	 * @param ctx the parse tree
	 */
	void enterStructExpr(BluespecParser.StructExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#structExpr}.
	 * @param ctx the parse tree
	 */
	void exitStructExpr(BluespecParser.StructExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#memberBind}.
	 * @param ctx the parse tree
	 */
	void enterMemberBind(BluespecParser.MemberBindContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#memberBind}.
	 * @param ctx the parse tree
	 */
	void exitMemberBind(BluespecParser.MemberBindContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#taggedUnionExpr}.
	 * @param ctx the parse tree
	 */
	void enterTaggedUnionExpr(BluespecParser.TaggedUnionExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#taggedUnionExpr}.
	 * @param ctx the parse tree
	 */
	void exitTaggedUnionExpr(BluespecParser.TaggedUnionExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceExpr}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceExpr(BluespecParser.InterfaceExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceExpr}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceExpr(BluespecParser.InterfaceExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ruleExpr}.
	 * @param ctx the parse tree
	 */
	void enterRuleExpr(BluespecParser.RuleExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ruleExpr}.
	 * @param ctx the parse tree
	 */
	void exitRuleExpr(BluespecParser.RuleExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ruleStmt}.
	 * @param ctx the parse tree
	 */
	void enterRuleStmt(BluespecParser.RuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ruleStmt}.
	 * @param ctx the parse tree
	 */
	void exitRuleStmt(BluespecParser.RuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(BluespecParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(BluespecParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#constantPattern}.
	 * @param ctx the parse tree
	 */
	void enterConstantPattern(BluespecParser.ConstantPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#constantPattern}.
	 * @param ctx the parse tree
	 */
	void exitConstantPattern(BluespecParser.ConstantPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#taggedUnionPattern}.
	 * @param ctx the parse tree
	 */
	void enterTaggedUnionPattern(BluespecParser.TaggedUnionPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#taggedUnionPattern}.
	 * @param ctx the parse tree
	 */
	void exitTaggedUnionPattern(BluespecParser.TaggedUnionPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#structPattern}.
	 * @param ctx the parse tree
	 */
	void enterStructPattern(BluespecParser.StructPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#structPattern}.
	 * @param ctx the parse tree
	 */
	void exitStructPattern(BluespecParser.StructPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#tuplePattern}.
	 * @param ctx the parse tree
	 */
	void enterTuplePattern(BluespecParser.TuplePatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#tuplePattern}.
	 * @param ctx the parse tree
	 */
	void exitTuplePattern(BluespecParser.TuplePatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#casePatItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void enterCasePatItem_functionBodyStmt(BluespecParser.CasePatItem_functionBodyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#casePatItem_functionBodyStmt}.
	 * @param ctx the parse tree
	 */
	void exitCasePatItem_functionBodyStmt(BluespecParser.CasePatItem_functionBodyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#casePatItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCasePatItem_actionStmt(BluespecParser.CasePatItem_actionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#casePatItem_actionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCasePatItem_actionStmt(BluespecParser.CasePatItem_actionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#casePatItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void enterCasePatItem_actionValueStmt(BluespecParser.CasePatItem_actionValueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#casePatItem_actionValueStmt}.
	 * @param ctx the parse tree
	 */
	void exitCasePatItem_actionValueStmt(BluespecParser.CasePatItem_actionValueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#casePatItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void enterCasePatItem_moduleStmt(BluespecParser.CasePatItem_moduleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#casePatItem_moduleStmt}.
	 * @param ctx the parse tree
	 */
	void exitCasePatItem_moduleStmt(BluespecParser.CasePatItem_moduleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#casePatItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCasePatItem_expressionStmt(BluespecParser.CasePatItem_expressionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#casePatItem_expressionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCasePatItem_expressionStmt(BluespecParser.CasePatItem_expressionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseExpr}.
	 * @param ctx the parse tree
	 */
	void enterCaseExpr(BluespecParser.CaseExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseExpr}.
	 * @param ctx the parse tree
	 */
	void exitCaseExpr(BluespecParser.CaseExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#caseExprItem}.
	 * @param ctx the parse tree
	 */
	void enterCaseExprItem(BluespecParser.CaseExprItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#caseExprItem}.
	 * @param ctx the parse tree
	 */
	void exitCaseExprItem(BluespecParser.CaseExprItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#systemTaskStmt}.
	 * @param ctx the parse tree
	 */
	void enterSystemTaskStmt(BluespecParser.SystemTaskStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#systemTaskStmt}.
	 * @param ctx the parse tree
	 */
	void exitSystemTaskStmt(BluespecParser.SystemTaskStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#displayTaskName}.
	 * @param ctx the parse tree
	 */
	void enterDisplayTaskName(BluespecParser.DisplayTaskNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#displayTaskName}.
	 * @param ctx the parse tree
	 */
	void exitDisplayTaskName(BluespecParser.DisplayTaskNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#stringTaskName}.
	 * @param ctx the parse tree
	 */
	void enterStringTaskName(BluespecParser.StringTaskNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#stringTaskName}.
	 * @param ctx the parse tree
	 */
	void exitStringTaskName(BluespecParser.StringTaskNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#systemFunctionCall}.
	 * @param ctx the parse tree
	 */
	void enterSystemFunctionCall(BluespecParser.SystemFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#systemFunctionCall}.
	 * @param ctx the parse tree
	 */
	void exitSystemFunctionCall(BluespecParser.SystemFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#systemTaskCall}.
	 * @param ctx the parse tree
	 */
	void enterSystemTaskCall(BluespecParser.SystemTaskCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#systemTaskCall}.
	 * @param ctx the parse tree
	 */
	void exitSystemTaskCall(BluespecParser.SystemTaskCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#stringAVTaskName}.
	 * @param ctx the parse tree
	 */
	void enterStringAVTaskName(BluespecParser.StringAVTaskNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#stringAVTaskName}.
	 * @param ctx the parse tree
	 */
	void exitStringAVTaskName(BluespecParser.StringAVTaskNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#attributeInstances}.
	 * @param ctx the parse tree
	 */
	void enterAttributeInstances(BluespecParser.AttributeInstancesContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#attributeInstances}.
	 * @param ctx the parse tree
	 */
	void exitAttributeInstances(BluespecParser.AttributeInstancesContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#attributeInstance}.
	 * @param ctx the parse tree
	 */
	void enterAttributeInstance(BluespecParser.AttributeInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#attributeInstance}.
	 * @param ctx the parse tree
	 */
	void exitAttributeInstance(BluespecParser.AttributeInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#attrSpec}.
	 * @param ctx the parse tree
	 */
	void enterAttrSpec(BluespecParser.AttrSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#attrSpec}.
	 * @param ctx the parse tree
	 */
	void exitAttrSpec(BluespecParser.AttrSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#attrName}.
	 * @param ctx the parse tree
	 */
	void enterAttrName(BluespecParser.AttrNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#attrName}.
	 * @param ctx the parse tree
	 */
	void exitAttrName(BluespecParser.AttrNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#provisos}.
	 * @param ctx the parse tree
	 */
	void enterProvisos(BluespecParser.ProvisosContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#provisos}.
	 * @param ctx the parse tree
	 */
	void exitProvisos(BluespecParser.ProvisosContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#proviso}.
	 * @param ctx the parse tree
	 */
	void enterProviso(BluespecParser.ProvisoContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#proviso}.
	 * @param ctx the parse tree
	 */
	void exitProviso(BluespecParser.ProvisoContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeclassDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeclassDef(BluespecParser.TypeclassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeclassDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeclassDef(BluespecParser.TypeclassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeclassIde}.
	 * @param ctx the parse tree
	 */
	void enterTypeclassIde(BluespecParser.TypeclassIdeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeclassIde}.
	 * @param ctx the parse tree
	 */
	void exitTypeclassIde(BluespecParser.TypeclassIdeContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typelist}.
	 * @param ctx the parse tree
	 */
	void enterTypelist(BluespecParser.TypelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typelist}.
	 * @param ctx the parse tree
	 */
	void exitTypelist(BluespecParser.TypelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedepends}.
	 * @param ctx the parse tree
	 */
	void enterTypedepends(BluespecParser.TypedependsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedepends}.
	 * @param ctx the parse tree
	 */
	void exitTypedepends(BluespecParser.TypedependsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typedepend}.
	 * @param ctx the parse tree
	 */
	void enterTypedepend(BluespecParser.TypedependContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typedepend}.
	 * @param ctx the parse tree
	 */
	void exitTypedepend(BluespecParser.TypedependContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#overloadedDef}.
	 * @param ctx the parse tree
	 */
	void enterOverloadedDef(BluespecParser.OverloadedDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#overloadedDef}.
	 * @param ctx the parse tree
	 */
	void exitOverloadedDef(BluespecParser.OverloadedDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#typeclassInstanceDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeclassInstanceDef(BluespecParser.TypeclassInstanceDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#typeclassInstanceDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeclassInstanceDef(BluespecParser.TypeclassInstanceDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#derives}.
	 * @param ctx the parse tree
	 */
	void enterDerives(BluespecParser.DerivesContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#derives}.
	 * @param ctx the parse tree
	 */
	void exitDerives(BluespecParser.DerivesContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#externModuleImport}.
	 * @param ctx the parse tree
	 */
	void enterExternModuleImport(BluespecParser.ExternModuleImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#externModuleImport}.
	 * @param ctx the parse tree
	 */
	void exitExternModuleImport(BluespecParser.ExternModuleImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#importBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterImportBVIStmt(BluespecParser.ImportBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#importBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitImportBVIStmt(BluespecParser.ImportBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#enabled_sel}.
	 * @param ctx the parse tree
	 */
	void enterEnabled_sel(BluespecParser.Enabled_selContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#enabled_sel}.
	 * @param ctx the parse tree
	 */
	void exitEnabled_sel(BluespecParser.Enabled_selContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ready_sel}.
	 * @param ctx the parse tree
	 */
	void enterReady_sel(BluespecParser.Ready_selContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ready_sel}.
	 * @param ctx the parse tree
	 */
	void exitReady_sel(BluespecParser.Ready_selContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#clocked_by_sel}.
	 * @param ctx the parse tree
	 */
	void enterClocked_by_sel(BluespecParser.Clocked_by_selContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#clocked_by_sel}.
	 * @param ctx the parse tree
	 */
	void exitClocked_by_sel(BluespecParser.Clocked_by_selContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#reset_by_sel}.
	 * @param ctx the parse tree
	 */
	void enterReset_by_sel(BluespecParser.Reset_by_selContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#reset_by_sel}.
	 * @param ctx the parse tree
	 */
	void exitReset_by_sel(BluespecParser.Reset_by_selContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#parameterBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterParameterBVIStmt(BluespecParser.ParameterBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#parameterBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitParameterBVIStmt(BluespecParser.ParameterBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#methodBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterMethodBVIStmt(BluespecParser.MethodBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#methodBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitMethodBVIStmt(BluespecParser.MethodBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#portBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterPortBVIStmt(BluespecParser.PortBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#portBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitPortBVIStmt(BluespecParser.PortBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#inputClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterInputClockBVIStmt(BluespecParser.InputClockBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#inputClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitInputClockBVIStmt(BluespecParser.InputClockBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#portsDef}.
	 * @param ctx the parse tree
	 */
	void enterPortsDef(BluespecParser.PortsDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#portsDef}.
	 * @param ctx the parse tree
	 */
	void exitPortsDef(BluespecParser.PortsDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#portId}.
	 * @param ctx the parse tree
	 */
	void enterPortId(BluespecParser.PortIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#portId}.
	 * @param ctx the parse tree
	 */
	void exitPortId(BluespecParser.PortIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultClockBVIStmt(BluespecParser.DefaultClockBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultClockBVIStmt(BluespecParser.DefaultClockBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#outputClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterOutputClockBVIStmt(BluespecParser.OutputClockBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#outputClockBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitOutputClockBVIStmt(BluespecParser.OutputClockBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#inputResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterInputResetBVIStmt(BluespecParser.InputResetBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#inputResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitInputResetBVIStmt(BluespecParser.InputResetBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#clockId}.
	 * @param ctx the parse tree
	 */
	void enterClockId(BluespecParser.ClockIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#clockId}.
	 * @param ctx the parse tree
	 */
	void exitClockId(BluespecParser.ClockIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#defaultResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterDefaultResetBVIStmt(BluespecParser.DefaultResetBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#defaultResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitDefaultResetBVIStmt(BluespecParser.DefaultResetBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#outputResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterOutputResetBVIStmt(BluespecParser.OutputResetBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#outputResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitOutputResetBVIStmt(BluespecParser.OutputResetBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ancestorBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterAncestorBVIStmt(BluespecParser.AncestorBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ancestorBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitAncestorBVIStmt(BluespecParser.AncestorBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#sameFamilyBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterSameFamilyBVIStmt(BluespecParser.SameFamilyBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#sameFamilyBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitSameFamilyBVIStmt(BluespecParser.SameFamilyBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#scheduleBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterScheduleBVIStmt(BluespecParser.ScheduleBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#scheduleBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitScheduleBVIStmt(BluespecParser.ScheduleBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#operatorId}.
	 * @param ctx the parse tree
	 */
	void enterOperatorId(BluespecParser.OperatorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#operatorId}.
	 * @param ctx the parse tree
	 */
	void exitOperatorId(BluespecParser.OperatorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#pathBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterPathBVIStmt(BluespecParser.PathBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#pathBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitPathBVIStmt(BluespecParser.PathBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBVIStmt(BluespecParser.InterfaceBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBVIStmt(BluespecParser.InterfaceBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#interfaceBVIMembDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBVIMembDecl(BluespecParser.InterfaceBVIMembDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#interfaceBVIMembDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBVIMembDecl(BluespecParser.InterfaceBVIMembDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#inoutBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterInoutBVIStmt(BluespecParser.InoutBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#inoutBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitInoutBVIStmt(BluespecParser.InoutBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#resetId}.
	 * @param ctx the parse tree
	 */
	void enterResetId(BluespecParser.ResetIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#resetId}.
	 * @param ctx the parse tree
	 */
	void exitResetId(BluespecParser.ResetIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#noResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void enterNoResetBVIStmt(BluespecParser.NoResetBVIStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#noResetBVIStmt}.
	 * @param ctx the parse tree
	 */
	void exitNoResetBVIStmt(BluespecParser.NoResetBVIStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#externCImport}.
	 * @param ctx the parse tree
	 */
	void enterExternCImport(BluespecParser.ExternCImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#externCImport}.
	 * @param ctx the parse tree
	 */
	void exitExternCImport(BluespecParser.ExternCImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#cFuncArgs}.
	 * @param ctx the parse tree
	 */
	void enterCFuncArgs(BluespecParser.CFuncArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#cFuncArgs}.
	 * @param ctx the parse tree
	 */
	void exitCFuncArgs(BluespecParser.CFuncArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#cFuncArg}.
	 * @param ctx the parse tree
	 */
	void enterCFuncArg(BluespecParser.CFuncArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#cFuncArg}.
	 * @param ctx the parse tree
	 */
	void exitCFuncArg(BluespecParser.CFuncArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#fsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterFsmStmt(BluespecParser.FsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#fsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitFsmStmt(BluespecParser.FsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#exprFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprFsmStmt(BluespecParser.ExprFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#exprFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprFsmStmt(BluespecParser.ExprFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#seqFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterSeqFsmStmt(BluespecParser.SeqFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#seqFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitSeqFsmStmt(BluespecParser.SeqFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#parFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterParFsmStmt(BluespecParser.ParFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#parFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitParFsmStmt(BluespecParser.ParFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#ifFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfFsmStmt(BluespecParser.IfFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#ifFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfFsmStmt(BluespecParser.IfFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#whileFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileFsmStmt(BluespecParser.WhileFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#whileFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileFsmStmt(BluespecParser.WhileFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#forFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterForFsmStmt(BluespecParser.ForFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#forFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitForFsmStmt(BluespecParser.ForFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#returnFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnFsmStmt(BluespecParser.ReturnFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#returnFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnFsmStmt(BluespecParser.ReturnFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#repeatFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterRepeatFsmStmt(BluespecParser.RepeatFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#repeatFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitRepeatFsmStmt(BluespecParser.RepeatFsmStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BluespecParser#loopBodyFsmStmt}.
	 * @param ctx the parse tree
	 */
	void enterLoopBodyFsmStmt(BluespecParser.LoopBodyFsmStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BluespecParser#loopBodyFsmStmt}.
	 * @param ctx the parse tree
	 */
	void exitLoopBodyFsmStmt(BluespecParser.LoopBodyFsmStmtContext ctx);
}