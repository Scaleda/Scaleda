package top.scaleda.kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.scaleda.kernel.project.config.ProjectConfig
import top.scaleda.kernel.utils.serialise.YAMLHelper

class KernelTests extends AnyFlatSpec with should.Matchers {
  it should "read .env file" in {
    val env = top.scaleda.kernel.utils.EnvironmentUtils.Backup.env
    env should not be empty
    // for ((k, v) <- env) {
    //   println(s"$k=$v")
    // }
  }

  it should "read project config" in {
    // read from resources/test.scaleda.yml
    val ymlStream = getClass.getClassLoader.getResourceAsStream("test.scaleda.yml")
    val ymlContent = ymlStream.readAllBytes()
    val config = YAMLHelper(new String(ymlContent), classOf[ProjectConfig])
    // println(config)
    // println(config.targets.find(_.toolchain == "vivado").get.tasks.find(_.`type` == "programming").get.constraints)
    val target = config.targets.find(_.toolchain == "quartus").get
    println(target.getConstraints)
    println(target.constraintPaths)
    println(target.findTopModule)
    println(target.topModule)
  }
}
