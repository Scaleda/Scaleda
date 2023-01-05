package top.criwits.scaleda
package kernel.project.task

case class TaskConfig
(name: String,
 toolchain: String,
 device: String,
 `package`: String,
 speed: Int,
 targets: Array[TargetConfig])
