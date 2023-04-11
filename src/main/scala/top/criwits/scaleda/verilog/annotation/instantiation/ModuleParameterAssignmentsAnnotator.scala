package top.criwits.scaleda
package verilog.annotation.instantiation

import idea.ScaledaBundle
import verilog.psi.nodes.instantiation.{ListOfParameterAssignmentsPsiNode, ModuleInstantiationPsiNode, OrderedParameterAssignmentPsiNode, ParameterValueAssignmentPsiNode}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile}

class ModuleParameterAssignmentsAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[ParameterValueAssignmentPsiNode]) return
    val e = element.asInstanceOf[ParameterValueAssignmentPsiNode]

    // Step 1: check if there is [[ListOfParameterAssignmentsPsiNode]]
    val listOfParameterAssignmentsPsiNode = e.getListOfParameterAssignments
    if (listOfParameterAssignmentsPsiNode.isEmpty) {
      // Suggest remove...
      holder.newAnnotation(HighlightSeverity.WEAK_WARNING, ScaledaBundle.message("annotation.empty.parameter.list"))
        .withFix(new RemoveEmptyParameterAssignmentsIntent(e))
        .highlightType(ProblemHighlightType.WEAK_WARNING)
        .create()
      return
    }

    // Step 2: resolve module
    val instance = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])
    if (instance == null) return
    val reference = instance.getReference.resolve
    if (reference == null) return
    val module = reference.asInstanceOf[ModuleDeclarationPsiNode]

    val named = listOfParameterAssignmentsPsiNode.get.getNamedParameterAssignments
    val ordered = listOfParameterAssignmentsPsiNode.get.getOrderedParameterAssignments

    if (ordered.nonEmpty) {
      // Step 3: For ordered list we check number...
      val paramSlots = module.getParameters
      if (paramSlots.length < ordered.length) {
        holder.newAnnotation(HighlightSeverity.ERROR,
          ScaledaBundle.message("annotation.redundant.parameters", module.getName, paramSlots.length, ordered.length))
          .withFix(new RemoveRedundantAssignments(listOfParameterAssignmentsPsiNode.get, ordered, paramSlots.length))
          .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
          .create()
      }
    }

    if (named.nonEmpty) {
      // Step 4: For named we check repeat
      //TODO
    }

  }
}


class RemoveRedundantAssignments(list: ListOfParameterAssignmentsPsiNode, ordered: Seq[OrderedParameterAssignmentPsiNode], required: Int) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.redundant.parameters.fix.remove")

  override def getText: String = ScaledaBundle.message("annotation.redundant.parameters.fix.remove")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    editor.getDocument.replaceString(
      list.getTextRange.getStartOffset,
      list.getTextRange.getEndOffset,
      ordered.slice(0, required).map(_.getText).mkString(", ")
    )
  }
}

class RemoveEmptyParameterAssignmentsIntent(element: ParameterValueAssignmentPsiNode) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.empty.parameter.list.fix.remove")

  override def getText: String = ScaledaBundle.message("annotation.empty.parameter.list.fix.remove")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    val range = element.getTextRange
    val identifier = PsiTreeUtil.prevVisibleLeaf(element)
    val headOffset = if (identifier != null) {
      identifier.getTextRange.getEndOffset
    } else {range.getStartOffset}

    val body = PsiTreeUtil.nextVisibleLeaf(element)
    val tailOffset = if(body != null) {
      body.getTextRange.getStartOffset
    } else {range.getEndOffset}

    val doc = editor.getDocument
    doc.replaceString(headOffset, tailOffset, " ")
  }
}
