package top.scaleda
package verilog.annotation.instantiation

import idea.ScaledaBundle
import verilog.psi.nodes.expression.ExpressionPsiNode
import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.{PsiDocumentManager, PsiFile}

class ModulePortConnectionIntent(
    val instant: ModuleInstantiationPsiNode,
    val connectionMap: Seq[(PortIdentifierPsiNode, Option[ExpressionPsiNode])]
) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.not.connected.port.fix")
  override def getText: String       = ScaledaBundle.message("annotation.not.connected.port.fix")

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    // first, find replace start and end place
    val portConnectionList = instant.getPortConnectionList
    if (portConnectionList == null) return

    val doc = editor.getDocument
    println(connectionMap)
    doc.replaceString(
      portConnectionList.getTextRange.getStartOffset,
      portConnectionList.getTextRange.getEndOffset,
      connectionMap
        .map {
          case (port, Some(expr)) => s".${port.getName}(${Option(expr).map(_.getText).getOrElse("")})"
          case (port, None)       => s".${port.getName}()"
        }
        .mkString(", ")
    )
    PsiDocumentManager.getInstance(project).commitDocument(doc)
    CodeStyleManager.getInstance(project).adjustLineIndent(file, portConnectionList.getTextRange)
  }
}
