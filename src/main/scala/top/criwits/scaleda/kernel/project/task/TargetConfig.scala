package top.criwits.scaleda
package kernel.project.task

case class TargetConfig
(name: String,
 toolchain: String,
 device: String,
 `package`: String,
 speed: Int,
 tasks: Array[TaskConfig])
