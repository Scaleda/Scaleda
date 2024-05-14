package top.scaleda
package idea.windows.rightPanel.project

import idea.ScaledaBundle
import idea.utils.{ChooseTopModuleListener, ProjectBrowserListener}
import idea.windows.rightPanel.ScaledaEditPanelWrapper
import kernel.project.ScaledaProject
import kernel.utils.KernelFileUtils

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.ui.DocumentAdapter
import org.jdesktop.swingx.prompt.PromptSupport
import top.scaleda.idea.windows.rightPanel.treeNodes.ScaledaTasksRootNode

import java.awt.{Color, Font}
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import scala.collection.mutable.ListBuffer

class ScaledaEditProjectPanelWrapper(val projectConfig: ScaledaTasksRootNode, setValid: => Unit)(implicit
    manifest: ScaledaProject
) extends ScaledaEditPanelWrapper {
  val inner = new ScaledaEditProjectPanel

  // initialise values
  inner.projectNameField.setText(projectConfig.name)
  inner.sourceField.setText(projectConfig.source)
  inner.testField.setText(projectConfig.test)
  inner.topModuleField.setText(projectConfig.topModule.getOrElse(""))
  //noinspection DuplicatedCode
  if (projectConfig.constraintsEditable)
    inner.constraintsField.setText(projectConfig.constraints.headOption.getOrElse(""))
  else {
    inner.constraintsField.setEnabled(false)
    inner.constraintsField.setText(ScaledaBundle.message("windows.edit.edit_config_file"))
    // PromptSupport.setPrompt(
    //   ScaledaBundle.message("windows.edit.edit_config_file"),
    //   inner.constraintsField.getTextField
    // )
    // PromptSupport.setFontStyle(Font.ITALIC, inner.constraintsField.getTextField)
    // PromptSupport.setBackground(new Color(0, 0, 0, 0), inner.constraintsField.getTextField)
  }
  inner.constraintsField.setEnabled(projectConfig.constraintsEditable)

  inner.sourceField.addBrowseFolderListener(
    new ProjectBrowserListener(FileChooserDescriptorFactory.createSingleFolderDescriptor())
  )
  inner.testField.addBrowseFolderListener(
    new ProjectBrowserListener(FileChooserDescriptorFactory.createSingleFolderDescriptor())
  )
  inner.constraintsField.addBrowseFolderListener(
    new ProjectBrowserListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor())
  )
  inner.selectTopModuleButton.addActionListener(new ChooseTopModuleListener(inner.topModuleField))

  def updateValue(): Unit = {
    projectConfig.name = inner.projectNameField.getText
    projectConfig.source = inner.sourceField.getText
    projectConfig.test = inner.testField.getText
    projectConfig.topModule = if (inner.topModuleField.getText.nonEmpty) Some(inner.topModuleField.getText) else None
    if (projectConfig.constraintsEditable)
      projectConfig.constraints =
        if (inner.constraintsField.getText.nonEmpty)
          Seq(inner.constraintsField.getText)
        else Seq()
    checkValue
  }

  def checkValue: Boolean = {
    setValid
    inner.statusLabel.clear()
    val messages = new ListBuffer[String]
    if (!KernelFileUtils.isLegalName(projectConfig.name))
      messages.addOne(ScaledaBundle.message("windows.edit.project.illegal.name"))
    if (projectConfig.source.isBlank /* todo: check exist */ )
      messages.addOne(ScaledaBundle.message("windows.edit.project.need.src"))
    if (projectConfig.test.isBlank /* todo: above */ )
      messages.addOne(ScaledaBundle.message("windows.edit.project.need.test"))

    if (messages.nonEmpty) {
      inner.statusLabel.setIcon(AllIcons.General.BalloonError)
      inner.statusLabel.append(messages.mkString("; "))
      false
    } else {
      inner.statusLabel.setIcon(AllIcons.General.InspectionsOK)
      inner.statusLabel.append(ScaledaBundle.message("windows.edit.ok"))
      true
    }
  }

  checkValue

  inner.projectNameField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = updateValue()
  })

  inner.sourceField.getTextField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = updateValue()
  })
  inner.testField.getTextField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = updateValue()
  })
  inner.topModuleField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = updateValue()
  })
  inner.constraintsField.getTextField.getDocument.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = updateValue()
  })

  def getPanel: JPanel = inner.mainPanel
}
