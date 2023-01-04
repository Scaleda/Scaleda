package top.criwits.scaleda
package kernel.utils

object LogLevel extends Enumeration {
  val Debug = Value(2)
  val Info = Value(3)
  val Warn = Value(4)
  val Error = Value(4)
  val Critical = Value(5)
}

trait AbstractLogger {
  def log(msg: String, level: LogLevel.Value): Unit

  def debug(msg: String): Unit = log(msg, LogLevel.Debug)

  def info(msg: String): Unit = log(msg, LogLevel.Info)

  def warn(msg: String): Unit = log(msg, LogLevel.Warn)

  def error(msg: String): Unit = log(msg, LogLevel.Error)

  def critical(msg: String): Unit = log(msg, LogLevel.Critical)
}
