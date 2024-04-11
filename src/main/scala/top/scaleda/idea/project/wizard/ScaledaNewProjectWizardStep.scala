package top.scaleda
package idea.project.wizard

import idea.ScaledaBundle
import idea.project.ScaledaModuleBuilder
import kernel.utils.KernelFileUtils

import com.intellij.ide.wizard.{AbstractNewProjectWizardStep, NewProjectWizardStep}
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder._
import com.intellij.ui.layout.ValidationInfoBuilder
import kotlin.Unit.{INSTANCE => KUnit}

/** Class for project wizard step, or call it configuration?
 * @param parent NewProjectWizardLanguageStep
 */
class ScaledaNewProjectWizardStep(private val parent: NewProjectWizardStep)
  extends AbstractNewProjectWizardStep(parent) {
  // basic info
  // private val moduleNameProperty: GraphProperty[String] = getPropertyGraph.lazyProperty(() => parent.getName)
  private val moduleNameProperty: GraphProperty[String] = getPropertyGraph.lazyProperty(() => parent.toString)
  private def moduleName: String                        = moduleNameProperty.get()
  private val projectRoot                               = getContext.getProjectDirectory.toAbsolutePath

  private val addSampleCode                                  = getPropertyGraph.property(false)
  private val sourceDirectoryProperty: GraphProperty[String] = getPropertyGraph.property("src")
  private val testDirectoryProperty: GraphProperty[String]   = getPropertyGraph.property("test")

  // detect when Scaleda is selected
  parent.getPropertyGraph.afterPropagation({ () =>
    // if (parent.getLanguage == ScaledaNewProjectWizard.ScaledaLanguageText) {
    //   // do something for preparation, if need!
    //   // TODO
    // }
    KUnit
  })

  override def setupUI(builder: Panel): Unit = {
    val srcValidator: kotlin.jvm.functions.Function2[ValidationInfoBuilder, JBTextField, ValidationInfo] =
      (builder: ValidationInfoBuilder, text: JBTextField) => {
        validateSourceDirectory(builder, text)
      }

    val testValidator: kotlin.jvm.functions.Function2[ValidationInfoBuilder, JBTextField, ValidationInfo] =
      (builder: ValidationInfoBuilder, text: JBTextField) => {
        validateTestDirectory(builder, text)
      }

    builder.row(
      ScaledaBundle.message("module.source.directory"),
      (row: Row) => {
        row.layout(RowLayout.PARENT_GRID)
        TextFieldKt.bindText(
          row.textField().align(Align.FILL).validationOnInput(srcValidator).validationOnApply(srcValidator),
          sourceDirectoryProperty
        )
        KUnit
      }
    )

    builder.row(
      ScaledaBundle.message("module.test.directory"),
      (row: Row) => {
        row.layout(RowLayout.PARENT_GRID)
        TextFieldKt.bindText(
          row.textField().align(Align.FILL).validationOnInput(testValidator).validationOnApply(testValidator),
          testDirectoryProperty
        )
        KUnit
      }
    )
  }

  override def setupProject(project: Project): Unit = {
    val builder = new ScaledaModuleBuilder(sourceDirectoryProperty.get(), testDirectoryProperty.get())
    builder.setName(moduleName)
    builder.setContentEntryPath(s"${projectRoot.toString}/$moduleName")
    builder.commit(project)
  }

  private def validateSourceDirectory(builder: ValidationInfoBuilder, text: JBTextField): ValidationInfo = {
    val directory = text.getText
    if (directory.isBlank) {
      builder.error(ScaledaBundle.message("module.source.directory.empty"))
    } else if (!KernelFileUtils.isLegalName(directory)) {
      builder.error(ScaledaBundle.message("module.source.directory.invalid"))
    } else if (directory == testDirectoryProperty.get()) {
      builder.error(ScaledaBundle.message("module.source.directory.same"))
    } else {
      null
    }
  }

  private def validateTestDirectory(builder: ValidationInfoBuilder, text: JBTextField): ValidationInfo = {
    val directory = text.getText
    if (directory.isBlank) {
      builder.error(ScaledaBundle.message("module.test.directory.empty"))
    } else if (!KernelFileUtils.isLegalName(directory)) {
      builder.error(ScaledaBundle.message("module.test.directory.invalid"))
    } else if (directory == sourceDirectoryProperty.get()) {
      builder.error(ScaledaBundle.message("module.test.directory.same"))
    } else {
      null
    }
  }
}
