# Scaleda: Transforming IntelliJ IDEA into an FPGA Development Tool

[![Visit marketplace](README.assets/icon-with-name.svg)](https://plugins.jetbrains.com/plugin/21863)

## Overview

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

![Verilog Development with Scaleda](docs/images/main-screenshot.png)

User manuals, API documents, etc., can be found at [Online Documentation](https://scaleda.top).

## Installation

Scaleda currently supports **version 2024.1.1 and above** of the IJ platform, excluding Gateway, including IDEA Community Edition and IDEA Ultimate Edition, etc.

[Visit Plugin Market: ![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/21863?color=%2315559aff)](https://plugins.jetbrains.com/plugin/21863)

Server-side programs can be found in [GitHub Release](https://github.com/Scaleda/Scaleda/releases)

### Installing Directly from Zip File

IJ platform plugins are distributed as zip archives. You can download the precompiled plugin zip package and install it in IntelliJ IDEA. In the [Releases](https://github.com/Scaleda/Scaleda/releases) page, you can find the latest plugin zip package named something like `Scaleda-0.1.0.zip`.

After launching IDEA, select `File -> Settings -> Plugins -> Install Plugin from Disk...`, then choose the downloaded zip file and click `OK` to install the plugin. For detailed instructions on installing Scaleda in IDEA, refer to this [document](docs/installation.md).

### Compiling from Source

Scaleda is primarily developed in Scala and uses sbt (not Gradle) as the build tool. Therefore, if you want to compile Scaleda from source, you need to install Scala and sbt.

After cloning the repository, compile with sbt:

```bash
$ sbt compile
```

Note that the first time you start sbt in this repository, it may download dependencies and source code for the IJ platform from the internet, which may take some time and disk space.

After compiling, package with the following command:

```bash
$ sbt packageArtifactZip
```

Once packaging is complete, you can find the packaged zip file in the `target/scala-2.13/` directory. You can install the plugin using the method described in the previous section.

## User Manual

The PDF version of the user manual can be downloaded [here](https://github.com/Scaleda/Scaleda/blob/master/docs/scaleda-manual.pdf).

## Project Structure

This section introduces the structure of the Scaleda project, not the structure of FPGA projects developed using Scaleda. For the latter, please refer to this [document](docs/project.md).

Scaleda is an IDEA plugin developed in Scala and Java. Its source code structure is as follows:

### Kernel Part

The "kernel" part of Scaleda includes features such as toolchain (e.g., Vivado) invocation and project structure definition. The kernel is designed to run as a standalone command-line program independent of the IJ platform. The kernel is located in the `scaleda-kernel` directory as an sbt subproject. Run `sbt 'project kernel; run'` or execute the compiled `java -jar scaleda-kernel.jar --help` to see the kernel usage help.

### Plugin Part

The "plugin" part of Scaleda includes Verilog language support, graphical project management, and other features interfacing with the IJ platform. The plugin code is located in the `src` directory. The code under the `top.scaleda.idea` package is for the IJ platform plugin, and the code under the `top.scaleda.verilog` package provides Verilog language support.

## Open Source Projects Used

Scaleda references and uses numerous open source projects, including but not limited to:

 - [ANTLR](https://github.com/antlr/antlr4), [ANTLR IntelliJ Adapter](https://github.com/antlr/antlr4-intellij-adaptor)
 - [JetBrains Verilog Plugin](https://github.com/MrTsepa/jetbrains-verilog-plugin/)
 - [Verilog-HDL/SystemVerilog/Bluespec SystemVerilog](https://github.com/mshr-h/vscode-verilog-hdl-support)
 - [Verible](https://github.com/chipsalliance/verible)
 - [Svls](https://github.com/dalance/svls)

## License and Acknowledgements

Scaleda is open-sourced under the [Mulan PSL v2](https://license.coscl.org.cn/MulanPSL2) license. For details, please refer to the [LICENSE](https://github.com/Scaleda/Scaleda/blob/master/LICENSE) file.

Scaleda's development is guided by Professor Xu Yong from Harbin Institute of Technology (Shenzhen) and originates from the HIT (Shenzhen) - China Micro Semicom co. Artificial Intelligence Chip Joint Laboratory.

Thanks to JetBrains for their tool support.

![JetBrains](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)