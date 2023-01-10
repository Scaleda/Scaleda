package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.template.Template
import top.criwits.scaleda.kernel.utils.KernelLogger

import scala.collection.immutable.Seq
import scala.jdk.javaapi.CollectionConverters

class TemplateTester extends AnyFlatSpec with should.Matchers {
  behavior of "Template"
  it should "test template loading from resources" in {
    Template.renderResourceTo("test.j2", Map(
      "test" -> "test data",
      "list" -> Seq("a", "b")
    ), "target/test.txt")
    val r = Template.getJin.get.render(
      """
        |test list: {% for i in list %} item:{{i}}{% endfor %}
        |test list2: {% for i in list2 %} item:{{i}}{% endfor %}
        |""".stripMargin, java.util.Map.of(
        "list", java.util.List.of("a", "b"),
        "list2", List("a", "b"),
      ))
    KernelLogger.warn(s"r = ${r}")
    val li = Seq("a", "b")
    KernelLogger.warn(s"li = ${li} => ${CollectionConverters.asJava(li)}")
  }
}
