package top.criwits.scaleda
package kernel.shell.command

trait AbstractCommandRunner {
  def run: CommandOutputStream
  def doTerminate(): Unit = {}
  def canTerminate: Boolean = false
  def isTerminating: Boolean = false
  def isTerminated: Boolean = false
}
