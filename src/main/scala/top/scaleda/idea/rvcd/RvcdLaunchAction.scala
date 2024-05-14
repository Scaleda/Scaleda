package top.scaleda
package idea.rvcd

import idea.utils.OutputLogger
import idea.waveform.RvcdHandler
import kernel.shell.command.{CommandDeps, CommandRunner}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import rvcd.rvcd.RvcdOpenFileWith

class RvcdLaunchAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    new Thread(
      () => {
        CommandRunner.execute(
          Seq(
            CommandDeps(
              args = Seq(RvcdService.rvcdFile.getAbsolutePath),
              description = "Start RVCD Instance"
            )
          ),
          new OutputLogger.StdErrToInfoHandler(event.getProject, RvcdHandler.getId, "Rvcd")
        )
      },
      "rvcd-instance"
    ).start()
  }
}

class RvcdLaunchActionWith(request: RvcdOpenFileWith) extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    val (client, shutdown) = Rvcd()
    client.openFileWith(request)
    shutdown()
  }
}
