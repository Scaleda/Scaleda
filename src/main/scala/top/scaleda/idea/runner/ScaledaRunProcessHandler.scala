package top.scaleda
package idea.runner

import idea.ScaledaBundle
import idea.utils.{MainLogger, OutputLogger}
import idea.windows.tool.message.ScaledaMessageRenderer
import kernel.shell.ScaledaRunHandler
import kernel.shell.command.CommandDeps
import kernel.utils.{BasicLogger, FileReplaceContext}

import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.project.Project
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

import java.io.OutputStream
import scala.collection.mutable.ArrayBuffer

/** Handle a task process locally or remotely
  *
  * @param logger            using logger
  * @param rt                Runtime of this run
  * @param invokeAfterFinish will emit with [[ScaledaRuntime]] after `finishedAll`
  */
class ScaledaRunProcessHandler(
    project: Project,
    logger: BasicLogger,
    val rt: ScaledaRuntime,
    invokeAfterFinish: (ScaledaRuntime, Seq[Int], Boolean, Boolean) => Unit
) extends ProcessHandler
    with ScaledaRunHandler {
  // Set terminating <- `true` to invoke stopping
  var terminating = false
  // terminated will be set `true` after process really terminated
  var terminated = false

  val returnValues = ArrayBuffer[Int]()

  /** Called when destroy button clicked
    */
  override def destroyProcessImpl(): Unit = {
    MainLogger.warn(
      s"destroyProcessImpl, running: $terminated, stopping: $terminating"
    )
    terminating = true
    // notifyProcessTerminated(returnValues.headOption.getOrElse(0))
  }

  override def detachProcessImpl(): Unit = {
    MainLogger.warn("detachProcessImpl")
    notifyProcessDetached()
  }

  override def detachIsDefault(): Boolean = false

  override def isProcessTerminated: Boolean = terminated

  override def isProcessTerminating: Boolean = terminating

  // Not in use
  private val outputStream = new OutputStream {
    override def write(i: Int): Unit = MainLogger.warn("getProcessInput:", i)
  }
  override def getProcessInput: OutputStream = outputStream

  override def onShellCommand(command: CommandDeps) =
    logger.debug("cd", s"\"${command.path}\"", "&&", command.args.map(s => s"\"$s\"").mkString(" "))

  private val outputLogger = OutputLogger(project)

  override def onStepDescription(data: String): Unit = logger.debug(data)

  private def doContextReplace(src: String): String = {
    var now = src
    rt.context
      .get("replaceFiles")
      .map(_.asInstanceOf[Seq[FileReplaceContext]])
      .foreach(_.foreach(replace => {
        val m            = ScaledaMessageRenderer.fileOptionalLineNumberPattern.matcher(now)
        val stringBuffer = new StringBuffer()
        while (m.find()) {
          val line = Option(m.group(m.groupCount()))
          if (m.group(1).equals(replace.to.getAbsolutePath)) {
            m.appendReplacement(
              stringBuffer,
              replace.from.getAbsolutePath + line
                .map(line => {
                  val number =
                    (try Some(Integer.parseInt(line))
                    catch {
                      case e: NumberFormatException => None
                    }).map(n => if (n > replace.lineOffsetStart) n - replace.lineOffset else n)
                  number.map(":" + _.toString).getOrElse("")
                })
                .getOrElse("")
            )
          } else {
            m.appendReplacement(stringBuffer, m.group())
          }
        }
        now = m.appendTail(stringBuffer).toString
      }))
    now
  }

  override def onStdout(data: String): Unit = {
    logger.info(doContextReplace(data))
    outputLogger.info(doContextReplace(data))
  }

  override def onStderr(data: String): Unit = {
    logger.warn(doContextReplace(data))
    outputLogger.warn(doContextReplace(data))
  }

  override def onReturn(returnValue: Int, finishedAll: Boolean, meetErrors: Boolean): Unit = {
    val msg = ScaledaBundle.message("task.run.return.text", returnValue)
    if (meetErrors) {
      logger.warn(msg)
      outputLogger.warn(msg)
    } else {
      logger.debug(msg)
      outputLogger.debug(msg)
    }
    returnValues.append(returnValue)
    if (finishedAll || meetErrors) {
      terminating = false
      terminated = true
      // invoke only success all
      invokeAfterFinish(rt, returnValues.toSeq, finishedAll, meetErrors)
      notifyProcessTerminated(returnValue)
    }
  }

  override def isTerminating: Boolean = terminating
}
