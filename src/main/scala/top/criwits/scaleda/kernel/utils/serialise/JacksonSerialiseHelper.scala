package top.criwits.scaleda
package kernel.utils.serialise

import com.fasterxml.jackson.databind.ObjectMapper

import java.io.{BufferedWriter, File, FileWriter}

trait JacksonSerialiseHelper {
  val mapper: ObjectMapper

  /**
   * Convert string into an object with given class type
   *
   * @param string The data string
   * @param clazz  The target class type
   * @tparam T The inherited class type (by providing `clazz` it is implicit)
   * @return
   */
  def apply[T](string: String, clazz: Class[T]): T = mapper.readValue[T](string, clazz)

  /**
   * Convert file into an object with given class type
   *
   * @param file  The data file
   * @param clazz The target class type
   * @tparam T The inherited class type (by providing `clazz` it is implicit)
   * @return
   */
  def apply[T](file: File, clazz: Class[T]): T = mapper.readValue[T](file, clazz)

  def apply(value: Any): String = mapper.writeValueAsString(value)

  def apply(value: Any, file: File): Unit = {
    val writer = new BufferedWriter(new FileWriter(file))
    val text = apply(value)
    writer.write(text)
    writer.close()
  }
}