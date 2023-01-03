package top.criwits.scaleda
package kernel.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.{BufferedWriter, File, FileWriter}

object YAMLHelper {
  private val mapper = YAMLMapper.builder()
    .addModule(DefaultScalaModule)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .build()

  /**
   * Convert YAML file into an object with given class type
   * @param file The YAML file
   * @param clazz The target class type
   * @tparam T The inherited class type (by providing `clazz` it is implicit)
   * @return
   */
  def apply[T](file: File, clazz: Class[T]): T = mapper.readValue[T](file, clazz)

  def apply(value: Any): String = mapper.writeValueAsString(value)

  def apply(value: Any, file: File): Unit = {
    val writer = new BufferedWriter(new FileWriter(file))
    writer.write(apply(value))
    writer.close()
  }
}
