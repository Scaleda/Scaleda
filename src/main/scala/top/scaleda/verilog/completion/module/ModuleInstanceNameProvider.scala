package top.scaleda
package verilog.completion.module

import com.intellij.codeInsight.completion.{CompletionParameters, CompletionProvider, CompletionResultSet}
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import top.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import top.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

class ModuleInstanceNameProvider extends CompletionProvider[CompletionParameters] {
  override def addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet): Unit = {
    val element            = parameters.getOriginalPosition
    val instantiatedModule = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])

    // get the module declaration
    val resolve = instantiatedModule.getReference
      .resolve

    if (resolve == null) return

    val moduleDeclaration = resolve.asInstanceOf[ModuleDeclarationPsiNode]

    result.addElement(LookupElementBuilder.create("u_" + moduleDeclaration.getName))

  }
}
