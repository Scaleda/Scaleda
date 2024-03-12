package top.scaleda
package kernel.shell.command

trait AbstractCommandRunner {
  def run: CommandOutputStream
  def doTerminate(): Unit = {}
  // NOT USED
  def canTerminate: Boolean  = false
  def isTerminating: Boolean = false
  def isTerminated: Boolean  = false
}
