// package top.criwits.scaleda
// package idea.runner
//
// import com.intellij.execution.configurations.GeneralCommandLine
// import com.intellij.execution.process.ProcessHandler
// import com.intellij.openapi.project.Project
// import top.criwits.scaleda.idea.windows.tool.ScaledaToolWindowFactory
//
// object Runner {
//   def runInConsole(
//                   project: Project,
//                   commandLine: GeneralCommandLine,
//                   showConsole: Boolean = true,
//                   showExitCode: Boolean = true
//                   ): ProcessHandler = {
//     val handler = new ConsoleProcessHandler(ScaledaToolWindowFactory.outputPanel(project).consoleView, commandLine, showExitCode)
//
//     if (showConsole) {
//       ScaledaToolWindowFactory.toolWindow(project).show(() => ScaledaToolWindowFactory.outputPanel(project).showPanel())
//     }
//
//     handler.startNotify()
//     handler
//   }
// }
