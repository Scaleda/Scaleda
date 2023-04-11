package top.criwits.scaleda
package kernel.shell

import kernel.database.UserException
import kernel.utils.KernelLogger

import java.lang.Thread.UncaughtExceptionHandler
import java.util.concurrent.TimeoutException

class ShellExceptionHandler extends UncaughtExceptionHandler {
  override def uncaughtException(thread: Thread, throwable: Throwable) = {
    throwable match {
      case e: TimeoutException =>
        KernelLogger.error(s"Timeout for ${e.getMessage}! Check your network connection.")
      case e: UserException =>
        KernelLogger.error(s"User Authorization check failed(${e.getMessage}). Register or re-login.")
      case e: Throwable =>
    }
    throwable.printStackTrace()
    throw throwable
  }
}
