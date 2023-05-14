package top.scaleda
package verilog.completion.signal

import idea.utils.Icons
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.PortDeclarationPsiNode._
import verilog.psi.nodes.signal._

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
      val regDeclarations =
        PsiTreeUtil.findChildrenOfAnyType(currentModuleDeclaration, classOf[VariableDeclarationPsiNode]).asScala
      regDeclarations.foreach(f => {
        val regIdentifiers = PsiTreeUtil.findChildrenOfAnyType(f, classOf[VariableIdentifierPsiNode]).asScala

        regIdentifiers.foreach(id => result.addElement(LookupElementBuilder.create(id.getName).withIcon(Icons.verilogReg).withTypeText(f.getTypeText)))
      })
    }

    val portDeclarations =
      PsiTreeUtil.findChildrenOfAnyType(currentModuleDeclaration, classOf[PortDeclarationPsiNode]).asScala

    portDeclarations.foreach(f => {
      val identifiers = PsiTreeUtil.findChildrenOfAnyType(f, classOf[PortIdentifierPsiNode]).asScala

      identifiers.foreach(id => {
        if (id != null && id.getName != null && id.getName != "") {
          f.getPortType match {
            case INPUT => if (input) {
              result.addElement(LookupElementBuilder.create(id.getName).withIcon(Icons.verilogInput).withTypeText("input"))
            }
            case OUTPUT => if (output) {
              result.addElement(LookupElementBuilder.create(id.getName).withIcon(Icons.verilogOutput).withTypeText("output"))
            }
            case INOUT => if (input || output) {
              result.addElement(LookupElementBuilder.create(id.getName).withIcon(Icons.verilogInout).withTypeText("inout"))
            }
            case OUTPUT_REG => if (outputReg) {
              result.addElement(LookupElementBuilder.create(id.getName).withIcon(Icons.verilogOutputReg).withTypeText("output reg"))
            }
          }
        }
      })

    })
  }
}
