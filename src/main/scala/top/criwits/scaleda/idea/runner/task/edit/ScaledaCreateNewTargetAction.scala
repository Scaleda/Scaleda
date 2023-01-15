 package top.criwits.scaleda
 package idea.runner.task.edit

 import idea.ScaledaBundle

 import com.intellij.icons.AllIcons
 import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
 import com.intellij.openapi.project.Project
 import top.criwits.scaleda.kernel.project.config.TargetConfig

 class ScaledaCreateNewTargetAction(project: Project)
     extends AnAction(
       ScaledaBundle.message("tasks.action.run.tool.window.create.target.name"),
       ScaledaBundle.message(
         "tasks.action.run.wool.window.create.target.description"
       ),
       AllIcons.Actions.NewFolder
     ) {
   override def actionPerformed(e: AnActionEvent) = {
     val _r = new EditTargetDialogWrapper(project, None).showAndGet()
   }
 }
