package top.criwits.scaleda
package kernel.utils

import com.typesafe.scalalogging.Logger

class KernelLogger

object KernelLogger {
  val logger: Logger = Logger("Kernel")

  def info(msg: String): Unit = logger.info(msg)
  def debug(msg: String): Unit = logger.info(msg)
}
