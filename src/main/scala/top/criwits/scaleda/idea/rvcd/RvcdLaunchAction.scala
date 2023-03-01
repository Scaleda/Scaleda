package top.criwits.scaleda
package idea.rvcd

import idea.utils.{MainLogger, OutputLogger}
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{OS, Paths}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import rvcd.rvcd.RvcdEmpty

import java.io.File

class RvcdLaunchAction(waveform: File, source: Seq[File]) extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val service = event.getProject.getService(classOf[RvcdService])
    service.launchWithWaveformAndSource(waveform, source)
  }
}
