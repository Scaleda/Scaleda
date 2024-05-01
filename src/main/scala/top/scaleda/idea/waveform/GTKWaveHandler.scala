package top.scaleda
package idea.waveform
import idea.utils.OutputLogger.StdErrToInfoHandler
import kernel.shell.command.{CommandDeps, CommandRunner}

import com.intellij.openapi.project.Project

import java.io.File

object GTKWaveHandler extends WaveformHandler {
  override def getId: String = "gtkwave"

  /** Handle waveform file in this project, blocking ui thread
    *
    * @param project      project
    * @param waveformPath file
    */
  override def handle(project: Project, waveformPath: File, sourceFiles: Seq[File]): Unit = {
    CommandRunner.executeAsync(
      Seq(CommandDeps(Seq("gtkwave", waveformPath.getAbsolutePath))),
      new StdErrToInfoHandler(project, getId, "GTKWave")
    )
  }
}
