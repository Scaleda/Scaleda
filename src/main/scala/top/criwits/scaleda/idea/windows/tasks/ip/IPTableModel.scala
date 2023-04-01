package top.criwits.scaleda
package idea.windows.tasks.ip

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
            Toolchain.toolchains.get(item.config.exports.get.vendor).map(_._1).getOrElse("Genric")
          override def getPreferredStringValue: String = "Generic"
        },
        new ColumnInfo[IP, String](ScaledaBundle.message("windows.ip.repository")) {
          override def valueOf(item: IP): String       = item.path
          override def getPreferredStringValue: String = "/"
        }
      ) ++
        Toolchain.toolchains
          .map(toolchain => {
            new IPTableModel.BooleanColumnInfo(toolchain._2._1) {
              override def valueOf(item: IP): JBoolean = item.config.exports.get.getSupports.contains(toolchain._1)
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
