package top.criwits.scaleda
package idea.windows.tasks.ip

import idea.ScaledaBundle
import idea.windows.tasks.ip.ScaledaIPManagerPanel.createConfigEditor
import kernel.project.config.ProjectConfig

import com.intellij.lang.Language
import com.intellij.openapi.editor.colors.impl.DefaultColorsScheme
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.editor.{EditorFactory, EditorSettings}
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory.getSyntaxHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.components.{JBList, JBPanelWithEmptyText, JBTextField}
import com.intellij.ui._
import com.intellij.util.ui.{FormBuilder, JBUI}
import top.criwits.scaleda.idea.utils.inWriteAction

import java.awt.BorderLayout
import javax.swing.event.{ChangeEvent, ChangeListener, DocumentEvent, ListSelectionEvent}
import javax.swing._
import scala.collection.mutable

class ScaledaIPManagerPanel(val project: Project) extends JPanel(new BorderLayout) {
  private val projIP = ProjectConfig.projectBasicIps().map(s => IP(s._1, false, s._2))
  private val libIP  = ProjectConfig.libraryIps.map(s => IP(s._1, true, s._2))
  private val ipList = projIP ++ libIP
  // Left side, list
  private val listModel      = new DefaultListModel[IPInstance]
  private val ipInstanceList = new JBList[IPInstance](listModel)
  ipInstanceList.setCellRenderer(new MyCellRenderer)
  ipInstanceList.addListSelectionListener(onItemSelected) // TODO

  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)

  val listPanel: JPanel = ToolbarDecorator
    .createDecorator(ipInstanceList)
    .setAddAction((e: AnActionButton) => addIP(e))
    .setRemoveAction((_: AnActionButton) => {})
    .disableUpDownActions()
    .createPanel()

  splitter.setFirstComponent(listPanel)

  // Right side, default panel is a JBPanelWithEmptyText with 'No IP selected'
  val emptyPanel: JBPanelWithEmptyText = new JBPanelWithEmptyText()
    .withEmptyText(ScaledaBundle.message("windows.ip.no.ip.selected"))
    .withBorder(JBUI.Borders.emptyLeft(6))
  splitter.setSecondComponent(emptyPanel)

  // Editor ...
  var editor: EditorEx = _

  private def addIP(e: AnActionButton): Unit = {

    val r  = new ScaledaChooseIPDialog(project, ipList)
    val _r = r.showAndGet()

    val selectedIP = r.mySelectedIP
    selectedIP.foreach(ip => {
      listModel.addElement(
        new IPInstance(
          ip.config.exports.get.module,
          ip.config.exports.get.id,
          ip.config.exports.get.options.map(e => (e.name, e.default)).to(mutable.Map)
        )
      )
    })
  }

  private def onItemSelected(e: ListSelectionEvent): Unit = {
    val index = ipInstanceList.getSelectedIndex
    if (index >= 0 && index < listModel.size()) {
      val ip = listModel.get(index)
      loadItem(ip)
    }
  }

  private def loadItem(item: IPInstance): Unit = {
    // First dispose previous editor (required)
    if (editor != null) {
      EditorFactory.getInstance().releaseEditor(editor)
      editor = null
    }

    // check IP model
    val ips = ipList.filter(_.config.exports.get.id == item.typeId)
    if (ips.isEmpty) return // TODO: shall never reach here...
    val ip = ips.head

    // Prepare right panel
    val rpanel = new ScaledaIPEditPanel

    editor = createConfigEditor
    rpanel.previewPanel.setLayout(new BorderLayout)
    rpanel.previewPanel.add(editor.getComponent)

    def renderEditor: Unit = {
      inWriteAction {
        editor.getDocument.setText(
          ip.config.exports.get.renderStub(item.getRenderOptions)
        )
      }
    }

    rpanel.nameField.setText(item.module)
    rpanel.nameField.getDocument.addDocumentListener(new DocumentAdapter {
      override def textChanged(e: DocumentEvent): Unit = {
        item.module = rpanel.nameField.getText
        renderEditor
        ipInstanceList.repaint() // wow!
      }
    })

    // Build UI
    val form = FormBuilder
      .createFormBuilder()
      .setAlignLabelOnRight(false)

    ip.config.exports.get.options.foreach(option => {
      option.`type` match {
        case "string" =>
          val field = new JBTextField(item.options(option.name).asInstanceOf[String])
          field.getDocument.addDocumentListener(new DocumentAdapter {
            override def textChanged(e: DocumentEvent): Unit = {
              item.options.put(option.name, field.getText)
              renderEditor
            }
          })
          form.addLabeledComponent(option.name, field) // FIXME: Nls

        case "int" =>
          val spinnerModel = new SpinnerNumberModel(
            item.options(option.name).asInstanceOf[Int],
            null, // TODO in FUTURE: We can have max & min
            null,
            Integer.valueOf(1)
          )
          spinnerModel.addChangeListener(new ChangeListener {
            override def stateChanged(e: ChangeEvent): Unit = {
              item.options.put(option.name, spinnerModel.getValue.asInstanceOf[Int])
              renderEditor
            }
          })
          val spinner = new JSpinner(spinnerModel)
          form.addLabeledComponent(
            option.name,
            spinner
          ) // FIXME: Nls

        case "float" =>
          val spinnerModel = new SpinnerNumberModel(
            item.options(option.name).asInstanceOf[Double], // Should double?
            null,                                           // TODO in FUTURE: We can have max & min
            null,
            0.001d // TODO: Step?
          )
          spinnerModel.addChangeListener(new ChangeListener {
            override def stateChanged(e: ChangeEvent): Unit = {
              item.options.put(option.name, spinnerModel.getValue.asInstanceOf[Double])
              renderEditor
            }
          })
          val spinner = new JSpinner(spinnerModel)
          form.addLabeledComponent(
            option.name,
            spinner
          )

        case "boolean" =>
          val checkBox = new JCheckBox()
          checkBox.setSelected(item.options(option.name).asInstanceOf[Boolean])
          checkBox.addChangeListener(new ChangeListener {
            override def stateChanged(e: ChangeEvent): Unit = {
              item.options.put(option.name, checkBox.isSelected)
              renderEditor
            }
          })
          form.addLabeledComponent(option.name, checkBox)
        case _ =>
      }
    })

    rpanel.configPanel.setLayout(new BorderLayout)
    rpanel.configPanel.add(form.getPanel, BorderLayout.NORTH)

    renderEditor

    splitter.setSecondComponent(rpanel.mainPanel)

  }

  def toIPInstances: Seq[IPInstance] = {
    for (i <- 0 until listModel.size()) yield listModel.get(i)
  }

  private class MyCellRenderer extends ColoredListCellRenderer[IPInstance] {
    override def customizeCellRenderer(
        list: JList[_ <: IPInstance],
        value: IPInstance,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ): Unit = {
      append(value.module)
      append(
        " [" + ipList
          .filter(_.config.exports.get.id == value.typeId)
          .headOption
          .map(_.config.exports.get.name)
          .getOrElse("unknown") + "]",
        SimpleTextAttributes.GRAYED_ATTRIBUTES
      )
    }
  }
}

object ScaledaIPManagerPanel {
  //copied from CodeStyleAbstractPanel
  private def createConfigEditor: EditorEx = {
    val editorFactory  = EditorFactory.getInstance
    val editorDocument = editorFactory.createDocument("")
    val editor = editorFactory
      .createViewer(editorDocument)
      .asInstanceOf[EditorEx]

    val verilogLanguage = Language.findLanguageByID("Verilog")
    fillEditorSettings(editor.getSettings, verilogLanguage)

    if (verilogLanguage != null) {
      val highlighter = new LayeredLexerEditorHighlighter(
        getSyntaxHighlighter(verilogLanguage, null, null),
        new DefaultColorsScheme
      )
      editor.setHighlighter(highlighter)
    }

    editor
  }

  private[this] def fillEditorSettings(settings: EditorSettings, language: Language): Unit = {
    settings.setLanguageSupplier(() => language)
    settings.setWhitespacesShown(true)
    settings.setLineMarkerAreaShown(false)
    settings.setIndentGuidesShown(false)
    settings.setLineNumbersShown(false)
    settings.setFoldingOutlineShown(false)
    settings.setAdditionalColumnsCount(0)
    settings.setAdditionalLinesCount(1)
    settings.setUseSoftWraps(false)
  }
}
