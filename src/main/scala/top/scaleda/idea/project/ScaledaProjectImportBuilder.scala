// package top.scaleda
// package idea.project
//
// import com.intellij.openapi.module
// import com.intellij.openapi.module.ModifiableModuleModel
// import com.intellij.openapi.project.Project
// import com.intellij.openapi.roots.ui.configuration.ModulesProvider
// import com.intellij.packaging.artifacts.ModifiableArtifactModel
// import com.intellij.projectImport.ProjectImportBuilder
// import com.intellij.util.SmartList
// import top.scaleda.idea.utils.Icons
//
// import java.util
// import javax.swing.Icon
//
// class ScaledaProjectImportBuilder extends ProjectImportBuilder {
//   override def getName: String = "Scaleda"
//
//   override def getIcon: Icon = Icons.mainSmall
//
//   override def isMarked(element: Nothing): Boolean = true
//
//   override def setOpenProjectSettingsAfter(on: Boolean): Unit = {}
//
//   override def commit(project: Project, model: ModifiableModuleModel, modulesProvider: ModulesProvider, artifactModel: ModifiableArtifactModel): util.List[module.Module] = {
//     // stub
//     new SmartList[module.Module]()
//   }
// }
