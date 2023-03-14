package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.utils.{MainLogger, inReadAction, invokeLater}
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig

import com.intellij.icons.AllIcons
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent}
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFile}

import java.io.File

/** Action: reload Scaleda project config, with all its targets and tasks.
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
          if (!fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile) {
            searchedFile = Some(fileOrDir)
          }
          true
        })
    }

    if (searchedFile.isEmpty) {
      ProjectConfig.configFile = None
      ProjectConfig.projectBase = None
      MainLogger.info(
        "No available Scaleda config (scaleda.yml) found under this project. This is not a Scaleda project"
      )

      // clear the model
      ScaledaRunWindowFactory.model.foreach(m => {
        m.setRoot(null) // legal
        m.reload()
      })
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

      // invokeLater {
      // Refresh tree panel while model valid
      val th = new Thread(() => {
        try {
          var done = false
          while (!done) {
            ScaledaRunWindowFactory.model
              .map(m => {
                invokeLater {
                  m.setRoot(ScaledaRunWindowFactory.getRootNode)
                  m.reload()
                  // then expand all
                  ScaledaRunWindowFactory.expandAll.foreach(expandAll =>
                    ActionManager
                      .getInstance()
                      .tryToExecute(expandAll, null, null, null, false)
                  )
                }
                done = true
                MainLogger.info("refresh scaleda tree done")
              })
              .getOrElse({
                done = false
              })
            Thread.sleep(100)
          }
        } catch {
          case _e: InterruptedException =>
        }
      })
      // when all thread is daemon, system exits
      th.setDaemon(true)
      th.start()
    }
    // }
  }
}
