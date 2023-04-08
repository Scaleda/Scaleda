package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.expression.ExpressionPsiNode
import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.{PsiDocumentManager, PsiFile}
import com.intellij.psi.codeStyle.CodeStyleManager

abstract class ConnectPortIndent(
    val instant: ModuleInstantiationPsiNode,
    val connectionMap: Array[(PortDeclarationPsiNode, Option[ExpressionPsiNode])]
) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.not.connected.port.fix")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    // first, find replace start and end place
    val portConnectionList = instant.getPortConnectionList
    if (portConnectionList == null) return

    val connectedPorts = portConnectionList.getNamedPorts
      .map(p => {
        val h = p.getHoldPsiNode
        if (h == null) ""
        else {
          val i = h.getNameIdentifier
          // val signal = p.getSignal
          // (i.getText, signal)
          i.getText
        }
      })
      .filter(_.nonEmpty)
      .toSet

    val doc = editor.getDocument
    val newPortList = connectionMap
      .filter(c => !connectedPorts.contains(c._1.getIdentifier.getName))
      .map(conn =>
        s".${conn._1.getIdentifier.getName}(${conn._2 match {
          case None      => ""
          case Some(exp) => if (exp == null) "" else exp.getText
        }})"
      )
      .mkString(",")

    val original = portConnectionList.getText
    val originalTrim = original.trim
    doc.replaceString(
      portConnectionList.getTextRange.getStartOffset,
      portConnectionList.getTextRange.getEndOffset,
      original + (if (originalTrim.isEmpty || originalTrim.endsWith(",")) "" else ",") + newPortList
    )
    PsiDocumentManager.getInstance(project).commitDocument(doc)
    CodeStyleManager.getInstance(project).adjustLineIndent(file, portConnectionList.getTextRange)
  }
}
