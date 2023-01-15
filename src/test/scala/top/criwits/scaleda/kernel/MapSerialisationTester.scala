package top.criwits.scaleda
package kernel

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.utils.serialise.YAMLHelper

@JsonInclude(Include.NON_EMPTY)
case class TestClass(a: Int, extraOptions: Option[Map[String, String]])

class MapSerialisationTester extends AnyFlatSpec with should.Matchers {
  def behaviour = behavior


  behaviour of "Map"
  it should "test serialisation" in {

    val testA = TestClass(1, Some(Map("a" -> "b", "c" -> "d")))
    val testB = TestClass(2, None)
    println(YAMLHelper(testA))
    println(YAMLHelper(testB))
  }

  it should "test deserialisation" in {
    val testC =
      """---
        |a: 2
        |""".stripMargin

    val deserialised = YAMLHelper(testC, classOf[TestClass])

    println(deserialised.extraOptions)
  }
}
