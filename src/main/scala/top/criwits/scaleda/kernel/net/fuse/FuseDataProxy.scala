package top.criwits.scaleda
package kernel.net.fuse

import kernel.net.fuse.fs.RemoteFuseGrpc.RemoteFuseBlockingClient

import top.criwits.scaleda.kernel.net.fuse.fs.{EmptyReq, GetAttrReply, IntReply, PathModeRequest, PathRequest, ReadReply, ReadRequest, ReaddirReply, ReaddirRequest, StringTupleReply, TuplePathRequest, WriteRequest}

class FuseDataProxy extends RemoteFuseBlockingClient {
  override def init(request: EmptyReq): EmptyReq = ???

  override def destroy(request: EmptyReq): EmptyReq = ???

  override def getattr(request: PathRequest): GetAttrReply = ???

  override def readlink(request: PathRequest): StringTupleReply = ???

  override def mkdir(request: PathModeRequest): IntReply = ???

  override def unlink(request: PathRequest): IntReply = ???

  override def rmdir(request: PathRequest): IntReply = ???

  override def symlink(request: TuplePathRequest): IntReply = ???

  override def rename(request: TuplePathRequest): IntReply = ???

  override def chmod(request: PathModeRequest): IntReply = ???

  override def read(request: ReadRequest): ReadReply = ???

  override def write(request: WriteRequest): IntReply = ???

  override def readdir(request: ReaddirRequest): ReaddirReply = ???

  override def create(request: PathModeRequest): IntReply = ???
}
