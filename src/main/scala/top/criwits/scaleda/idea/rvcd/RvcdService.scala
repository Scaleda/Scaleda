package top.criwits.scaleda
package idea.rvcd

import kernel.rvcd.RvcdClient

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.{Project, ProjectManager}
import rvcd.rvcd.{RvcdEmpty, RvcdRpcGrpc}
import top.criwits.scaleda.idea.utils.{MainLogger, OutputLogger}
import top.criwits.scaleda.kernel.shell.command.{CommandDeps, CommandRunner}
import top.criwits.scaleda.kernel.utils.{OS, Paths}

import java.io.File

/**
 * Daemon service for RVCD, the waveform viewer of Scaleda.
 */
class RvcdService extends Disposable {
  private val rvcdFile = new File(Paths.getBinaryDir, "rvcd" + (if (OS.isWindows) ".exe" else ""))
  private def stub: RvcdRpcGrpc.RvcdRpcBlockingStub = RvcdClient("127.0.0.1")

  private var myProject: Project = _
  def setProject(project: Project): Unit = {
    myProject = project
  }

  private def hasInstance: Boolean = {
    try {
      stub.ping(RvcdEmpty.of())
      true
    } catch {
      case e: Throwable =>
        false
    }
  }

  def launchWithWaveformAndSource(waveform: File, source: Seq[File]): Unit = {
    if (!hasInstance) {
      // No instance is running, launch
      if (!rvcdFile.canExecute) {
        throw new RuntimeException()
      }

      // generate rvcd startup arguments
      // TODO NO SOURCE
      val cmdLine = Seq(rvcdFile.getAbsolutePath)  ++ Seq(waveform.getAbsolutePath)
//      val cmdLine = Seq(rvcdFile.getAbsolutePath) ++ source.map(s => Seq("-i", s.getAbsolutePath)).reduce(_ ++ _) ++ Seq(waveform.getAbsolutePath)
      MainLogger.info(s"Starting RVCD with command line: ${cmdLine.mkString(" ")}")

      // run
      CommandRunner.execute(
        Seq(CommandDeps(
          commands = cmdLine,
          description = "Start RVCD Instance"
        )),
        new OutputLogger.Handler(myProject)
      )
    } else {
      // TODO: RPC Open
    }
  }


  override def dispose(): Unit = {
  }
}