package top.criwits.scaleda
package kernel.rvcd

import com.intellij.openapi.Disposable
import rvcd.rvcd.RvcdEmpty

class RvcdService extends Disposable {
  val stub = RvcdClient("127.0.0.1")
  override def dispose() = {}
}

object RvcdServiceTest extends App {
  val service = new RvcdService
  service.stub.ping(RvcdEmpty.of())
}
