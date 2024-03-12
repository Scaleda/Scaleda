package top.scaleda
package kernel.utils.serialise

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object XMLHelper extends JacksonSerialiseHelper {
  override val mapper = XmlMapper
    .builder()
    .addModule(DefaultScalaModule)
    .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .build()
}
