package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig

import com.intellij.icons.AllIcons
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent}
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFile, VirtualFileManager}
import top.criwits.scaleda.idea.utils.{MainLogger, inReadAction}

import java.io.File

/**
 * Action: reload Scaleda project config, with all its targets and tasks.
 * This action should be performed when
 *
 *  - Start up;
 *  - User press the reload button;
 *  - Config was generated;
 *  - Target / task was added;
 */
class ScaledaReloadTasksAction
    extends AnAction(
      ScaledaBundle.message("windows.task.refresh"),
      ScaledaBundle.message("windows.task.refresh"),
      AllIcons.Actions.Refresh
    ) {
  override def actionPerformed(e: AnActionEvent): Unit = {
    // Detect config file
    var searchedFile: Option[VirtualFile] = None
    inReadAction {
      ProjectFileIndex
        .getInstance(e.getProject)
        .iterateContent(fileOrDir => {
          if (
            !fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile
          ) {
            searchedFile = Some(fileOrDir)
          }
          true
        })
    }
    if (searchedFile.isEmpty) {
      MainLogger.warn("No available Scaleda config (scaleda.yml) found under this project. This is not a Scaleda project")
    } else {
      val f = searchedFile.get
      // refill
      ProjectConfig.configFile = Some(f.getPath)
      ProjectConfig.projectBase = Some(f.getParent.getPath)
      MainLogger.info(s"Scaleda config file $f detected")

      LocalFileSystem
        .getInstance()
        .refreshAndFindFileByIoFile(
          new File(ProjectConfig.configFile.get)
        )
      SaveAndSyncHandler.getInstance().scheduleRefresh()

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
}
