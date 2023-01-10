package top.criwits.scaleda
package kernel.utils

object LogLevel extends Enumeration {
  val Debug = Value(1)
  val Verbose = Value(2)
  val Info = Value(3)
  val Warn = Value(4)
  val Error = Value(5)
  val Fatal = Value(6)
}

trait BasicLogger {

  import LogLevel._

  private val levelTextWithColor = Seq(
    "DEBUG",
    "VERB",
    Console.GREEN + "INFO" + Console.RESET,
    Console.YELLOW + "WARN" + Console.RESET,
    Console.RED + "ERROR" + Console.RESET,
    Console.BLACK + Console.RED_B + "FATAL" + Console.RESET
  )
  private val levelText = Seq(
    "DEBUG",
    "INFO",
    "WARN",
    "ERROR",
    "FATAL"
  )

  // private var loggingLevel = Debug
  private var loggingLevel = Info
  private var loggingEnabled = true
  private var colorEnabled = true
  private var pathEnabled = true

  def logLevel = loggingLevel

  protected final def stdoutWithNewLine[T](msg: String, xs: T*) = printf((if (msg.endsWith("\n")) msg.slice(0, msg.length - 1) else msg) + "\n", xs: _*)

  protected final def getLevelText(level: LogLevel.Value): String =
    if (colorEnabled) levelTextWithColor(level.id - Debug.id)
    else levelText(level.id - Debug.id)

  protected final def getSourceInfoText(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): String =
    (if (pathEnabled) f"${file.value.split("/").last.split("\\\\").last}:${line.value} " else "") + name.value

  protected final def logStdout[T](level: LogLevel.Value, xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name) =
    stdoutWithNewLine(f"${getLevelText(level)} [${getSourceInfoText}] ", xs: _*)

  def setLevel(l: LogLevel.Value) = loggingLevel = l

  def setEnable(functionName: String, e: Boolean) = functionName match {
    case "logging" => loggingEnabled = e
    case "color" => colorEnabled = e
    case "path" => pathEnabled = e
    case _ => {}
  }

  def log[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Verbose, xs: _*)

  def debug[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Debug, xs: _*)

  def info[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Info, xs: _*)

  def warn[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Warn, xs: _*)

  def error[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Error, xs: _*)

  def fatal[T](xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    loggingFilter(Fatal, xs: _*)

  def printStackTrace(cls: Class[_]): Unit = {
    val elements = (new Throwable).getStackTrace
    info("Stack for " + cls.getName + ":")
    elements.foreach(element => {
      info(element.getClassName +
        "." + element.getMethodName +
        "(" + element.getFileName +
        ":" + element.getLineNumber +
        ")")
    })
  }

  protected final def loggingFilter[T](level: LogLevel.Value, xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name) =
    if (level >= logLevel && loggingEnabled) logging(level, xs: _*)(line, file, name)


  def logging[T](level: LogLevel.Value, xs: T*)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit =
    logStdout(level, xs: _*)(line, file, name)
}
