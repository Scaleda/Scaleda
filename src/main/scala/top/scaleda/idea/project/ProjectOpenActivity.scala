package top.scaleda
package idea.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.startup.StartupActivity.DumbAware
import com.intellij.openapi.vfs.VirtualFileManager
import top.scaleda.idea.ScaledaAppService
import top.scaleda.idea.project.io.{ScaledaVFSListener, YmlRootManager}
import top.scaleda.idea.utils.ScaledaIdeaLogger

/**
 * Runs startup actions just after a project is opened, before it's indexed.
 */
class ProjectOpenActivity extends StartupActivity with DumbAware {

  override def runActivity(project: Project): Unit = {
    if (project == null || project.isDisposed) return

    // init app services
    val appService = ApplicationManager.getApplication.getService(classOf[ScaledaAppService])
    appService.initLoggers()
    appService.initJinja()

    // subscribe to VFS events
    val vfsListener = new ScaledaVFSListener(project)
    project.getMessageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, vfsListener)

    // check if project is a scaleda project
    val ymlRootManager = YmlRootManager.getInstance(project)
    if (ymlRootManager.getRoots.isEmpty) {
      ScaledaIdeaLogger.info("Currently this project is not a scaleda project")
      return
    }

    // activate tool window
  }
}