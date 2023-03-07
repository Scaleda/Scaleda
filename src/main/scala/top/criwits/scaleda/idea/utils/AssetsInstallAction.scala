package top.criwits.scaleda
package idea.utils

import idea.ScaledaBundle
import kernel.bin.ExtractAssets

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.{Project, ProjectManager}

class AssetsInstallAction(project: Project) extends AnAction {
  override def actionPerformed(e: AnActionEvent) = {
    ProgressManager
      .getInstance()
      .runProcessWithProgressSynchronously(
        new Runnable {
          override def run(): Unit = ExtractAssets.run()
        },
        ScaledaBundle.message("utils.installing.assets"),
        false,
        project
      )
  }
}

class AssetsInstallManuallyAction extends AnAction {
  override def actionPerformed(e: AnActionEvent) = {
    val project = ProjectManager.getInstance().getDefaultProject
    new AssetsInstallAction(project).actionPerformed(e)
  }
}
