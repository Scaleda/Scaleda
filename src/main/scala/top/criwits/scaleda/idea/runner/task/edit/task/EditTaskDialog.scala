package top.criwits.scaleda
package idea.runner.task.edit.task

import idea.runner.task.edit.EditDialog
import kernel.project.config.{TargetConfig, TaskConfig}

import com.intellij.ui.DocumentAdapter
import org.jdesktop.swingx.prompt.PromptSupport
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.kernel.toolchain.Toolchain

import java.awt.{BorderLayout, Color, Font}
import java.awt.event.ItemEvent
import javax.swing.JPanel
import javax.swing.event.{ChangeEvent, ChangeListener, DocumentEvent}

class EditTaskDialog(target: TargetConfig, config: Option[TaskConfig], parent: EditTaskDialogWrapper)
    extends JPanel
    with EditDialog[TaskConfig] {

  private val toolchainType = target.toolchain
  private val toolchain = Toolchain.toolchains(toolchainType) // FIXME: null?
  private val toolchainSupportedTaskList = TaskConfig.taskTypeList
    .filter(f => toolchain._3.contains(f._2._2)).toList

  // use border layout
  setLayout(new BorderLayout())

  /** MAIN PANEL */
  val panel = new EditTaskPanel
  add(panel.mainPanel, BorderLayout.CENTER)

  // add title
  panel.targetField.setText(target.name)
  panel.toolchainField.setText(toolchain._1)

  // add types to the combo box
  toolchainSupportedTaskList.foreach(f => panel.typeField.addItem(f._2._1))
  panel.typeField.setSelectedIndex(0) // set the first; may be overridden later;

  // add listeners
  panel.addDocumentListener(new DocumentAdapter {
    override def textChanged(e: DocumentEvent): Unit = {
       parent.updateOKStatus()
    }
  })

  panel.typeField.addItemListener((e: ItemEvent) => if (e.getStateChange == ItemEvent.SELECTED) {
     parent.updateOKStatus()

  })

  panel.presetField.addChangeListener((e: ChangeEvent) => {
    if (panel.presetField.isSelected) {
      panel.tclField.setEnabled(false)
    } else {
      panel.tclField.setEnabled(true)
    }
     parent.updateOKStatus()
  })

  // check toolchain type
  toolchainType match {
    case "iverilog" | "verilator" =>
      panel.presetField.setSelected(true)
      panel.presetField.setEnabled(false)
    case _ =>
  }

  private val parentTopModule = target.findTopModule
  private val parentTopModuleValid = parentTopModule.isDefined

  if (parentTopModuleValid) {
    PromptSupport.setPrompt(ScaledaBundle.message("windows.edit.task.inherited_top_module",
      parentTopModule.get),
      panel.topModuleField)
    //noinspection DuplicatedCode
    PromptSupport.setFontStyle(Font.ITALIC, panel.topModuleField)
    PromptSupport.setBackground(new Color(0, 0, 0, 0), panel.topModuleField)
  }

  config match {
    case Some(config) =>
      panel.nameField.setText(config.name)
      panel.topModuleField.setText(config.topModule.getOrElse(""))
      panel.typeField.setSelectedIndex(toolchainSupportedTaskList.zipWithIndex.filter(_._1._1 == config.`type`).head._2)
      panel.presetField.setSelected(config.preset)
      panel.tclField.setText(config.tcl.getOrElse(""))
    case None =>
      // Nothing
  }
  parent.updateOKStatus()

  override def getMainPanel: JPanel = this

  override def getData: TaskConfig = TaskConfig(
    panel.nameField.getText,
    toolchainSupportedTaskList(panel.typeField.getSelectedIndex)._1,
    if (panel.topModuleField.getText.isBlank) None else Some(panel.topModuleField.getText),
    panel.presetField.isSelected,
    if (panel.presetField.isSelected) None else Some(panel.tclField.getText)
  )

  override def validateConfig: Boolean =
    !panel.nameField.getText.isBlank &&
      (parentTopModuleValid || panel.topModuleField.getText.isBlank) &&
      (panel.presetField.isSelected || !panel.tclField.getText.isBlank)
}
