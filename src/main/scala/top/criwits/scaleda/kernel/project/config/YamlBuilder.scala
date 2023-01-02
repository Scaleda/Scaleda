package top.criwits.scaleda
package kernel.project.config

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.File

object YamlBuilder {
  private val mapper = YAMLMapper.builder()
    .addModule(DefaultScalaModule)
    .build()

  def apply[T](file: File, clazz: Class[T]): T = mapper.readValue[T](file, clazz)
}
