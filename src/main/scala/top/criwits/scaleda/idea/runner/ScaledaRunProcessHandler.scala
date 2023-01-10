package top.criwits.scaleda
package idea.runner

import idea.utils.{ConsoleLogger, MainLogger}

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project

import java.io.OutputStream

class ScaledaRunProcessHandler(
    project: Project,
    console: ConsoleView
) extends ProcessHandler {
  var launched = false
  var stopped = false
  var stopping = false

  // val streamInput = new PipedInputStream()
  // // val streamOutput = new PipedOutputStream(streamInput)
  // val streamOutput = new PrintStream(streamInput)

  // val consoleViewBuilder =
  //   TextConsoleBuilderFactory.getInstance.createBuilder(project)
  // val console = consoleViewBuilder.getConsole

  // val searchScope =
  //   ExecutionSearchScopes.executionScope(project, environment.getRunProfile)
  // val myConsoleBuilder =
  //   TextConsoleBuilderFactory.getInstance.createBuilder(project, searchScope)
  // val console = myConsoleBuilder.getConsole

  val logger = new ConsoleLogger(console)

  val thread = new Thread(() => {
    launched = true
    while (!stopping) {
      Thread.sleep(300)
      // KernelLogger.warn("thread...")
      // streamInput
      // console.print("hi...\n", ConsoleViewContentType.NORMAL_OUTPUT)
      logger.info("hi", "there")
    }
    // console.dispose()
    // console.print("done.\n", ConsoleViewContentType.NORMAL_OUTPUT)
    logger.warn("well", "I'm", "finished")
    stopped = true
    stopping = false
  })
  thread.setDaemon(true)
  thread.start()

  override def destroyProcessImpl(): Unit = {
    MainLogger.warn(
      s"destroyProcessImpl, running: ${stopped}, stopping: ${stopping}"
    )
    // val t = new Thread(() => {
    //   stopping = true
    //   Thread.sleep(1000)
    //   stopping = false
    //   stopped = true
    // })
    // t.setDaemon(true)
    // t.start()
    stopping = true
  }

  override def detachProcessImpl(): Unit = {
    MainLogger.warn("detachProcessImpl")
  }

  override def detachIsDefault(): Boolean = {
    MainLogger.warn("detachIsDefault", false)
    false
  }

  override def isProcessTerminated = {
    MainLogger.warn("isProcessTerminated", stopped)
    stopped
  }

  override def isProcessTerminating = {
    MainLogger.warn("isProcessTerminating", stopping)
    stopping
  }

  override def getProcessInput: OutputStream = {
    // new OutputStream {
    //   override def write(i: Int) = MainLogger.warn("getProcessInput:", i)
    // }
    // streamOutput
    null
  }

}
