package top.criwits.scaleda
package verilog.annotation

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode

/**
 * This instant can complement port connection
 * @param instant
 * @param ports
 */
class ModulePortConnectionIntent(val instant: ModuleInstantiationPsiNode, val ports: Iterable[String]) extends BaseIntentionAction {
  override def getFamilyName: String = "Complement port connection"

  override def getText: String = "Complement port connection"

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    // First thing, detect original port connection list


    val document = editor.getDocument
//    document.replaceString()
  }
}
