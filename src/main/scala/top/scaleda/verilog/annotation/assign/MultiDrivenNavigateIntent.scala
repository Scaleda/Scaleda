package top.scaleda
package verilog.annotation.assign

import idea.ScaledaBundle
import verilog.psi.nodes.assignments.AlwaysAssignmentPsiNode

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class MultiDrivenNavigateIntent(val anotherAssign: AlwaysAssignmentPsiNode) extends BaseIntentionAction{
  override def getFamilyName: String = ScaledaBundle.message("annotation.multidriven.navigate")

  override def getText: String = ScaledaBundle.message("annotation.multidriven.navigate")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = anotherAssign.navigate(true)
}
