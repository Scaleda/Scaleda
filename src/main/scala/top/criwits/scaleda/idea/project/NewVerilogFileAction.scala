package top.criwits.scaleda
package idea.project

import idea.ScaledaBundle
import idea.utils.Icons
import idea.utils.template.VerilogFileTemplateUtil

import com.intellij.ide.actions.{CreateFileFromTemplateDialog, CreateFromTemplateAction}
import com.intellij.ide.fileTemplates.{FileTemplate, FileTemplateManager, FileTemplateUtil}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.{PsiDirectory, PsiFile}
import top.criwits.scaleda.kernel.utils.KernelFileUtils

import java.util.Properties

class NewVerilogFileAction extends CreateFromTemplateAction[PsiFile](
  ScaledaBundle.message("verilog.file"), null, Icons.verilog
) {
  override def createFile(name: String, templateName: String, dir: PsiDirectory): PsiFile = {
    val props = new Properties(FileTemplateManager.getInstance(dir.getProject).getDefaultProperties)
    props.setProperty(FileTemplate.ATTRIBUTE_NAME, name)
    val template = FileTemplateManager.getInstance(dir.getProject).getInternalTemplate(templateName)
    FileTemplateUtil.createFromTemplate(template, name, props, dir, classOf[NewVerilogFileAction].getClassLoader).getContainingFile
  }

  override def buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder): Unit = {
    builder.setTitle(ScaledaBundle.message("verilog.create.new.file"))
    VerilogFileTemplateUtil.getApplicableTemplates().foreach(template => {
      val templateName = template.getName
      val shortName = VerilogFileTemplateUtil.getTemplateShortName(templateName)
      val icon = Icons.verilogModule
      builder.addKind(shortName, icon, templateName)
    })

    builder.setValidator(new InputValidatorEx {
      override def getErrorText(inputString: String): String = if (!checkInput(inputString)) ScaledaBundle.message("verilog.invalid.file") else null
      override def checkInput(inputString: String): Boolean = inputString.nonEmpty && StringUtil.isJavaIdentifier(inputString)
      override def canClose(inputString: String): Boolean = !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null
    })
  }

  override def getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
    ScaledaBundle.message("verilog.create.new.file.with.name", newName)
}
