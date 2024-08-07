package top.scaleda
package idea.utils

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

import javax.swing.Icon

object Icons {
  private def getIcon(path: String): Icon = IconLoader.getIcon(path, Icons.getClass)

  final val mainRaw = IconLoader.getIcon("/icons/icon-raw", Icons.getClass)
  final val mainRawPlain =
    IconLoader.getIcon("/icons/icon-raw-plain", Icons.getClass)
  final val mainSmall = IconLoader.getIcon("/icons/icon-small", Icons.getClass)
  final val mainSmallDark =
    IconLoader.getIcon("/icons/icon-small_dark", Icons.getClass)
  final val mainWithName =
    IconLoader.getIcon("/icons/icon-with-name", Icons.getClass)
  final val mainWithNamePlain =
    IconLoader.getIcon("/icons/icon-with-name-plain", Icons.getClass)
  final val folder = IconLoader.getIcon("/icons/icon-over-folder", Icons.getClass)

  final val project = AllIcons.Actions.ModuleDirectory
  final val target  = AllIcons.Actions.RunAll
  final val task    = AllIcons.Actions.Execute

  final val verilog       = IconLoader.getIcon("/icons/verilog", Icons.getClass)
  final val verilogModule = getIcon("/icons/verilog-module")
  final val verilogWire   = getIcon("/icons/verilog-wire")
  final val verilogReg    = getIcon("/icons/verilog-reg")
  final val verilogAlways = getIcon("/icons/verilog-always")

  final val verilogInput     = AllIcons.Nodes.Interface
  final val verilogOutput    = getIcon("/icons/verilog-output")
  final val verilogInout     = getIcon("/icons/verilog-inout")
  final val verilogOutputReg = getIcon("/icons/verilog-output-reg")

  final val tcl = IconLoader.getIcon("/icons/tcl", Icons.getClass)
  final val toolWindow =
    IconLoader.getIcon("/icons/scaledaToolWindow", Icons.getClass)
  final val logVerbose =
    IconLoader.getIcon("/icons/log-verbose", Icons.getClass)

  final val systemVerilog = IconLoader.getIcon("/icons/systemverilog", Icons.getClass)

  final val bluespec = AllIcons.Actions.Execute
}
