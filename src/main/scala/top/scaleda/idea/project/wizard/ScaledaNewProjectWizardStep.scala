package top.scaleda
package idea.project.wizard

import idea.project.ScaledaModuleBuilder

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.wizard.{AbstractNewProjectWizardStep, NewProjectWizardLanguageStep}
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.ui.dsl.builder.{Panel, Row, RowLayout, TextFieldKt}
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import kotlin.Unit.{INSTANCE => KUnit}
import top.scaleda.idea.ScaledaBundle
import top.scaleda.idea.utils.{MainLogger, inWriteAction}

import javax.swing.JTextField
import scala.jdk.CollectionConverters.MapHasAsJava

/** Class for project wizard step, or call it configuration?
  * @param parent NewProjectWizardLanguageStep
  */
class ScaledaNewProjectWizardStep(private val parent: NewProjectWizardLanguageStep)
    extends AbstractNewProjectWizardStep(parent) {
  // basic info
  private val moduleNameProperty: GraphProperty[String] = getPropertyGraph.lazyProperty(() => parent.getName)
  private def moduleName: String = moduleNameProperty.get()
  private val projectRoot = getContext.getProjectDirectory.toAbsolutePath

  private val addSampleCode = getPropertyGraph.property(false)
  private val sourceDirectoryProperty: GraphProperty[String] = getPropertyGraph.property("src")
  private val testDirectoryProperty: GraphProperty[String] = getPropertyGraph.property("test")

  // detect when Scaleda is selected
  parent.getPropertyGraph.afterPropagation({() =>
    if (parent.getLanguage == ScaledaNewProjectWizard.ScaledaLanguageText) {
      // do something for preparation, if need!
      // TODO
    }
    KUnit
  })

  override def setupUI(builder: Panel): Unit = {
    builder.row(ScaledaBundle.message("module.source.directory"), (row: Row) => {
      row.layout(RowLayout.PARENT_GRID)
      TextFieldKt.bindText(row.textField().horizontalAlign(HorizontalAlign.FILL), sourceDirectoryProperty)
      KUnit
    })

    builder.row(ScaledaBundle.message("module.test.directory"), (row: Row) => {
      row.layout(RowLayout.PARENT_GRID)
      TextFieldKt.bindText(row.textField().horizontalAlign(HorizontalAlign.FILL), testDirectoryProperty)
      KUnit
    })
  }


  override def setupProject(project: Project): Unit = {
    val builder = new ScaledaModuleBuilder(sourceDirectoryProperty.get(), testDirectoryProperty.get())
    builder.setName(moduleName)
    builder.setContentEntryPath(s"${projectRoot.toString}/$moduleName")
    builder.commit(project)
  }
}
