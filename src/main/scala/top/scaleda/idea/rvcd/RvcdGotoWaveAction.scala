package top.scaleda
package idea.rvcd

import idea.utils.MainLogger
import verilog.VerilogPSIFileRoot
import verilog.psi.nodes.IdentifierPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.psi.util.PsiTreeUtil
import rvcd.rvcd.RvcdSignalPath
import top.scaleda.kernel.rvcd.Rvcd

import scala.collection.mutable.ArrayBuffer

/** <b>Go To | Waveform</b> context option for HDL files.
  */
class RvcdGotoWaveAction extends AnAction {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val psiFile      = e.getData(CommonDataKeys.PSI_FILE)
    // if not verilog file, disable
    if (!psiFile.isInstanceOf[VerilogPSIFileRoot]) {
      presentation.setEnabled(false)
      return
    }

    // if rvcd is not running OR rvcd is not working for current simulation, disable
    if (!RvcdService.hasInstance) {
      presentation.setEnabled(false)
      return
    }

    // if selected is not signal, disable
    val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)

    psiElement match {
      case _: SignalIdentifierPsiNode =>
        // This indicates caret is at some reference (not the declaration itself)
        presentation.setEnabled(true)
      case _: IdentifierPsiNode =>
        // Tricky
        val parent = psiElement.getParent
        if (parent != null && parent.isInstanceOf[SignalIdentifierPsiNode]) {
          presentation.setEnabled(true)
        } else
          presentation.setEnabled(false)
      case _ =>
        presentation.setEnabled(false)
    }
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)

    val currentSignal = psiElement match {
      case _: SignalIdentifierPsiNode => psiElement
      case _: IdentifierPsiNode =>
        val parent = psiElement.getParent
        parent.asInstanceOf[SignalIdentifierPsiNode]
      case _ => null // should never reach
    }
    var paths   = ArrayBuffer[String]()
    var psiNode = currentSignal
    while (psiNode != null && psiNode.getParent != null) {
      MainLogger.info(s"psi now: $psiNode")
      (psiNode match {
        case p: SignalIdentifierPsiNode  => Some(p.getNameIdentifier.getText)
        case m: ModuleDeclarationPsiNode => Some(m.getNameIdentifier.getText)
        case _                           => None
      }).foreach(paths.addOne)
      psiNode = PsiTreeUtil.getParentOfType(psiNode, classOf[ModuleDeclarationPsiNode])
    }

    paths = paths.reverse
    MainLogger.info(s"RvcdGotoWaveAction: psi=$currentSignal, signal=${currentSignal.getText}, paths=$paths")
    val (client, shutdown) = Rvcd()
    client.gotoSignal(RvcdSignalPath.of("", paths.toSeq))
    shutdown()
  }
}
