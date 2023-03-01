package top.criwits.scaleda
package idea.rvcd

import idea.utils.{MainLogger, OutputLogger}
import kernel.rvcd.RvcdClient
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{OS, Paths}

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import rvcd.rvcd.RvcdEmpty

import java.io.File

/** Daemon service for RVCD, the waveform viewer of Scaleda.
  */
class RvcdService extends Disposable {
  import RvcdService._

  private var myProject: Project = _
  def setProject(project: Project): Unit = {
    myProject = project
  }

  var (client, shutdown) = RvcdClient()

  def launchWithWaveformAndSource(waveform: File, source: Seq[File]): Unit = {
    if (!hasInstance) {
      // No instance is running, launch
      if (!RvcdService.rvcdFile.canExecute) {
        throw new RuntimeException()
      }

      // generate rvcd startup arguments
      // TODO NO SOURCE
      // val cmdLine = Seq(RvcdService.rvcdFile.getAbsolutePath)  ++ Seq(waveform.getAbsolutePath)
      val cmdLine = Seq(RvcdService.rvcdFile.getAbsolutePath) ++ source
        .map(s => Seq("-i", s.getAbsolutePath))
        .fold(Seq())(_ ++ _) ++ Seq(waveform.getAbsolutePath)
      MainLogger.info(s"Starting RVCD with command line: ${cmdLine.mkString(" ")}")

      // run
      CommandRunner.execute(
        Seq(
          CommandDeps(
            commands = cmdLine,
            description = "Start RVCD Instance"
          )
        ),
        new OutputLogger.Handler(myProject)
      )
    } else {
      client.openFileWith(
        rvcd.rvcd.RvcdOpenFileWith.of(waveform.getAbsolutePath, "", source.map(f => f.getAbsolutePath), None)
      )
    }
  }

  override def dispose(): Unit = {
    shutdown()
  }
}

object RvcdService {
  final val rvcdFile = new File(Paths.getBinaryDir, "rvcd" + (if (OS.isWindows) ".exe" else ""))

  final def hasInstance: Boolean = {
    var (client, shutdown) = RvcdClient()
    try {
      try {
        client.ping(RvcdEmpty.of())
        true
      } catch {
        case _e: io.grpc.StatusRuntimeException =>
          shutdown()
          val r = RvcdClient()
          client = r._1
          shutdown = r._2
          client.ping(RvcdEmpty.of())
          true
      }
    } catch {
      case e: Throwable =>
        // e.printStackTrace()
        false
    }
  }
}
