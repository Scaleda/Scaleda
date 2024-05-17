# Scaleda：将 IntelliJ IDEA 打造为 FPGA 开发工具

[![Visit marketplace](docs/images/icon-with-name.svg)](https://plugins.jetbrains.com/plugin/21863)

[English](README.en.md)

![使用 Scaleda 进行 Verilog 开发](docs/images/main-screenshot.png)

## 概述

Scaleda is an extensible FPGA EDA support tool based on the IntelliJ platform, featuring HDL project management, HDL language support, FPGA tool invocation, and more. It aims to transform IntelliJ IDEA into a flexible and powerful FPGA development tool. Currently, Scaleda supports the following features:

 - Basic syntax features for Verilog language (syntax highlighting, code folding, code formatting, code commenting, code navigation, etc.);
 - Simple code completion for Verilog language;
 - Code inspection for Verilog (syntax errors and semantic errors, as well as errors such as missing ports, multiple drivers, etc.);
 - Verilog and SystemVerilog code formatting using [Verible](https://github.com/chipsalliance/verible);
 - Syntax parsing and highlighting for Verilog, SystemVerilog, Tcl, Bluespec SystemVerilog;
 - Proprietary project and IP structure, supporting the invocation of tools like Vivado, Quartus Prime, iverilog, and Yosys for simulation, synthesis, and implementation;
 - Support for loading Vivado projects and invoking synthesis and implementation operations within them;
 - Built-in waveform viewers [Rvcd](https://github.com/Scaleda/rvcd) and [Surfer](https://gitlab.com/surfer-project/surfer);
 - Running in server mode to execute remote EDA tasks.
 - Verible, [Svls](https://github.com/dalance/svls), or custom language server (LSP) support;
 - i18n support.

Scaleda（=Scalable + EDA）是一个 IntelliJ 平台的可扩展 FPGA EDA 辅助工具，具有 HDL 项目管理、HDL 语言支持、FPGA 工具调用等功能，将 IJ 打造成为灵活而强大的 FPGA 开发工具。
目前，Scaleda 已支持的功能包括：

 - Verilog 语言的基本语法功能（语法高亮、代码折叠、代码格式化、代码注释、代码导航等）；
 - Verilog 语言的简单代码补全；
 - Verilog 语言的代码检查（语法错误与语义错误，以及诸如端口缺失、多驱动等错误）；
 - 使用 [Verible](https://github.com/chipsalliance/verible) 的 Verilog、SystemVerilog 代码格式化；
 - Verilog、SystemVerilog、Tcl、Bluespec SystemVerilog 语法解析高亮；
 - 自有工程和 IP 结构，支持调用 Vivado、Quartus Prime、iverilog、Yosys 等工具进行仿真、综合、实现等操作；
 - 支持加载 Vivado 工程并调用其中的综合、实现等操作；
 - 内置波形查看器 [Rvcd](https://github.com/Scaleda/rvcd)、[Surfer](https://gitlab.com/surfer-project/surfer)；
 - 运行服务器模式以执行远程 EDA 任务；
 - Verible、[Svls](https://github.com/dalance/svls)或自定义语言服务器（LSP）支持；
 - 国际化多显示语言支持；

用户手册、API 文档等请访问 [在线文档](https://scaleda.top)。

## 安装

Scaleda 目前支持 **2024.1.1 及以上版本** 的除了 Gateway 以外的 IJ 平台，包括 IDEA Community Edition 和 IDEA Ultimate Edition 等。
您可以选择在插件市场搜索「Scaleda」，或直接下载编译好的插件 zip 包，或者自行编译插件。

[访问插件市场：![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/21863?color=%2315559aff)](https://plugins.jetbrains.com/plugin/21863)

服务器端程序请访问 [Github Release](https://github.com/Scaleda/Scaleda/releases) 下载 `scaleda-kernel.jar`。

### 直接下载 zip 文件来安装

IJ 平台的插件以 zip 压缩包的形式进行发布，您可以直接下载编译好的插件 zip 包，然后在 IJ 中安装。
在 [Releases](https://github.com/Scaleda/Scaleda/releases) 页面中，您可以找到最新的插件 zip 包，它的名字类似于 `Scaleda-0.1.0.zip`。

启动 IDEA 后，选择 `File -> Settings -> Plugins -> Install Plugin from Disk...`，然后选择下载好的 zip 文件，点击 `OK` 即可安装插件。
有关在 IDEA 中安装 Scaleda 的详细过程，请参考 [这篇](docs/installation.md) 文档。

### 从源码编译

Scaleda 主要使用 Scala 开发，并使用 sbt（而不是 Gradle）作为构建工具。
因此，如果你想从源码编译 Scaleda，你需要安装 Scala 和 sbt。

克隆本仓库后，使用 sbt 进行编译：

```bash
$ sbt compile
```

注意，第一次在本仓库下启动 sbt 时，sbt 可能会从网络下载 IJ 平台的依赖和源码，这可能需要一些时间并占用磁盘空间。

编译完成后，使用下面的命令进行打包：

```bash
$ sbt packageArtifactZip
```

打包完成后，你可以在 `target/scala-2.13/` 目录下找到打包好的 zip 文件。
您可以用上节的方法安装插件。

## 使用手册

PDF 版本的使用手册可以在 [这里](https://github.com/Scaleda/Scaleda/blob/master/docs/scaleda-manual.pdf) 下载。
（适用于 v0.0.9 版本。）

## 项目结构

这里介绍 Scaleda 这个项目的结构，而不是「使用 Scaleda 开发的 FPGA 项目」的结构。
对于后者，请参考 [这篇](docs/project.md) 文档。

Scaleda 是一个使用 Scala 和 Java 开发的 IDEA 插件，其源码的结构如下：

### 内核部分

Scaleda 的「内核」部分包括工具链（如 Vivado）的调用、工程结构的定义等功能。
内核被设计为一个可以独立于 IJ 平台运行的命令行程序。
内核位于 `scaleda-kernel` 目录下，作为一个 sbt 子项目存在。
运行 `sbt 'project kernel; run'` 或执行编译好的 `java -jar scaleda-kernel.jar --help` 以查看内核使用帮助。

### 插件部分

Scaleda 的「插件」部分包括 Verilog 语言支持、图形化工程管理等与 IJ 平台对接的功能。
插件部分的代码位于 `src` 目录下。
其中，包 `top.scaleda.idea` 下的代码为 IJ 平台的插件代码，包 `top.scaleda.verilog` 下的代码为提供 Verilog 语言支持功能的代码。

## 使用的开源项目

Scaleda 参考和使用了诸多开源项目，包括但不限于：

 - [ANTLR](https://github.com/antlr/antlr4)、[ANTLR IntelliJ Adapter](https://github.com/antlr/antlr4-intellij-adaptor)
 - [JetBrains Verilog Plugin](https://github.com/MrTsepa/jetbrains-verilog-plugin/)
 - [Verilog-HDL/SystemVerilog/Bluespec SystemVerilog](https://github.com/mshr-h/vscode-verilog-hdl-support)
 - [Verible](https://github.com/chipsalliance/verible)
 - [Svls](https://github.com/dalance/svls)

## 开源协议与鸣谢

Scaleda 使用[《木兰宽松许可证（第 2 版）》](https://license.coscl.org.cn/MulanPSL2)协议开源，详情请参考 [LICENSE](https://github.com/Scaleda/Scaleda/blob/master/LICENSE) 文件。

Scaleda 的开发由哈尔滨工业大学（深圳）徐勇教授指导，项目来源于哈工大（深圳）- 中微半导人工智能芯片联合实验室。

感谢 JetBrains 提供的工具支持。

![JetBrains](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)

