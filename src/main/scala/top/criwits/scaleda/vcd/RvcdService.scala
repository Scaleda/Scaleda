package top.criwits.scaleda
package vcd

import kernel.rvcd.RvcdClient

import com.intellij.openapi.Disposable

class RvcdService extends Disposable {
  val stub = RvcdClient("localhost")
  override def dispose() = {}
}
