package top.criwits.scaleda
package kernel.toolchain.executor

import kernel.toolchain.ToolchainProfile

import java.io.File

abstract class Executor
(val profile: ToolchainProfile,
 val workingDir: File = new File("."))
