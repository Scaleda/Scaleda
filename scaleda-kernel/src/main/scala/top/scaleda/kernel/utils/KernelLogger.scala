package top.scaleda
package kernel.utils

import org.slf4j.LoggerFactory
import sourcecode.{File, Line, Name}

import scala.collection.mutable.ArrayBuffer

class KernelLogger

object KernelLogger extends BasicLogger {
  val logger = LoggerFactory.getLogger("scaleda-kernel")
  private val others = ArrayBuffer[BasicLogger]()

  def append(l: BasicLogger) = others.addOne(l)

  def clearOthers(): Unit = others.clear()

  override def logging[T](level: LogLevel.Value, xs: T*)(implicit line: Line, file: File, name: Name) = {
    import LogLevel._
    val args = xs.mkString(" ")
    val msg = f"[${getSourceInfoText}] $args"
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    others.foreach(l => l.logging(level, xs: _*)(line, file, name))
  }
}
