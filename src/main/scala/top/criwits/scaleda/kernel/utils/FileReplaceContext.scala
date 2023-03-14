package top.criwits.scaleda
package kernel.utils

import java.io.File

case class FileReplaceContext(from: File, to: File, lineOffsetStart: Int = 0, lineOffset: Int = 0)
