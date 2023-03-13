package top.criwits.scaleda
package verilog.annotation

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.verilog.psi.nodes.always.AssignmentPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

class MultiDrivenNavigateIntent(val anotherAssign: AssignmentPsiNode) extends BaseIntentionAction{
  override def getFamilyName: String = ScaledaBundle.message("annotation.multidriven.navigate")

  override def getText: String = ScaledaBundle.message("annotation.multidriven.navigate")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = anotherAssign.navigate(true)
}
