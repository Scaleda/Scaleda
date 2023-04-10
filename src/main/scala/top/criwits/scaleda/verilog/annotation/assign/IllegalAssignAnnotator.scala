package top.criwits.scaleda
package verilog.annotation.assign

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.verilog.psi.nodes.assignments.{AbstractAssignmentPsiNode, AlwaysAssignmentPsiNode, NetAssignmentPsiNode}
import top.criwits.scaleda.verilog.psi.nodes.signal.{NetIdentifierPsiNode, PortDeclarationPsiNode, PortIdentifierPsiNode, VariableIdentifierPsiNode}

class IllegalAssignAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[AbstractAssignmentPsiNode]) return
    val assign = element.asInstanceOf[AbstractAssignmentPsiNode]

    val lvalues = assign.getLValues
    if (!lvalues.forall(ref => {
      assign match {
        case _: NetAssignmentPsiNode => ref.isInstanceOf[NetIdentifierPsiNode] ||
        (ref.isInstanceOf[PortIdentifierPsiNode] && ref.asInstanceOf[PortIdentifierPsiNode].getPortType == PortDeclarationPsiNode.OUTPUT)
        case _: AlwaysAssignmentPsiNode => ref.isInstanceOf[VariableIdentifierPsiNode] || (ref.isInstanceOf[PortIdentifierPsiNode] && ref.asInstanceOf[PortIdentifierPsiNode].getPortType == PortDeclarationPsiNode.OUTPUT_REG)
      }

    })) {
      holder.newAnnotation(HighlightSeverity.ERROR, assign match {
        case _: NetAssignmentPsiNode => ScaledaBundle.message("annotation.net.assign.illegal")
        case _: AlwaysAssignmentPsiNode => ScaledaBundle.message("annotation.always.assign.illegal")
      })
        .highlightType(ProblemHighlightType.ERROR)
        .create()
    }
  }
}
