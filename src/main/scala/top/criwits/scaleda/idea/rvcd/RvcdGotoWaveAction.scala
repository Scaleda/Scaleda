package top.criwits.scaleda
package idea.rvcd

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}

class RvcdGotoWaveAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    println(e.getData(CommonDataKeys.PSI_ELEMENT))
  }
}
