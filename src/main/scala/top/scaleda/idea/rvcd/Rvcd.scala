package top.scaleda.idea.rvcd

import rvcd.rvcd.RvcdRpcGrpc
import top.scaleda.idea.utils.IdeaRpcPatch

import scala.language.existentials

object Rvcd {
  private final val DEFAULT_PORT = 5411
  def apply(host: String = "127.0.0.1", port: Int = DEFAULT_PORT) =
    IdeaRpcPatch.getClient(RvcdRpcGrpc.blockingStub, host, port)
}
