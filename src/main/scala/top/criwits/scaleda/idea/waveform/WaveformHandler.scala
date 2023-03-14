package top.criwits.scaleda
package idea.waveform

import com.intellij.openapi.project.Project

import java.io.File

trait WaveformHandler {
  def getId: String
  def handle(project: Project, waveformPath: File): Unit
}

object WaveformHandler {
  def availableHandlers: Map[String, WaveformHandler] = Map(
    RvcdHandler.getId -> RvcdHandler
  )
  def apply(id: String) = availableHandlers.get(id)
}