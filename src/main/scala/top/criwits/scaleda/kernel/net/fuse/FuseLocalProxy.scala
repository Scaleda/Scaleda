package top.criwits.scaleda
package kernel.net.fuse
import sourcecode.Name

class FuseLocalProxy(localObserver: FuseTransferClientObserver, identifier: String = "test")
    extends FuseDataProxy(identifier) {
  override protected def makeRequest[TReq, TResp](data: TReq)(implicit function: Name) = {
    val msg  = FuseTransferMessageCase(FuseTransferMessageCase.newMsgId, identifier, function.value, data)
    val resp = FuseTransferServer.localRequest(localObserver, msg)
    if (resp.error.isDefined) throw resp.error.get
    resp.data.asInstanceOf[TResp]
  }
}
