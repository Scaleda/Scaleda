package top.criwits.scaleda
package idea.rvcd

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import top.criwits.scaleda.verilog.VerilogPSIFileRoot
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
    // TODO
    if (!true) {
      presentation.setEnabled(false)
      return
    }

    // if selected is not signal, disable
    val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
    if (!psiElement.isInstanceOf[SignalIdentifierPsiNode]) {
      presentation.setEnabled(false)
      return
    }

    presentation.setEnabled(true)
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    // TODO
    println(e.getData(CommonDataKeys.PSI_ELEMENT))
  }
}
