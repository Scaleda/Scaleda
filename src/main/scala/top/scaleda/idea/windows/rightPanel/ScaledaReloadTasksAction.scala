package top.scaleda
package idea.windows.rightPanel

import idea.ScaledaBundle
import idea.project.io.YmlRootManager

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}

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
    val project = e.getProject
    if (project == null || project.isDisposed) return

    val service = project.getService(classOf[ScaledaTasksService])
    if (service.getPanel.isEmpty) return // todo: in this case, should we create a new panel?
    val panel = service.getPanel.get

    val ymlRoot = YmlRootManager.getInstance(project).getRoots

    panel.getRoots.clear()
    panel.getRoots.addAll(ymlRoot.flatMap(_.toProjectConfig.map(config => new ScaledaRunRootNode(config))))
  }
}
