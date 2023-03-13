package top.criwits.scaleda
package kernel

import idea.windows.tool.message.ScaledaMessageRenderer

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import sourcecode.Name

import java.util.regex.Pattern

class SimpleTester extends AnyFlatSpec with should.Matchers {
  it should "test message pattern" in {
    val p = Pattern.compile("(INFO|WARNING|ERROR): \\[(.+)\\] ?(.+)")
    val text =
      """INFO: [Common 17-1381] The checkpoint '/home/chiro/programs/scaleda-sample-project/.synth/Vivado Synth/waterfall/waterfall.runs/synth_1/waterfall.dcp' has been generated."""
    val m = p.matcher(text)
    assert(m.find())
    println(m.group())
    (0 to m.groupCount).map(i => println(s"$i: ${m.group(i)}"))
  }

  it should "test path replace on windows" in {
    val path     = "D:\\Programs\\scaleda-sample-project\\test\\waterfall_tb.v"
    val replaced = path.replace('\\', '/')
    println(s"replaced: $replaced")
  }

  it should "test file matches" in {
    import ScaledaMessageRenderer._
    val text =
      "INFO: [Common 17-1381] The checkpoint '/home/chiro/programs/scaleda-sample-project/.synth/Vivado-Vivado Synth/waterfall/waterfall.runs/synth_1/waterfall.dcp' has been generated. && Run output will be captured here: /home/chiro/programs/scaleda-sample-project/.synth/Vivado-Vivado Synth/waterfall/waterfall.runs/synth_1/runme.log" +
        " C:\\Scaleda\\a.txt - caleda.vv"
    val fileMatches = fileRegex.findAllMatchIn(text).toSeq.sortBy(d => d.start - d.end)
    fileMatches.foreach(p => println("[" + p + "]"))
    println("slice:", "0123456789".slice(0, 1))
  }

  it should "test get function name" in {
    def getFunctionName(implicit name: Name): String = {
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
}
