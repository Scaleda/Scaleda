package top.criwits.scaleda
package verilog.annotation

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode

class ModulePortConnectionIntent(val instant: ModuleInstantiationPsiNode, val ports: Seq[String]) extends BaseIntentionAction {
  override def getFamilyName: String = "Add named port connection"

  override def getText: String = "Add named port connection for " + ports.mkString(", ")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    val document = editor.getDocument

  }
}
