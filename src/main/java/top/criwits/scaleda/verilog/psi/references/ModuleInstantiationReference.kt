package top.criwits.scaleda.verilog.psi.references

import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import top.criwits.scaleda.verilog.psi.factory.nodes.ModuleInstantiationPsiNode

//import top.criwits.scaleda.verilog.utils.getAllVerilogFiles

class ModuleInstantiationReference(element: ModuleInstantiationPsiNode) :
    PsiReferenceBase.Poly<ModuleInstantiationPsiNode>(
        element,
        element.getHoldPsiNodeRelativeTextRange(),
        true
    ) {

    //    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
//        return getAllVerilogFiles(myElement.project)
//                .flatMap { it.availableNamedElements }
//                .filterIsInstance(ModuleDeclarationPsiNode::class.java)
//                .filter { it.name == myElement.getHoldPsiNode()?.name }
//                .map { PsiElementResolveResult(it) }
//                .toTypedArray()
//    }
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        TODO("Not yet implemented")
    }

    override fun getVariants(): Array<Any> {
        return emptyArray()
    }

}