package top.criwits.scaleda
package verilog.editor.formatter

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.{CapturingProcessAdapter, OSProcessHandler, ProcessEvent}
import com.intellij.formatting.service.AsyncDocumentFormattingService.FormattingTask
import com.intellij.formatting.service.{AsyncDocumentFormattingService, AsyncFormattingRequest, FormattingService}
import com.intellij.psi.PsiFile
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.idea.utils.Notification
import top.criwits.scaleda.verilog.{VerilogFileType, VerilogLanguage, VerilogPSIFileRoot}
import top.criwits.scaleda.verilog.editor.codeStyle.VerilogCodeStyleSettings

import java.nio.charset.StandardCharsets
import java.util
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionException
import scala.jdk.CollectionConverters._

class VerilogExternalFormatter extends AsyncDocumentFormattingService {
  override def createFormattingTask(formattingRequest: AsyncFormattingRequest): AsyncDocumentFormattingService.FormattingTask = {
    val formattingContext = formattingRequest.getContext
    val project = formattingContext.getProject

    // Check if Verible is installed
    if (!VeribleFormatterHelper.veribleFormatter.exists()) {
      return null
    }

    val codeStyleSettings = formattingContext.getCodeStyleSettings
    val verilogStyle = codeStyleSettings.getCustomSettings(classOf[VerilogCodeStyleSettings])
    val file = formattingRequest.getIOFile
    if (file == null) return null

    val params = Seq(
      "--indentation_spaces=" + codeStyleSettings.getIndentOptions(VerilogFileType.instance).INDENT_SIZE,
      "--column_limit=" + codeStyleSettings.getRightMargin(VerilogLanguage),
      "--line_break_penalty=" + verilogStyle.LINE_BREAK_PENALTY,
      "--over_column_limit_penalty=" + verilogStyle.OVER_COLUMN_LIMIT_PENALTY,
      "--wrap_spaces=" + verilogStyle.WRAP_SPACES,

      "--assignment_statement_alignment=" + verilogStyle.ASSIGNMENT_STATEMENT_ALIGNMENT,
      "--case_items_alignment=" + verilogStyle.CASE_ITEMS_ALIGNMENT,
      "--class_member_variable_alignment=" + verilogStyle.CLASS_MEMBER_VARIABLE_ALIGNMENT,
      "--compact_indexing_and_selections=" + verilogStyle.COMPACT_INDEXING_AND_SELECTION,
      "--distribution_items_alignment=" + verilogStyle.DISTRIBUTION_ITEMS_ALIGNMENT,
      "--enum_assignment_statement_alignment=" + verilogStyle.ENUM_ASSIGNMENT_STATEMENT_ALIGNMENT,
      "--expand_coverpoints=" + verilogStyle.EXPAND_COVERPOINTS,
      "--formal_parameters_alignment=" + verilogStyle.FORMAL_PARAMETERS_ALIGNMENT,
      "--formal_parameters_indentation=" + (if (verilogStyle.FORMAL_PARAMETERS_INDENTATION) "indent" else "wrap"),
      "--module_net_variable_alignment=" + verilogStyle.MODULE_NET_VARIABLE_ALIGNMENT,
      "--named_parameter_alignment=" + verilogStyle.NAMED_PARAMETER_ALIGNMENT,
      "--named_parameter_indentation=" + (if (verilogStyle.NAMED_PARAMETER_INDENTATION) "indent" else "wrap"),
      "--named_port_alignment=" + verilogStyle.NAMED_PORT_ALIGNMENT,
      "--named_port_indentation=" + (if (verilogStyle.NAMED_PORT_INDENTATION) "indent" else "wrap"),
      "--port_declarations_alignment=" + verilogStyle.PORT_DECLARATIONS_ALIGNMENT,
      "--port_declarations_indentation=" + (if (verilogStyle.PORT_DECLARATIONS_INDENTATION) "indent" else "wrap"),
      "--port_declarations_right_align_packed_dimensions=" + verilogStyle.PORT_DRAPD,
      "--port_declarations_right_align_unpacked_dimensions=" + verilogStyle.PORT_DRAUD,
      "--struct_union_members_alignment=" + verilogStyle.STRUCT_UNION_MEMBERS_ALIGNMENT,
      "--try_wrap_long_lines=" + verilogStyle.TRY_WRAP_LONG_LINES,
      "--failsafe_success=false",
      file.getAbsolutePath
    )

    try {
      val cmdLine = new GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath(VeribleFormatterHelper.veribleFormatter.getAbsolutePath)
        .withParameters(params.asJava)

      val handler = new OSProcessHandler(cmdLine.withCharset(StandardCharsets.UTF_8))

      return new FormattingTask() {
        override def run(): Unit = {
          handler.addProcessListener(new CapturingProcessAdapter() {
            override def processTerminated(event: ProcessEvent): Unit = {
              val exitCode = event.getExitCode;
              if (exitCode == 0) {
                formattingRequest.onTextReady(getOutput.getStdout)
              } else {
                formattingRequest.onError(ScaledaBundle.message("notification.formatting.failed"), getOutput.getStderr)
              }
            }
          })
          handler.startNotify()
        }

        override def cancel(): Boolean = {
          handler.destroyProcess()
          return true
        }

        override def isRunUnderProgress: Boolean = true
      }
    } catch {
      case e: ExecutionException =>
        formattingRequest.onError(ScaledaBundle.message("notification.formatting.failed"), e.getMessage)
        return null
    }
  }

  override def getNotificationGroupId: String = Notification.NOTIFICATION_GROUP_ID

  override def getName: String = "Verilog"

  override def getFeatures: util.Set[FormattingService.Feature] =
    util.EnumSet.noneOf(classOf[FormattingService.Feature])

  override def canFormat(file: PsiFile): Boolean =
    file.isInstanceOf[VerilogPSIFileRoot]
}
