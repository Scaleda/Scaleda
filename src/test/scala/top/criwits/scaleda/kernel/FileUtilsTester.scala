package top.criwits.scaleda
package kernel

import kernel.project.config.ProjectConfig
import kernel.utils.{KernelFileUtils, OS}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.{File, PrintWriter}

class FileUtilsTester extends AnyFlatSpec with should.Matchers {
  val testVerilog =
    """module li_tangDingZhen114514 #(
      |  parameter yiwu = 15,
      |  parameter xuebao = 1919810
      |) (
      |  input clk,
      |  input rst,
      |  output dingzhen
      |);
      |
      |  always @ (posedge clk or posedge rst) begin
      |    if (rst) dingzhen = 0;
      |    else dingzhen = 1;
      |  end
      |
      |endmodule
      |""".stripMargin

  val testFileName = "testLoad.v"
  val testFile     = new File(testFileName)

  behavior of "KernelFileUtils"

  it should "generate test verilogs" in {
    val printWriter = new PrintWriter(testFile)
    printWriter.write(testVerilog)
    printWriter.close()
  }

  it should "parse verilog module name" in {
    assert(KernelFileUtils.getModuleTitle(testFile).head == "li_tangDingZhen114514")
  }

  it should "delete test file" in {
    testFile.delete()
  }

  it should "converts between absolute paths and relative paths" in {
    // make backup so we can have continuous testing
    val bkp = ProjectConfig.projectBase
    if (OS.isWindows) {
      ProjectConfig.projectBase = Some("C:\\Coding\\my_project")
      println(KernelFileUtils.toAbsolutePath("\\Coding\\my_project\\src\\1.v"))
      println(KernelFileUtils.toAbsolutePath("C:\\Coding\\my_project\\src\\1.v"))
      println(KernelFileUtils.toAbsolutePath("src\\2.v"))
      println(KernelFileUtils.toAbsolutePath("src/3/4.v"))
      println(KernelFileUtils.toProjectRelativePath("src/5.v"))
      println(KernelFileUtils.toProjectRelativePath("src\\6.v"))
      println(KernelFileUtils.toProjectRelativePath("C:\\Coding\\my_project\\test\\2.v"))
      println(KernelFileUtils.toProjectRelativePath("C:/Coding"))
      println(KernelFileUtils.toProjectRelativePath("D:\\a.txt"))
    } else {
      // same tests on Linux
      ProjectConfig.projectBase = Some("/home/criwits/my_project")
      println(KernelFileUtils.toAbsolutePath("/home/criwits/my_project/src/1.v"))
      println(KernelFileUtils.toAbsolutePath("/home/criwits/my_project/src/2.v"))
      println(KernelFileUtils.toAbsolutePath("src/3.v"))
      println(KernelFileUtils.toAbsolutePath("src/4/5.v"))
      println(KernelFileUtils.toProjectRelativePath("src/6.v"))
      println(KernelFileUtils.toProjectRelativePath("src/7.v"))
      println(KernelFileUtils.toProjectRelativePath("/home/criwits/my_project/test/2.v"))
      println(KernelFileUtils.toProjectRelativePath("/home/criwits"))
      println(KernelFileUtils.toProjectRelativePath("/home/criwits/"))
      println(KernelFileUtils.toProjectRelativePath("/home/criwits/a.txt"))
    }
    // restore bkp to original place
    ProjectConfig.projectBase = bkp
  }

  it should "generate test files" in {
    Seq(
      new File("testProj/src/1.v"),
      new File("testProj/src/2.v"),
      new File("testProj/test/tb.v"),
      new File("testProj/another.v"),
      new File("another/an.v")
    ).foreach(f => {
      new File(f.getParent).mkdirs()
      f.createNewFile()
    })
  }

  it should "scan directory" in {
    ProjectConfig.projectBase = Some("testProj")
    println(KernelFileUtils.scanDirectory(Set("v"), new File("testProj")))
    println(
      KernelFileUtils.getAllProjectSourceFiles(projectConfig =
        ProjectConfig(
          source = "src/",
          sources = Seq(
            new File("another/an.v").getAbsolutePath,
            "another.v"
          ),
          test = "test"
        )
      )
    )
    println(
      KernelFileUtils.getAllTestFiles(projectConfig =
        ProjectConfig(
          source = "src/",
          sources = Seq(
            new File("another/an.v").getAbsolutePath,
            "another.v"
          ),
          test = "test"
        )
      )
    )
  }

  it should "remove test project" in {
    Seq(
      new File("testProj/src/1.v"),
      new File("testProj/src/2.v"),
      new File("testProj/src"),
      new File("testProj/test/tb.v"),
      new File("testProj/test"),
      new File("testProj/another.v"),
      new File("testProj"),
      new File("another/an.v"),
      new File("another")
    ).foreach(_.delete())
  }
}
