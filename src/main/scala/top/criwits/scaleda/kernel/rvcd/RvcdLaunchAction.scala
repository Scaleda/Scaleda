package top.criwits.scaleda
package kernel.rvcd

import idea.utils.{MainLogger, OutputLogger}
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{KernelLogger, OS, Paths}

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import rvcd.rvcd.RvcdEmpty

import java.io.File

class RvcdLaunchAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    try {
      val service = event.getProject.getService(classOf[RvcdService])
      service.stub.ping(RvcdEmpty.of())
    } catch {
      case e: Throwable =>
        MainLogger.info("rvcd service missing", e, "starting new")
        new Thread(() => {
          CommandRunner.execute(
            Seq(
              CommandDeps(commands =
                Seq(
                  new File(Paths.getBinaryDir, if (OS.isWindows) "rvcd.exe" else "rvcd").getAbsolutePath
                )
              )
            ),
            new OutputLogger.Handler(event.getProject)
          )
          MainLogger.info("rvcd finished")
        }).start()
    }
  }
}
