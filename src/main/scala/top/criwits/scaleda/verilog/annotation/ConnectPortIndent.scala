package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.expression.ExpressionPsiNode
import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.{PsiElement, PsiFile}
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.editor.formatter.VerilogLineIndentProvider

abstract class ConnectPortIndent(
    val instant: ModuleInstantiationPsiNode,
    val connectionMap: Array[(PortDeclarationPsiNode, Option[ExpressionPsiNode])],
) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.not.connected.port.fix")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    // first, find replace start and end place
    val portConnectionList = instant.getPortConnectionList
    if (portConnectionList == null) return

    val doc = editor.getDocument
    val newPortList = connectionMap.map(conn => s".${conn._1.getIdentifier.getName}(${conn._2 match {
      case None => ""
      case Some(exp) => if (exp == null) "" else exp.getText
    }})").mkString(",")

    doc.replaceString(portConnectionList.getTextRange.getStartOffset, portConnectionList.getTextRange.getEndOffset, newPortList)
    CodeStyleManager.getInstance(project).reformat(portConnectionList)
  }
}
