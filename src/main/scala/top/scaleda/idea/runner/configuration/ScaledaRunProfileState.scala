package top.scaleda
package idea.runner.configuration

import com.intellij.execution.{ExecutionResult, Executor}
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.project.Project
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

class ScaledaRunProfileState(project: Project, runtime: ScaledaRuntime) extends RunProfileState{
  override def execute(executor: Executor, runner: ProgramRunner[_]): ExecutionResult = {

  }
}
