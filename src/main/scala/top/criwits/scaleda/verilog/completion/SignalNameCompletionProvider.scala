package top.criwits.scaleda
package verilog.completion

import idea.utils.Icons
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.{NetDeclarationPsiNode, NetIdentifierPsiNode}

import com.intellij.codeInsight.completion.{CompletionParameters, CompletionProvider, CompletionResultSet}
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

import scala.jdk.CollectionConverters._

class SignalNameCompletionProvider(
    net: Boolean = true,
    reg: Boolean = true,
    input: Boolean = true,
    output: Boolean = true,
    outputReg: Boolean = true
) extends CompletionProvider[CompletionParameters] {
  override def addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext,
      result: CompletionResultSet
  ): Unit = {
//    println("hey!")
    // get current module
    val element                  = parameters.getOriginalPosition
    val currentModuleDeclaration = PsiTreeUtil.getParentOfType(element, classOf[ModuleDeclarationPsiNode])

    // Nets (wire, tri, ...)
    if (net) {
      val netDeclarations =
        PsiTreeUtil.findChildrenOfAnyType(currentModuleDeclaration, classOf[NetDeclarationPsiNode]).asScala
      netDeclarations.foreach(f => {
        val netIdentifiers = PsiTreeUtil.findChildrenOfAnyType(f, classOf[NetIdentifierPsiNode]).asScala

        netIdentifiers.foreach(id =>
          result.addElement(
            LookupElementBuilder.create(id.getName).withIcon(Icons.verilogWire).withTypeText(f.getTypeText)
          )
        )
      })

    }

    if (reg) {

    }
  }
}
