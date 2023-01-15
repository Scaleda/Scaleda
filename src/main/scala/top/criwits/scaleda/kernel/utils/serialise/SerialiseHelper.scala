package top.criwits.scaleda
package kernel.utils.serialise

import java.io.File

trait SerialiseHelper {
  def apply[T](string: String, clazz: Class[T]): T

  def apply[T](file: File, clazz: Class[T]): T

  def apply(value: Any): String

  def apply(value: Any, file: File): Unit
}