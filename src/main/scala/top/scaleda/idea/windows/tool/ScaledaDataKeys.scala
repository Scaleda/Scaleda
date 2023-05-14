package top.scaleda
package idea.windows.tool

import com.intellij.openapi.actionSystem.DataKey

object ScaledaDataKeys {
  val LOG_SOURCE_ID = DataKey.create[String]("ScaledaLogConsole_SourceId")
}
