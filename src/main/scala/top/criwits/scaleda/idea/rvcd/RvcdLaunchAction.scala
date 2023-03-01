package top.criwits.scaleda
package idea.rvcd

import idea.utils.OutputLogger
import kernel.shell.command.{CommandDeps, CommandRunner}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

import java.io.File

class RvcdLaunchAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    CommandRunner.execute(
      Seq(
        CommandDeps(
          commands = Seq(RvcdService.rvcdFile.getAbsolutePath),
          description = "Start RVCD Instance"
        )
      ),
      new OutputLogger.StdErrToWarningHandler(event.getProject)
    )
  }
}

class RvcdLaunchActionGoto(waveform: File, source: Seq[File]) extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val service = event.getProject.getService(classOf[RvcdService])
    service.launchWithWaveformAndSource(waveform, source)
  }
}
