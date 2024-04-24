package top.scaleda.kernel.utils

import org.slf4j.LoggerFactory
import sourcecode.{File, Line, Name}

object ScaledaShellLogger extends BasicLogger {
  val logger = LoggerFactory.getLogger("scaleda-kernel")

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg  = f"[${getSourceInfoText}] $args"
    level match {
      case Debug => logger.debug(msg)
      case Info  => logger.info(msg)
      case Warn  => logger.warn(msg)
      case _     => logger.error(msg)
    }
  }
}
