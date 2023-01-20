package top.criwits.scaleda
package idea.project.wizard

import idea.project.ScaledaModuleBuilder

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.wizard.{AbstractNewProjectWizardStep, NewProjectWizardLanguageStep}
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import kotlin.Unit.{INSTANCE => KUnit}
import top.criwits.scaleda.idea.utils.{MainLogger, inWriteAction}

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

  // detect when Scaleda is selected
  parent.getPropertyGraph.afterPropagation({() =>
    if (parent.getLanguage == ScaledaNewProjectWizard.ScaledaLanguageText) {
      // do something for preparation, if need!
      // TODO
    }
    KUnit
  })

  override def setupProject(project: Project): Unit = {
    val builder = new ScaledaModuleBuilder
    builder.setName(moduleName)
    builder.setContentEntryPath(s"${projectRoot.toString}/$moduleName")
    builder.commit(project)
  }
}
