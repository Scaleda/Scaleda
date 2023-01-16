package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig

import com.intellij.icons.AllIcons
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.openapi.actionSystem.{
  ActionManager,
  AnAction,
  AnActionEvent
}
import com.intellij.openapi.vfs.VirtualFileManager

import java.io.File

class ScaledaReloadTasksAction
    extends AnAction(
      ScaledaBundle.message("windows.task.refresh"),
      ScaledaBundle.message("windows.task.refresh"),
      AllIcons.Actions.Refresh
    ) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    // TODO: seems buggy

    SaveAndSyncHandler.getInstance().refreshOpenFiles();
    VirtualFileManager
      .getInstance()
      .refreshAndFindFileByNioPath(
        new File(ProjectConfig.configFile.get).toPath
      )

    // Refresh tree panel
    ScaledaRunWindowFactory.model.foreach(m => {
      m.setRoot(ScaledaRunWindowFactory.getRootNode)
      m.reload()
      // then expand all
      ScaledaRunWindowFactory.expandAll.foreach(expandAll =>
        ActionManager
          .getInstance()
          .tryToExecute(expandAll, null, null, null, false)
      )
    })
  }
}
