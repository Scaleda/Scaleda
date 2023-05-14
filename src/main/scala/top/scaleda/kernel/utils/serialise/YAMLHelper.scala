package top.scaleda
package kernel.utils.serialise

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.yaml.{YAMLGenerator, YAMLMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object YAMLHelper extends JacksonSerialiseHelper {
  override val mapper = YAMLMapper
    .builder()
    .addModule(DefaultScalaModule)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES) // quote is unnecessary
    .build()
}
