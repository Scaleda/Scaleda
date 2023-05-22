package top.scaleda
package kernel.utils.serialise

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JSONHelper extends JacksonSerialiseHelper {
  override val mapper = JsonMapper
    .builder()
    .addModule(DefaultScalaModule)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .build()
}
