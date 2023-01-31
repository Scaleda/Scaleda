package top.criwits.scaleda
package idea.utils

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
  final val verilog = IconLoader.getIcon("/icons/verilog", Icons.getClass)
  final val verilogModule = getIcon("/icons/verilog-module")
  final val tcl = IconLoader.getIcon("/icons/tcl", Icons.getClass)
  final val toolWindow =
    IconLoader.getIcon("/icons/scaledaToolWindow", Icons.getClass)
  final val logVerbose =
    IconLoader.getIcon("/icons/log-verbose", Icons.getClass)
}
