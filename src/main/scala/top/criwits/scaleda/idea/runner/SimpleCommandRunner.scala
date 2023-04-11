package top.criwits.scaleda
package idea.runner

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.{ExecutionException, Executor}
import com.intellij.ide.IdeBundle
import com.intellij.ide.actions.runAnything.execution.RunAnythingRunProfile
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.execution.ParametersListUtil

object SimpleCommandRunner {
  def runCommand(
      project: Project,
      workDirectory: VirtualFile,
      commandString: String
  ) = {
    val dataContext: DataContext = SimpleDataContext.builder().build()
    val executor: Executor       = DefaultRunExecutor.getRunExecutorInstance
    val initialCommandLine =
      new GeneralCommandLine(ParametersListUtil.parse(commandString, false, true))
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withWorkDirectory(workDirectory.getPath)

    val commandLine = initialCommandLine
    try {
      val runAnythingRunProfile = new RunAnythingRunProfile(
        commandLine,
        commandString
      )
      ExecutionEnvironmentBuilder
        .create(project, executor, runAnythingRunProfile)
        .dataContext(dataContext)
        .buildAndExecute()
    } catch {
      case e: ExecutionException =>
        Messages.showInfoMessage(project, e.getMessage, IdeBundle.message("run.anything.console.error.title"))
    }
  }
}
