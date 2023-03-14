package top.criwits.scaleda
package idea.waveform
import idea.rvcd.RvcdService

import com.intellij.openapi.project.Project

import java.io.File

object RvcdHandler extends WaveformHandler {
  override def handle(project: Project, waveformPath: File): Unit = {
    // TODO / FIXME: Source is not given
    project
      .getService(classOf[RvcdService])
      .launchWithWaveformAndSource(waveformPath, Seq.empty)
  }

  override def getId: String = "rvcd"
}
