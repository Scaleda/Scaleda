package top.scaleda
package idea.windows.rightPanel.task

import idea.ScaledaBundle
import idea.utils.{ChooseTopModuleListener, ProjectBrowserListener}
import idea.windows.rightPanel.ScaledaEditPanelWrapper
import kernel.project.ScaledaProject
import kernel.project.config.TaskConfig
import kernel.toolchain.Toolchain
import kernel.utils.KernelFileUtils

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.ui.{DocumentAdapter, SimpleColoredComponent}
import org.jdesktop.swingx.prompt.PromptSupport
import top.scaleda.idea.windows.rightPanel.treeNodes.{ScaledaTasksTargetNode, ScaledaTasksTaskNode}

import java.awt.event.ItemEvent
import java.awt.{BorderLayout, Color, Font}
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import scala.collection.mutable.ListBuffer

class ScaledaEditTaskPanelWrapper(val taskConfig: ScaledaTasksTaskNode, setValid: => Unit)(implicit
    manifest: ScaledaProject
) extends JPanel
    with ScaledaEditPanelWrapper {

  private val toolchainType = taskConfig.getParent.asInstanceOf[ScaledaTasksTargetNode].toolchain
  private val toolchain     = Toolchain.toolchains.get(toolchainType)
  private val toolchainSupportedTaskList = toolchain map { t =>
    TaskConfig.taskTypeList
      .filter(f => t._3.contains(f._2._2))
      .toList
  } getOrElse List()

  // use border layout
  setLayout(new BorderLayout())

  /** MAIN PANEL */
  val panel = new EditTaskPanel
  add(panel.mainPanel, BorderLayout.CENTER)

  val statusLabel = new SimpleColoredComponent
  add(statusLabel, BorderLayout.SOUTH)

  // add types to the combo box
  toolchainSupportedTaskList.foreach(f => panel.typeField.addItem(f._2._1))
  panel.typeField.setSelectedIndex(0) // set the first; may be overridden later;

  // set text before listeners available
  panel.nameField.setText(taskConfig.name)
  panel.topModuleField.setText(taskConfig.topModule.getOrElse(""))

  panel.typeField.setSelectedIndex(
    toolchainSupportedTaskList.zipWithIndex.find(_._1._1 == taskConfig.`type`).map(_._2).getOrElse(0)
  )
  //noinspection DuplicatedCode
  if (taskConfig.constraintsEditable)
    panel.constraintsField.setText(taskConfig.constraints.headOption.getOrElse(""))
  else {
    panel.constraintsField.setEnabled(false)
    panel.constraintsField.setText(ScaledaBundle.message("windows.edit.edit_config_file"))
    // PromptSupport.setPrompt(
    //   ScaledaBundle.message("windows.edit.edit_config_file"),
    //   panel.constraintsField.getTextField
    // )
    // PromptSupport.setFontStyle(Font.ITALIC, panel.constraintsField.getTextField)
    // PromptSupport.setBackground(new Color(0, 0, 0, 0), panel.constraintsField.getTextField)
  }

  panel.constraintsField.setEnabled(taskConfig.constraintsEditable)

  panel.constraintsField.addBrowseFolderListener(
    new ProjectBrowserListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor())
  )

  panel.chooseTopModuleButton.addActionListener(new ChooseTopModuleListener(panel.topModuleField))

  // add listeners
  panel.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = {
      taskConfig.name = panel.nameField.getText
      taskConfig.topModule = if (!panel.topModuleField.getText.isBlank) Some(panel.topModuleField.getText) else None
      if (taskConfig.constraintsEditable)
        taskConfig.constraints =
          if (!panel.constraintsField.getText.isBlank)
            Seq(panel.constraintsField.getText)
          else Seq()
      checkValue
    }
  })

  panel.typeField.addItemListener((e: ItemEvent) =>
    if (e.getStateChange == ItemEvent.SELECTED) {
      taskConfig.`type` = toolchainSupportedTaskList(panel.typeField.getSelectedIndex)._1
      checkValue
    }
  )
//
//  panel.presetField.addChangeListener((e: ChangeEvent) => {
//    if (panel.presetField.isSelected) {
//      panel.tclField.setEnabled(false)
//    } else {
//      panel.tclField.setEnabled(true)
//    }
//     parent.updateOKStatus()
//  })

//  // check toolchain type
//  toolchainType match {
//    case "iverilog" | "verilator" =>
//      panel.presetField.setSelected(true)
//      panel.presetField.setEnabled(false)
//    case _ =>
//  }

  private val parentTopModule: Option[String] = taskConfig.parent.flatMap(_.findTopModule)
  private val parentTopModuleValid            = parentTopModule.isDefined

  if (parentTopModuleValid) {
    PromptSupport.setPrompt(
      ScaledaBundle.message("windows.edit.task.inherited_top_module", parentTopModule.get),
      panel.topModuleField
    )
    //noinspection DuplicatedCode
    PromptSupport.setFontStyle(Font.ITALIC, panel.topModuleField)
    PromptSupport.setBackground(new Color(0, 0, 0, 0), panel.topModuleField)
  }

//
//  panel.chooseTopModuleButton.addActionListener(new ActionListener {
//    override def actionPerformed(e: ActionEvent): Unit = {
//      val selectModuleAction = new ScaledaSelectModuleAction(project)
//      ActionManager.getInstance().tryToExecute(selectModuleAction, null, null, null, true)
//      val module = selectModuleAction.module
//
//      if (module != null) {
//        val moduleName = module.getName
//        panel.topModuleField.setText(moduleName)
//      }
//    }
//  })
//
//  override def validateConfig: Boolean =
//    !panel.nameField.getText.isBlank &&
//      (parentTopModuleValid || !panel.topModuleField.getText.isBlank) &&
//      (panel.presetField.isSelected || !panel.tclField.getText.isBlank)

  override def getPanel: JPanel = this

  override def checkValue: Boolean = {
    setValid
    statusLabel.clear()
    val messages = new ListBuffer[String]

    if (!KernelFileUtils.isLegalName(taskConfig.name))
      messages.addOne(ScaledaBundle.message("windows.edit.task.illegal.name"))
    if (taskConfig.findTopModule.isEmpty) messages.addOne(ScaledaBundle.message("windows.edit.task.no.topmodule"))

    if (messages.nonEmpty) {
      statusLabel.setIcon(AllIcons.General.BalloonError)
      statusLabel.append(messages.mkString("; "))
      false
    } else {
      statusLabel.setIcon(AllIcons.General.InspectionsOK)
      statusLabel.append(ScaledaBundle.message("windows.edit.ok"))
      true
    }
  }
  checkValue
}
