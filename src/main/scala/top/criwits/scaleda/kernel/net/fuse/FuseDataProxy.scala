package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.RemoteFuseGrpc.RemoteFuseBlockingClient
import kernel.net.fuse.fs._

import sourcecode.Name

class FuseDataProxy(identifier: String) extends RemoteFuseBlockingClient {
  protected def makeRequest[TReq, TResp](data: TReq)(implicit function: Name): TResp = {
    val resp = FuseTransferServer.request(
      FuseTransferMessageCase(FuseTransferMessageCase.newMsgId, identifier, function.value, data)
    )
    if (resp.error.isDefined) {
      throw resp.error.get
    }
    resp.data.asInstanceOf[TResp]
  }

  override def init(request: EmptyReq): EmptyReq = makeRequest(request)

  // override def destroy(request: EmptyReq): EmptyReq = makeRequest(request)

  override def getattr(request: PathRequest): GetAttrReply = makeRequest(request)

  // override def readlink(request: PathRequest): StringTupleReply = makeRequest(request)

  override def mkdir(request: PathModeRequest): IntReply = makeRequest(request)

  override def unlink(request: PathRequest): IntReply = makeRequest(request)

  override def rmdir(request: PathRequest): IntReply = makeRequest(request)

  // override def symlink(request: TuplePathRequest): IntReply = makeRequest(request)

  override def rename(request: TuplePathRequest): IntReply = makeRequest(request)

  // override def chmod(request: PathModeRequest): IntReply = makeRequest(request)

  override def read(request: ReadRequest): ReadReply = makeRequest(request)

  override def write(request: WriteRequest): IntReply = makeRequest(request)

  override def readdir(request: ReaddirRequest): ReaddirReply = makeRequest(request)

  override def create(request: PathModeRequest): IntReply = makeRequest(request)

  override def truncate(request: PathOffsetRequest): IntReply = makeRequest(request)

  override def `open`(request: PathRequest): IntReply = makeRequest(request)

  override def release(request: PathRequest): IntReply = makeRequest(request)

  override def statfs(request: PathRequest): StatfsReply = makeRequest(request)
}

object GetFunctionNameTest extends App {
  private def getFunctionName(implicit name: Name): String = {
    val r = name.value
    println(r)
    r
  }

  def test()      = getFunctionName
  def testName()  = getFunctionName
  def testName2() = getFunctionName

  test()
  testName2()
  testName()
}
