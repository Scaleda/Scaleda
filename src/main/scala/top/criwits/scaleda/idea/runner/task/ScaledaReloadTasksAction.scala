package top.criwits.scaleda
package idea.runner.task

import com.intellij.icons.AllIcons
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.vfs.VirtualFileManager
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.windows.tasks.ScaledaRunWindowFactory
import top.criwits.scaleda.kernel.project.config.ProjectConfig

import java.io.File

class ScaledaReloadTasksAction extends AnAction(
  ScaledaBundle.message("windows.task.refresh"),
  ScaledaBundle.message("windows.task.refresh"),
  AllIcons.Actions.Refresh
){
  override def actionPerformed(e: AnActionEvent): Unit = {
    // TODO: seems buggy

    SaveAndSyncHandler.getInstance().refreshOpenFiles();
    VirtualFileManager.getInstance().refreshAndFindFileByNioPath(new File(ProjectConfig.configFile.get).toPath)

    // Refresh tree panel
    ScaledaRunWindowFactory.model.foreach(m => {
      m.setRoot(ScaledaRunWindowFactory.getRootNode)
      m.reload()
    })
  }
}
