package top.scaleda
package kernel.utils

import sourcecode.{File, Line, Name}

import scala.collection.mutable.ArrayBuffer

class KernelLogger

object KernelLogger extends BasicLogger {
  private val loggers = ArrayBuffer[BasicLogger]()

  def addLogger(l: BasicLogger) = loggers.addOne(l)

  def clearAllLoggers(): Unit = loggers.clear()

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    loggers.foreach(l => l.logging(level, xs: _*)(line, file, name))
  }
}
