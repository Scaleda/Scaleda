//package top.criwits.scaleda.verilog.psi.references
//
//import com.intellij.psi.PsiElementResolveResult
//import com.intellij.psi.PsiReferenceBase
//import com.intellij.psi.ResolveResult
//import com.intellij.psi.util.PsiTreeUtil
//import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleDeclarationPsiNode
//import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleInstantiationPsiNode
//import top.criwits.scaleda.verilog.psi.factory.nodes.NamedPortConnectionPsiNode
//
//class NamedPortConnectionReferenceOld(element: NamedPortConnectionPsiNode) :
//    PsiReferenceBase.Poly<NamedPortConnectionPsiNode>(
//        element,
//        element.getHoldPsiNodeRelativeTextRange(),
//        true
//    ) {
//
//    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
//        val moduleInstantiationPsiNode = PsiTreeUtil
//            .getParentOfType(myElement, ModuleInstantiationPsiNode::class.java)
//            ?: return emptyArray()
//        return (moduleInstantiationPsiNode.reference as? Poly<*> ?: return emptyArray())
//            .multiResolve(incompleteCode)
//            .map { it.element }
//            .filterIsInstance(ModuleDeclarationPsiNode::class.java)
////                .flatMap { it.availableNamedElements }
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