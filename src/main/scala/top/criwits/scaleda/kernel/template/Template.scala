package top.criwits.scaleda
package kernel.template

import com.hubspot.jinjava.Jinjava

import java.io.{File, PrintWriter}
import scala.jdk.javaapi.CollectionConverters

object Template {
  private val jin = new Jinjava()

  def render(template: String, context: Map[String, Any]): String = {
    val c = CollectionConverters.asJava(context)
    // println(s"context: ${c}")
    jin.render(template, c)
  }

  def renderResource(resourcePath: String, context: Map[String, Any]): String =
    render(scala.io.Source.fromResource(s"templates/${resourcePath}").getLines().mkString("\n"), context)

  def renderResourceTo(resourcePath: String, context: Map[String, Any], targetPath: String) = {
    val result = renderResource(resourcePath, context)
    val printer = new PrintWriter(new File(targetPath))
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
    "test" -> "test data"
  ), "target/test.txt")
}