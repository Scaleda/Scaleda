package top.scaleda
package idea.project.wizard

import idea.ScaledaBundle
import idea.project.ScaledaModuleBuilder

import com.intellij.ide.wizard.{AbstractNewProjectWizardStep, NewProjectWizardLanguageStep}
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder._
import kotlin.Unit.{INSTANCE => KUnit}

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
      TextFieldKt.bindText(row.textField().align(Align.FILL), sourceDirectoryProperty)
      KUnit
    })

    builder.row(ScaledaBundle.message("module.test.directory"), (row: Row) => {
      row.layout(RowLayout.PARENT_GRID)
      TextFieldKt.bindText(row.textField().align(Align.FILL), testDirectoryProperty)
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
