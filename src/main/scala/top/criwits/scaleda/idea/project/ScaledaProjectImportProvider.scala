package top.criwits.scaleda
package idea.project

import com.intellij.projectImport.ProjectImportProvider

final class ScaledaProjectImportProvider extends ProjectImportProvider(new ScaledaProjectImportBuilder) {
  override def getFileSample: String = "Scaleda project"
  // stub
}
