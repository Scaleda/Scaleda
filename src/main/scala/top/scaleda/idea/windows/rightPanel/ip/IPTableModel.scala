package top.scaleda
package idea.windows.rightPanel.ip

import idea.ScaledaBundle
import kernel.toolchain.Toolchain

import com.intellij.util.ui.{ColumnInfo, ListTableModel}

import java.lang.{Boolean => JBoolean}

final class IPTableModel
    extends ListTableModel[IP](
      Array(
        new ColumnInfo[IP, String](ScaledaBundle.message("windows.ip.name")) {
          override def valueOf(item: IP): String = item.config.exports.get.name

          override def getPreferredStringValue: String = "SuperIP" // Why do we need this?
        },
        new ColumnInfo[IP, String](ScaledaBundle.message("windows.ip.vendor")) {
          override def valueOf(item: IP): String =
            Toolchain.toolchains.get(item.config.exports.get.vendor).map(_._1).getOrElse("Generic")
          override def getPreferredStringValue: String = "Generic"
        },
        new ColumnInfo[IP, String](ScaledaBundle.message("windows.ip.repository")) {
          override def valueOf(item: IP): String       = if (item.library) "Library" else "Project"
          override def getPreferredStringValue: String = "Library"
        }
      ) ++
        Toolchain.toolchains
          .map(toolchain => {
            new IPTableModel.BooleanColumnInfo(toolchain._2._1) {
              override def valueOf(item: IP): JBoolean = {
                val supports = item.config.exports.get.getSupports
                supports.contains(toolchain._1) || supports.contains("generic")
              }
            }
          })
          .toArray: _*
    )

object IPTableModel {
  private abstract class BooleanColumnInfo(name: String) extends ColumnInfo[IP, JBoolean](name) {
    override final def getColumnClass: Class[JBoolean] = classOf[JBoolean]
    override final def getPreferredStringValue         = "0"
  }
}
