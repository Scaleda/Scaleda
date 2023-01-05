package top.criwits.scaleda
package kernel.utils

// import com.typesafe.scalalogging.Logger

import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class KernelLogger

object KernelLogger extends AbstractLogger {
  // val logger: Logger = Logger("Kernel")
  val logger = LoggerFactory.getLogger("scaleda")
  private val others = ArrayBuffer[AbstractLogger]()

  //noinspection DuplicatedCode
  override def log(msg: String, level: LogLevel.Value): Unit = {
    import LogLevel._
    level match {
      case Debug => logger.debug(msg)
      case Info => logger.info(msg)
      case Warn => logger.warn(msg)
      case _ => logger.error(msg)
    }
    others.foreach(l => l.log(msg, level))
  }

  def append(l: AbstractLogger) = others.addOne(l)
}
