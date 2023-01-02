package top.criwits.scaleda
package kernel.toolchain

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.circe.yaml.parser
import top.criwits.scaleda.kernel.toolchain.Toolchain.toolchains
import top.criwits.scaleda.kernel.toolchain.ToolchainProfile
import top.criwits.scaleda.kernel.toolchain.executor.Executor
import top.criwits.scaleda.kernel.toolchain.impl.{IVerilog, PDS, Quartus, Verilator, Vivado}
import top.criwits.scaleda.kernel.utils.{KernelLogger, OS}

import java.io.{File, FilenameFilter}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
 * A [[Toolchain]] is an external software used to perform simulation, synthesis and implementation.
 * Applied toolchains, like [[Vivado]] or [[Quartus]], extend this abstract base class.
 * @param executor An [[Executor]] used to hold information like configurations.
 */
abstract class Toolchain(val executor: Executor) {
  def getInternalID: String
  def getName: String

  def simulate(): Unit = ???
  def synthesise(): Unit = ???
  def implement(): Unit = ???

}

object Toolchain {
  /**
   * All valid toolchains are listed here, with their corresponding implementation class
   */
  val toolchains: Map[String, Executor => Toolchain] = Map(
    Vivado.internalID -> ((executor: Executor) => new Vivado(executor)),
    Quartus.internalID -> ((executor: Executor) => new Quartus(executor)),
    PDS.internalID -> ((executor: Executor) => new PDS(executor)),
    Verilator.internalID -> ((executor: Executor) => new Verilator(executor)),
    IVerilog.internalID -> ((executor: Executor) => new IVerilog(executor)),
  )

  /**
   * Profiles for different toolchains, loaded by [[ToolchainProfileLoader]]
   */
  val profiles: ListBuffer[ToolchainProfile] = new ListBuffer[ToolchainProfile]

  val defaultConfigDirectory: String = OS.getUserHome + "/.scaleda/toolchains"

  /**
   * Load toolchain profiles from given directory
   * @param path Directory to save profiles
   */
  def loadToolchainProfiles(path: String = defaultConfigDirectory): Unit = {
    val mapper = YAMLMapper.builder()
      .addModule(DefaultScalaModule)
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build()

    KernelLogger.info(s"Toolchain profiles directory: ${path}")

    val files = new File(path).listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.matches("(\\w+)-(.+)\\.yml")
    }).filter(f => toolchains.keys.toList.contains(f.getName.split("-").head))

    files.foreach(f => {
      KernelLogger.debug(s"Processing config file ${f.getName}")
      val newItem = f.getName.split("-").head match {
        case Vivado.internalID => mapper.readValue[Vivado.Profile](f, classOf[Vivado.Profile])
        case Verilator.internalID => mapper.readValue[Verilator.Profile](f, classOf[Verilator.Profile])
        case Quartus.internalID => mapper.readValue[Quartus.Profile](f, classOf[Quartus.Profile])
        case PDS.internalID => mapper.readValue[PDS.Profile](f, classOf[PDS.Profile])
        case IVerilog.internalID => mapper.readValue[impl.IVerilog.Profile](f, classOf[IVerilog.Profile])
      }
      newItem.file = f
      profiles += newItem
    })

    KernelLogger.info(s"${profiles.size} profiles loaded")
  }

}