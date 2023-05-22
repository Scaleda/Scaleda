// TODO: resolve resource leak

// package top.scaleda
// package kernel.net
//
// import kernel.net.fuse.FuseTransferServer
//
// import org.apache.commons.codec.digest.DigestUtils
//
// object RemoteResourceMonitor {
//   private def daemonReleaseFsProxies(): Unit = {
//     while (true) {
//       // search fs mount that serve exited command, umount it
//       val runningIdHashed = RemoteServer.commandThreadPool.synchronized {
//         RemoteServer.commandThreadPool.map(_._2._1).map(DigestUtils.sha256Hex)
//       }
//       val selectedFsProxies = FuseTransferServer.fsProxies.synchronized {
//         FuseTransferServer.fsProxies.filter(p => !runningIdHashed.exists(p._1.contains(_)))
//       }
//       Thread.sleep(100)
//     }
//   }
//
//   def startDaemon(): Unit = {
//     val thread = new Thread(() => {
//       val threads = Seq(() => daemonReleaseFsProxies()).map(r => new Thread(() => r()))
//       threads.foreach(_.setDaemon(true))
//       threads.foreach(_.start())
//       threads.foreach(_.join())
//     })
//     thread.setDaemon(true)
//     thread.start()
//   }
// }
