# Scaleda：将 IntelliJ IDEA 打造为 FPGA 开发工具

## 概述

Scaleda 是一款 IntelliJ IDEA（下文简称 IJ）平台上的插件，它能为 IJ 平台增加包括 HDL 工程管理、HDL 语言支持、FPGA 工具调用等功能，从而将 IJ 打造成为灵活而强大的 FPGA 开发工具。
目前，Scaleda 已支持的功能包括：

 - Verilog 语言的基本语法功能（语法高亮、代码折叠、代码格式化、代码注释、代码导航等）；
 - Verilog 语言的简单代码补全；
 - Verilog 语言的代码检查（语法错误与语义错误，以及诸如端口缺失、多驱动等错误）；
 - 自有工程结构，支持调用 Vivado、iverilog 等工具进行仿真、综合、实现等操作；
 - 支持加载 Vivado 工程并调用其中的综合、实现等操作；

Scaleda 目前计划增加的功能有：
 - 更加完善的 Verilog 语言支持：重构（Refactoring）、更多代码检查（Inspection）等；
 - 完善自有工程结构：支持更多工具、支持更多操作；

## 安装

### 从源码编译

Scaleda 主要使用 Scala 开发，并使用 sbt（而不是 Gradle）作为构建工具。
因此，如果你想从源码编译 Scaleda，你需要安装 Scala 和 sbt。

克隆本仓库后，使用 sbt 进行编译：

```bash
$ sbt compile
```

编译完成后，使用 sbt 进行打包：

```bash
$ sbt packageArtifactZip
```

打包完成后，你可以在 `target/scala-2.12/` 目录下找到打包好的 zip 文件。

## 项目结构

Scaleda 项目的结构如下：

### 内核部分

Scaleda 的「内核」部分包括工具链（如 Vivado）的调用、工程结构的定义等功能。
内核被设计为一个可以独立于 IJ 平台运行的命令行程序。
内核位于 `scaleda-kernel` 目录下，作为一个 sbt 子项目存在。

### 插件部分

Scaleda 的「插件」部分包括 Verilog 语言支持、图形化工程管理等与 IJ 平台对接的功能。
插件部分的代码位于 `src` 目录下。
其中，包 `top.scaleda.idea` 下的代码为 IJ 平台的插件代码，包 `top.scaleda.verilog` 下的代码为提供 Verilog 语言支持功能的代码。

## 使用的开源项目

Scaleda 参考和使用了诸多开源项目，包括但不限于：

 - [ANTLR](https://github.com/antlr/antlr4)、[ANTLR IntelliJ Adapter](https://github.com/antlr/antlr4-intellij-adaptor)
 - [JetBrains Verilog Plugin](https://github.com/MrTsepa/jetbrains-verilog-plugin/)

## 开源协议与鸣谢

Scaleda 使用[《木兰宽松许可证（第 2 版）》](https://license.coscl.org.cn/MulanPSL2)协议开源，详情请参考 [LICENSE](LICENSE) 文件。

Scaleda 的开发由哈尔滨工业大学（深圳）徐勇教授指导。

感谢 JetBrains 提供的工具支持。

![JetBrains](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)

