package top.criwits.scaleda
package idea.utils.template

import com.intellij.ide.fileTemplates.{FileTemplate, FileTemplateManager}
import com.intellij.openapi.util.Condition
import com.intellij.util.SmartList
import com.intellij.util.containers.ContainerUtil
import top.criwits.scaleda.verilog.VerilogFileType

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object VerilogFileTemplateUtil {
  val PREFIX = "Verilog "
  def getApplicableTemplates(): mutable.Seq[FileTemplate] = {
    getApplicableTemplates(new Condition[FileTemplate] {
      override def value(t: FileTemplate): Boolean =
        VerilogFileType.instance.getDefaultExtension == t.getExtension
    })
  }

  def getApplicableTemplates(filter: Condition[FileTemplate]): mutable.Seq[FileTemplate] = {
    val templates = new SmartList[FileTemplate]()
    templates.addAll(ContainerUtil.findAll(FileTemplateManager.getDefaultInstance.getInternalTemplates, filter))
    templates.addAll(ContainerUtil.findAll(FileTemplateManager.getDefaultInstance.getAllTemplates, filter))
    println(templates)
    templates.asScala
  }

  def getTemplateShortName(templateName: String) = {
    if (templateName.startsWith(PREFIX)) {
      templateName.substring(PREFIX.length)
    } else templateName
  }
}
