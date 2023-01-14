package top.criwits.scaleda
package kernel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

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
}
