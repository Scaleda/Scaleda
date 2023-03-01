package top.criwits.scaleda
package idea.rvcd

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import top.criwits.scaleda.idea.utils.MainLogger
import top.criwits.scaleda.verilog.VerilogPSIFileRoot
import top.criwits.scaleda.verilog.psi.nodes.IdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

/** <b>Go To | Waveform</b> context option for HDL files.
  */
class RvcdGotoWaveAction extends AnAction {
  override def update(e: AnActionEvent): Unit = {
    val presentation = e.getPresentation
    val psiFile = e.getData(CommonDataKeys.PSI_FILE)
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

    // TODO: Stub!
    MainLogger.warn(s"RvcdGotoWaveAction: psi=$currentSignal, signal=${currentSignal.getText}")

  }
}
