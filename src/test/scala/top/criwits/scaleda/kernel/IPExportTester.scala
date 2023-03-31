package top.criwits.scaleda

package kernel
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.kernel.project.config.ProjectConfig
import top.criwits.scaleda.kernel.template.Template
import top.criwits.scaleda.kernel.utils.TemplateContextReplace
import top.criwits.scaleda.kernel.utils.serialise.YAMLHelper

class IPExportTester extends AnyFlatSpec with should.Matchers {
  behavior of "IP export"
  it should "test export project parse" in {
    val text =
      """
name: scaleda-ip-project-clk_wiz
exports:
  name: Xilinx PLL IP
  module: clk_wiz_0
  options:
    - name: clk_in_freq
      type: float
      default: 50.0
    - name: clk_out_1_freq
      type: float
      default: 100.0
  actions:
    all:
      tcl:
      - 'create_ip -name clk_wiz -vendor xilinx.com -library ip -version 6.0 -module_name {{module}}'
      - 'set_property -dict
        [list
        CONFIG.PRIM_IN_FREQ { {{clk_in_freq}} }
        CONFIG.CLKIN1_JITTER_PS {500.0}
        CONFIG.MMCM_CLKFBOUT_MULT_F {50.000}
        CONFIG.MMCM_CLKIN1_PERIOD {50.000}
        CONFIG.MMCM_CLKIN2_PERIOD {10.0}
        CONFIG.CLKOUT1_JITTER {258.391}
        CONFIG.CLKOUT1_PHASE_ERROR {301.005}]
        [get_ips {{module}}]'
"""
    val project = YAMLHelper(text, classOf[ProjectConfig])
    project should not be null
    project.name should be("scaleda-ip-project-clk_wiz")
    // project.description should be("test project")
    // project.version should be("0.1")
    // project.author should be("criwits")
    project.exports.foreach(ex => {
      ex.name should be("Xilinx PLL IP")
      ex.module should be("clk_wiz_0")
      ex.options should not be null
      ex.options.length should be(2)
      ex.options(0).name should be("clk_in_freq")
      ex.options(0).`type` should be("float")
      ex.options(1).name should be("clk_out_1_freq")
      ex.options(1).`type` should be("float")
      ex.actions should not be null
      ex.actions.size should be(1)
      ex.actions("all").tcl should not be null
      ex.actions("all").tcl.size should be(2)
      ex.actions("all").tcl.foreach(tcl => {
        println(tcl)
        val r = Template.render(tcl, ex.getContextMap())(TemplateContextReplace)
        println(r)
      })
      // export.actions("all").tcl.head should be("puts \"hello world\"")
      // export.actions("all").tcl(1) should be("puts \"hello world2\"")
      // ex.defaults should not be null
      // ex.defaults.head._1 should be("clk_in_freq")
      // ex.defaults.head._2 should be(50.0)
      // export.defines should not be null
      // export.defines.length should be(2)
      // export.defines(0).define should be("test1")
      // export.defines(0).value should be("test1")
      // export.defines(0).`type` should be("string")
      // export.defines(0).description should be("")
      // export.defines(1).define should be("test2")
      // export.defines(1).value should be("test2")
      // export.defines(1).`type` should be("string")
      // export.defines(1).description should be("")
    })
  }
}
