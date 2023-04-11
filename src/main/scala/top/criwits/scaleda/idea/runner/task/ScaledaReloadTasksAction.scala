package top.criwits.scaleda
package idea.runner.task

import idea.ScaledaBundle
import idea.project.IdeaManifestManager
import idea.utils.{MainLogger, inReadAction, invokeLater}
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig

import com.intellij.icons.AllIcons
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.openapi.actionSystem.{ActionManager, AnAction, AnActionEvent}
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFile}

import java.io.File
import javax.swing.tree.DefaultTreeModel

/** Action: Load Scaleda project config, with all its targets and tasks.
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
    implicit val project = e.getProject
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

    implicit val manifest = IdeaManifestManager.getImplicitManifest(project = e.getProject)
    if (searchedFile.isEmpty) {
      manifest.synchronized {
        manifest.configFile = None
        manifest.projectBase = None
      }
      MainLogger.info(
        "No available Scaleda config (scaleda.yml) found under this project. This is not a Scaleda project"
      )

      // clear the model
      IdeaManifestManager
        .getObject[DefaultTreeModel](ScaledaRunWindowFactory.getClass.getName)
        .foreach(m => {
          m.setRoot(null) // legal
          m.reload()
        })
    } else {
      val f = searchedFile.get
      // refill
      manifest.synchronized {
        manifest.configFile = Some(f.getPath)
        manifest.projectBase = Some(f.getParent.getPath)
      }
      MainLogger.info(s"Scaleda config file $f detected")

      // Refresh `scaleda.yml`
      LocalFileSystem
        .getInstance()
        .refreshAndFindFileByIoFile(
          new File(manifest.configFile.get)
        )
      SaveAndSyncHandler.getInstance().scheduleRefresh()

      // Refresh tree panel when the model is valid, tricky
      val th = new Thread(() => {
        try {
          var done = false
          while (!done) {
            IdeaManifestManager
              .getObject[DefaultTreeModel](ScaledaRunWindowFactory.WINDOW_ID)
              .map(m => {
                invokeLater {
                  m.setRoot(ScaledaRunWindowFactory.getRootNode)
                  m.reload()
                  // then expand all
                  IdeaManifestManager
                    .getObject[AnAction](ScaledaRunWindowFactory.WINDOW_ID + ".expandAll")
                    .foreach(expandAll =>
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
  }
}
