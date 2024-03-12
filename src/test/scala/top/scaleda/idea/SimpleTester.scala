package top.scaleda
package idea

import idea.windows.tasks.ScaledaRunWindowFactory
import idea.windows.tool.message.ScaledaMessageRenderer.fileRegex

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

  it should "test patten replace" in {
    val source = "set project_name { waterfall } { test-source }  { dest }"
    val patten = "\\{ ([^\\{\\}]+) \\}"
    val p      = Pattern.compile(patten)
    val m      = p.matcher(source)
    val sb     = new StringBuffer()
    while (m.find()) {
      println(m.group(), m.group(1))
      m.appendReplacement(sb, "{" + m.group(1) + "}")
    }
    val result = m.appendTail(sb).toString
    println(result)
  }

  it should "test filepath:number" in {
    // val patten = Pattern.compile(ScaledaMessageRenderer.fileOptionalLineNumberRegexString)
    val patten =
      Pattern.compile("((\\w+:)*?[^\\s'\"/\\\\?:]*?/?((/[^&:]+/)|(\\\\[^&:]+\\\\))?)((\\w+)\\.(\\w+))(:(\\w+))?")
    val text =
      "/home/chiro/programs/scaleda-sample-project/.sim/Icarus-Run iverilog simulation/tb_waterfall_generated.v:83: error: Unknown module type: xmlx" +
        "/test/file/no/num.txt here" +
        "/ysuifasiof/fdsajflsajflsda/fasd.v:12331:123" +
        "/yfuisadhfs:fdasfds/dsafa.v" +
        "//fdsa//fdsa//a.v:123"
    val m = patten.matcher(text)
    while (m.find()) {
      println(m.group())
      for (i <- 0 to m.groupCount()) {
        println(s"$i: ${m.group(i)}")
      }
      println("------")
    }
  }

  it should "test match level word" in {
    // default is NOT case-insensitive
    // val patten = Pattern.compile("\\s?(info|warn|warning|error|panic)\\s?")
    val patten = Pattern.compile("\\b(info|warn|warning|error|panic)\\b", Pattern.CASE_INSENSITIVE)
    val text = "error here\n" +
      "should not match small_error\n" +
      "should match error\n" +
      "BIG WARN\n" +
      "end info"
    val m = patten.matcher(text)
    while (m.find()) {
      println(
        m.group(1),
        m.start(1),
        m.end(1),
        text.slice(0, m.start(1)) + "[" + text.slice(m.start(1), m.end(1)) + "]" + text.slice(m.end(1), text.length)
      )
      println(m.groupCount())
    }
  }

  it should "get class path" in {
    println(getClass.getName)
    println(ScaledaRunWindowFactory.getClass.getName)
  }
}
