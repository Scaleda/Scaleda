package top.criwits.scaleda
package verilog.completion.module

import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.codeInsight.completion.util.{MethodParenthesesHandler, ParenthesesInsertHandler}
import com.intellij.codeInsight.completion.{CompletionParameters, CompletionProvider, CompletionResultSet}
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import top.criwits.scaleda.idea.utils.Icons
import top.criwits.scaleda.verilog.psi.nodes.signal.PortDeclarationPsiNode

class ModulePortProvider extends CompletionProvider[CompletionParameters] {
  override def addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet
  ): Unit = {
    val element            = parameters.getOriginalPosition
    val instantiatedModule = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])

    // get the module declaration
    val resolve = instantiatedModule.getReference
      .multiResolve(false)

    if (resolve.length == 0) return

    val moduleDeclaration = resolve
      .head
      .getElement
      .asInstanceOf[ModuleDeclarationPsiNode]

    moduleDeclaration.getPorts.foreach(port => {result.addElement(
      LookupElementBuilder.create(port.getIdentifier.getName)
        .withIcon(port.getPortType match {
          case PortDeclarationPsiNode.INPUT => Icons.verilogInput
          case PortDeclarationPsiNode.INOUT => Icons.verilogInout
          case PortDeclarationPsiNode.OUTPUT => Icons.verilogOutput
          case PortDeclarationPsiNode.OUTPUT_REG => Icons.verilogOutputReg
          case _ => AllIcons.General.TodoQuestion // should never reach
        })
//        .withTypeText(port.getTypeText)
        .withTailText(s" (in module \"${moduleDeclaration.getName}\")")
        .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS))
    })

  }
}
