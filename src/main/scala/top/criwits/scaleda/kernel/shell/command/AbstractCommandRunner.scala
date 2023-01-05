package top.criwits.scaleda
package kernel.shell.command

trait AbstractCommandRunner {
  def run: CommandOutputStream
}
