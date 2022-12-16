//package top.criwits.scaleda.verilog.psi.references
//
//import com.intellij.psi.PsiElementResolveResult
//import com.intellij.psi.PsiReferenceBase
//import com.intellij.psi.ResolveResult
//import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleDeclarationPsiNode
//import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleInstantiationPsiNode
//import top.criwits.scaleda.verilog.utils.*;
//
//class ModuleInstantiationReferenceOld(element: ModuleInstantiationPsiNode) :
//    PsiReferenceBase.Poly<ModuleInstantiationPsiNode>(
//        element,
//        element.getHoldPsiNodeRelativeTextRange(),
//        true
//    ) {
//
//    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
//        return FileUtilsJava.getAllVerilogFilesList(myElement.project)
//            .flatMap { it.availableNamedElementsLists }
//            .filterIsInstance(ModuleDeclarationPsiNode::class.java)
//            .filter { it.name == myElement.getHoldPsiNode()?.name }
//            .map { PsiElementResolveResult(it) }
//            .toTypedArray()
//    }
//
//    override fun getVariants(): Array<Any> {
//        return emptyArray()
//    }
//
//}