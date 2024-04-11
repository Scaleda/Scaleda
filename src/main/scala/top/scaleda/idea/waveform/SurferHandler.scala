package top.scaleda
package idea.waveform

import idea.utils.OutputLogger.StdErrToInfoHandler
import kernel.shell.command.{CommandDeps, CommandRunner}

import com.intellij.openapi.project.Project
import top.scaleda.kernel.utils.{OS, Paths}

import java.io.File

object SurferHandler extends WaveformHandler {
  override def getId: String = "surfer"

  /** Handle waveform file in this project, blocking ui thread
    *
    * @param project      project
    * @param waveformPath file
    */
  override def handle(project: Project, waveformPath: File, sourceFiles: Seq[File]): Unit = {
    CommandRunner.executeAsync(
      Seq(
        CommandDeps(
          Seq(
            new File(Paths.getBinaryDir, "surfer" + (if (OS.isWindows) ".exe" else "")).getAbsolutePath,
            waveformPath.getAbsolutePath
          )
        )
      ),
      new StdErrToInfoHandler(project)
    )
  }
}
