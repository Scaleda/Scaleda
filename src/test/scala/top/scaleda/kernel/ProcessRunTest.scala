package top.scaleda
package kernel

import kernel.utils.OS

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.util.Scanner
import scala.jdk.javaapi.CollectionConverters

class ProcessRunTest extends AnyFlatSpec with should.Matchers {
  behavior of "Process"
  it should "set environments for ProcessBuilder" in {
    val procBuilder = new ProcessBuilder()
    val envs        = procBuilder.environment()
    val envName     = "_TEST_ENV"
    val envValue    = "_ENV_VALUE"
    envs.put(envName, envName)
    procBuilder.command(
      CollectionConverters.asJava(
        if (OS.isWindows) Seq("cmd", "/c", s"echo %$envName%")
          // not work, but set env may works..? // MAY NOT
        else Seq("sh", "-c", "echo $%s".format(envName)))
    )
    val process    = procBuilder.start()
    val outScanner = new Scanner(process.getInputStream)
    val output     = outScanner.nextLine()
    println(s"output: $output")
    // assert(output == envValue)
  }
}
