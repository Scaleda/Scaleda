package top.criwits.scaleda
package kernel.template

import kernel.utils.{ImplicitPathReplace, KernelLogger, NoPathReplace}

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
    } catch {
      case e: Throwable => KernelLogger.error(e)
    } finally Thread.currentThread.setContextClassLoader(curClassLoader)
  }

  def destroyJinja(): Unit = jin = None

  def getJin = jin

  def render(template: String, context: Map[String, Any])(implicit
      replace: ImplicitPathReplace = NoPathReplace
  ): String = {
    if (jin.isEmpty) initJinja()
    val c              = CollectionConverters.asJava(context)
    val curClassLoader = Thread.currentThread.getContextClassLoader
    try {
      Thread.currentThread.setContextClassLoader(this.getClass.getClassLoader)
      replace.doAllReplace(jin.get.render(template, c))
    } catch {
      case e: Throwable =>
        KernelLogger.error(e)
        throw e
    } finally Thread.currentThread.setContextClassLoader(curClassLoader)
  }

  def renderResource(resourcePath: String, context: Map[String, Any])(implicit
      replace: ImplicitPathReplace = NoPathReplace
  ): String = {
    val stream = getClass.getClassLoader.getResourceAsStream(s"templates/${resourcePath}")
    val s      = IOUtils.toString(stream, "UTF-8")
    render(s, context)
  }

  def renderResourceTo(resourcePath: String, context: Map[String, Any], targetPath: String)(implicit
      replace: ImplicitPathReplace = NoPathReplace
  ): Unit = {
    KernelLogger.info(s"render resource ${resourcePath} to ${targetPath}")
    val f = new File(targetPath)
    // if (f.exists()) return
    FileUtils.touch(f)
    val printer      = new PrintWriter(f)
    val renderResult = renderResource(resourcePath, context)
    printer.write(renderResult)
    printer.close()
  }
}

trait TemplateRenderer {
  def render(): Unit
}

abstract class ResourceTemplateRender(resourceBase: String, targetBase: String, resources: Map[String, String])(implicit
    replace: ImplicitPathReplace = NoPathReplace
) extends TemplateRenderer {
  def context: Map[String, Any]

  override def render(): Unit =
    resources
      .map(f => (s"${resourceBase}/${f._1}", s"${targetBase}/${f._2}"))
      .foreach(f => Template.renderResourceTo(f._1, context, f._2)(replace))

}
