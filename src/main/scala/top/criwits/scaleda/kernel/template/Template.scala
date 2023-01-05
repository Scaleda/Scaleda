package top.criwits.scaleda
package kernel.template

import com.hubspot.jinjava.Jinjava
import org.apache.commons.io.FileUtils
import top.criwits.scaleda.kernel.utils.{JsonHelper, KernelLogger}

import java.io.{File, PrintWriter}
import scala.jdk.javaapi.CollectionConverters

object Template {
  private val jin = new Jinjava()

  def getJin = jin

  def render(template: String, context: Map[String, Any]): String = {
    val c = CollectionConverters.asJava(context)
    println(s"context: ${context} => ${c}")
    jin.render(template, c)
  }

  def renderResource(resourcePath: String, context: Map[String, Any]): String =
    render(scala.io.Source.fromResource(s"templates/${resourcePath}").getLines().mkString("\n"), context)

  def renderResourceTo(resourcePath: String, context: Map[String, Any], targetPath: String) = {
    KernelLogger.info(s"render resource ${resourcePath} to ${targetPath}, context: ${JsonHelper(context)}")
    val result = renderResource(resourcePath, context)
    val f = new File(targetPath)
    FileUtils.touch(f)
    val printer = new PrintWriter(f)
    printer.write(result)
    printer.close()
  }
}

trait TemplateRenderer {
  def render(): Unit
}

abstract class ResourceTemplateRender
(resourceBase: String, targetBase: String, resources: Map[String, String]) extends TemplateRenderer {
  def context: Map[String, Any]

  override def render(): Unit =
    resources.map(f => (s"${resourceBase}/${f._1}", s"${targetBase}/${f._2}"))
      .foreach(f => Template.renderResourceTo(f._1, context, f._2))

}

object TemplateTest extends App {
  Template.renderResourceTo("test.j2", Map(
    "test" -> "test data",
    "list" -> Seq("a", "b")
  ), "target/test.txt")
  val r = Template.getJin.render(
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