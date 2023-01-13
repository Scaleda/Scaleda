package top.criwits.scaleda
package idea.windows.tool.logging

import kernel.utils.{BasicLogger, LogLevel}

import com.intellij.openapi.Disposable

import scala.collection.mutable

class ScaledaLoggingService extends Disposable {
  private val listeners = new mutable.HashMap[String, BasicLogger]

  def addListener(sourceId: String, logger: BasicLogger) =
    listeners.put(sourceId, logger)

  def logging[T](sourceId: String, level: LogLevel.Value, xs: T*)(implicit
      line: sourcecode.Line,
      file: sourcecode.File,
      name: sourcecode.Name
  ): Unit =
    listeners.get(sourceId).foreach(_.logging(level, xs: _*))

  def stop(logSourceId: String): Unit = {}

  override def dispose() = {}
}
