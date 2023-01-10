package top.criwits.scaleda
package kernel.template

import kernel.utils.{JsonHelper, KernelLogger}

import com.hubspot.jinjava.Jinjava
import org.apache.commons.io.{FileUtils, IOUtils}

import java.io.{File, PrintWriter}
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
    val curClassLoader = Thread.currentThread.getContextClassLoader
    try {
      Thread.currentThread.setContextClassLoader(this.getClass.getClassLoader)
      jin.get.render(template, c)
    } finally Thread.currentThread.setContextClassLoader(curClassLoader)
  }

  def renderResource(resourcePath: String, context: Map[String, Any]): String = {
    // val reader = try {
    //   scala.io.Source.fromResource(s"templates/${resourcePath}")
    // } catch {
    //   case _: Throwable => scala.io.Source.fromResource(s"/templates/${resourcePath}")
    // }
    // render(reader.getLines().mkString("\n"), context)
    val stream = getClass.getClassLoader.getResourceAsStream(s"templates/${resourcePath}")
    val s = IOUtils.toString(stream, "UTF-8")
    // render(Resources.toString(new URL(s"templates/${resourcePath}"), Charset.defaultCharset()), context)
    render(s, context)
  }

  def renderResourceTo(resourcePath: String, context: Map[String, Any], targetPath: String): Unit = {
    KernelLogger.info(s"render resource ${resourcePath} to ${targetPath}, context: ${JsonHelper(context)}")
    val f = new File(targetPath)
    // if (f.exists()) return
    FileUtils.touch(f)
    val printer = new PrintWriter(f)
    val result = renderResource(resourcePath, context)
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
