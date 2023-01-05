package top.criwits.scaleda
package kernel.template

import com.google.common.io.Resources
import com.hubspot.jinjava.Jinjava
import org.apache.commons.io.FileUtils
import top.criwits.scaleda.kernel.utils.{JsonHelper, KernelLogger}

import java.io.{File, PrintWriter}
import java.net.URL
import java.nio.charset.Charset
import scala.jdk.javaapi.CollectionConverters

object Template {
  private var jin: Option[Jinjava] = None

  def initJinja(): Unit = {
    val curClassLoader = Thread.currentThread.getContextClassLoader
    try {
      Thread.currentThread.setContextClassLoader(this.getClass.getClassLoader)
      jin = Some(new Jinjava())
    } finally Thread.currentThread.setContextClassLoader(curClassLoader)
  }

  def getJin = jin

  def render(template: String, context: Map[String, Any]): String = {
    val c = CollectionConverters.asJava(context)
    println(s"context: ${context} => ${c}")
    jin.get.render(template, c)
  }

  def renderResource(resourcePath: String, context: Map[String, Any]): String = {
    // val reader = try {
    //   scala.io.Source.fromResource(s"templates/${resourcePath}")
    // } catch {
    //   case _: Throwable => scala.io.Source.fromResource(s"/templates/${resourcePath}")
    // }
    // render(reader.getLines().mkString("\n"), context)
    getClass.getClassLoader.getResourceAsStream(s"templates/${resourcePath}").toString
    // render(Resources.toString(new URL(s"templates/${resourcePath}"), Charset.defaultCharset()), context)
  }

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