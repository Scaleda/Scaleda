package top.criwits.scaleda
package kernel.database

class UserException(msg: String) extends Throwable {
  override def getMessage = msg
}