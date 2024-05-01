package top.scaleda
package idea.rvcd

import idea.utils.OutputLogger.StdErrToInfoHandler
import idea.utils.ScaledaIdeaLogger
import idea.waveform.RvcdHandler
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.utils.{OS, Paths}

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import rvcd.rvcd.RvcdEmpty

import java.io.File
import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

/** Daemon service for RVCD, the waveform viewer of Scaleda.
  */
class RvcdService extends Disposable {
  import RvcdService._

  private var myProject: Option[Project] = None
  def setProject(project: Project): Unit = {
    myProject = Some(project)
    Disposer.register(project, this)
  }

  var (client, shutdown) = Rvcd()

  def launchWithWaveformAndSource(waveform: File, source: Seq[File]): Future[Unit] = async {
    if (!hasInstance) {
      // No instance is running, launch
      if (!RvcdService.rvcdFile.canExecute) {
        throw new RuntimeException()
      }

      // generate rvcd startup arguments
      // val cmdLine = Seq(RvcdService.rvcdFile.getAbsolutePath)  ++ Seq(waveform.getAbsolutePath)
      val cmdLine = Seq(RvcdService.rvcdFile.getAbsolutePath) ++ source
        .map(s => Seq("-i", s.getAbsolutePath))
        .fold(Seq())(_ ++ _) ++ Seq(waveform.getAbsolutePath)
      ScaledaIdeaLogger.info(s"Starting RVCD with command line: ${cmdLine.mkString(" ")}")

      myProject match {
        case None    => throw new RuntimeException("No project is set for RvcdService")
        case Some(p) =>
          // run
          await(
            CommandRunner.executeAsync(
              Seq(CommandDeps(args = cmdLine, description = "Start RVCD Instance")),
              new StdErrToInfoHandler(p, RvcdHandler.getId, "Rvcd")
            )
          )
      }
    } else {
      client.openFileWith(
        rvcd.rvcd.RvcdOpenFileWith.of(waveform.getAbsolutePath, "", source.map(f => f.getAbsolutePath), None)
      )
    }
  }

  override def dispose(): Unit = {
    shutdown()
    myProject = null
  }
}

object RvcdService {
  final val rvcdFile = new File(Paths.getBinaryDir, "rvcd" + (if (OS.isWindows) ".exe" else ""))

  final def hasInstance: Boolean = {
    var (client, shutdown) = Rvcd()
    val ret = {
      try {
        try {
          client.ping(RvcdEmpty.of())
          true
        } catch {
          case _e: io.grpc.StatusRuntimeException =>
            shutdown()
            val r = Rvcd()
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
    shutdown()
    ret
  }
}
