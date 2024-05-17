.. image:: images/icon-with-name.svg
   :align: left

Scaleda: Transforming IntelliJ IDEA into an FPGA Development Tool
===============================================================

.. image:: https://img.shields.io/jetbrains/plugin/d/21863?color=%2315559aff
   :target: https://plugins.jetbrains.com/plugin/21863
.. image:: https://readthedocs.org/projects/scaleda/badge/?version=latest
   :target: https://scaleda.readthedocs.io/zh-cn/latest/?badge=latest
   :alt: Documentation Status
.. image:: https://img.shields.io/badge/document-online-blue
   :target: https://scaleda.top
   :alt: Online Documentation

.. image:: images/main-screenshot.png
   :alt: Using Scaleda for Verilog Development

Overview
--------

Scaleda (=Scalable + EDA) is an extensible FPGA EDA support tool based on the IntelliJ platform, featuring HDL project management, HDL language support, FPGA tool invocation, and more. It aims to transform IntelliJ IDEA into a flexible and powerful FPGA development tool. Currently, Scaleda supports the following features:

- Basic syntax features for Verilog language (syntax highlighting, code folding, code formatting, code commenting, code navigation, etc.);
- Simple code completion for Verilog language;
- Code inspection for Verilog (syntax errors and semantic errors, as well as errors such as missing ports, multiple drivers, etc.);
- Verilog and SystemVerilog code formatting using `Verible <https://github.com/chipsalliance/verible>`__;
- Syntax parsing and highlighting for Verilog, SystemVerilog, Tcl, Bluespec SystemVerilog;
- Proprietary project and IP structure, supporting the invocation of tools like Vivado, Quartus Prime, iverilog, and Yosys for simulation, synthesis, and implementation;
- Support for loading Vivado projects and invoking synthesis and implementation operations within them;
- Built-in waveform viewers `Rvcd <https://github.com/Scaleda/rvcd>`__ and `Surfer <https://gitlab.com/surfer-project/surfer>`__;
- Running in server mode to execute remote EDA tasks;
- Verible, `Svls <https://github.com/dalance/svls>`__, or custom language server (LSP) support;
- i18n support.

For user manuals, API documentation, and more, please visit the `Online Documentation <https://scaleda.top>`__.

Installation
------------

Scaleda currently supports **version 2024.1.1 and above** of all IJ platforms except Gateway, including IDEA Community Edition and IDEA Ultimate Edition. You can search for "Scaleda" in the plugin marketplace, download the precompiled plugin zip package, or compile the plugin yourself.

.. |Visit marketplace| image:: https://img.shields.io/jetbrains/plugin/d/21863?color=%2315559aff
   :target: https://plugins.jetbrains.com/plugin/21863

`Visit marketplace <https://plugins.jetbrains.com/plugin/21863>`__ |Visit marketplace|

For the server-side program, please visit `Github Release <https://github.com/Scaleda/Scaleda/releases>`__ to download the ``scaleda-kernel.jar``.

Installing Directly from Zip File
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

IJ platform plugins are distributed as zip archives. You can download the precompiled plugin zip package and install it in IntelliJ IDEA. In the `Releases <https://github.com/Scaleda/Scaleda/releases>`__ page, you can find the latest plugin zip package named something like ``Scaleda-0.1.0.zip``.

After launching IDEA, select ``File -> Settings -> Plugins -> Install Plugin from Disk...``, then choose the downloaded zip file and click ``OK`` to install the plugin. For detailed instructions on installing Scaleda in IDEA, refer to :doc:`docs/installation`.

Compiling from Source
~~~~~~~~~~~~~~~~~~~~~

Scaleda is primarily developed in Scala and uses sbt (not Gradle) as the build tool. Therefore, if you want to compile Scaleda from source, you need to install Scala and sbt.

After cloning the repository, compile with sbt:

.. code:: bash

   $ sbt compile

Note that the first time you start sbt in this repository, it may download dependencies and source code for the IJ platform from the internet, which may take some time and disk space.

After compiling, package with the following command:

.. code:: bash

   $ sbt packageArtifactZip

Once packaging is complete, you can find the packaged zip file in the ``target/scala-2.13/`` directory. You can install the plugin using the method described in the previous section.

User Manual
-----------

Please visit the `Online Documentation <https://scaleda.top/manual/index.html>`__ to view the user manual.

The PDF version of the user manual can be downloaded `here <https://github.com/Scaleda/Scaleda/blob/master/docs/scaleda-manual.pdf>`__. (For version v0.0.9.)

Project Structure
-----------------

This section

 introduces the structure of the Scaleda project, not the structure of FPGA projects developed using Scaleda. For the latter, please refer to :doc:`docs/manual/project`.

Scaleda is an IDEA plugin developed in Scala and Java. Its source code structure is as follows:

Kernel Part
~~~~~~~~~~~

The "kernel" part of Scaleda includes features such as toolchain (e.g., Vivado) invocation and project structure definition. The kernel is designed to run as a standalone command-line program independent of the IJ platform. The kernel is located in the ``scaleda-kernel`` directory as an sbt subproject. Run ``sbt 'project kernel; run'`` or execute the compiled ``java -jar scaleda-kernel.jar --help`` to see the kernel usage help.

Plugin Part
~~~~~~~~~~~

The "plugin" part of Scaleda includes Verilog language support, graphical project management, and other features interfacing with the IJ platform. The plugin code is located in the ``src`` directory. The code under the ``top.scaleda.idea`` package is for the IJ platform plugin, and the code under the ``top.scaleda.verilog`` package provides Verilog language support.

Open Source Projects Used
-------------------------

Scaleda references and uses numerous open source projects, including but not limited to:

- `ANTLR <https://github.com/antlr/antlr4>`__, `ANTLR IntelliJ Adapter <https://github.com/antlr/antlr4-intellij-adaptor>`__
- `JetBrains Verilog Plugin <https://github.com/MrTsepa/jetbrains-verilog-plugin/>`__
- `Verilog-HDL/SystemVerilog/Bluespec SystemVerilog <https://github.com/mshr-h/vscode-verilog-hdl-support>`__
- `Verible <https://github.com/chipsalliance/verible>`__
- `Svls <https://github.com/dalance/svls>`__

License and Acknowledgements
-----------------------------

Scaleda is open-sourced under the `Mulan PSL v2 <https://license.coscl.org.cn/MulanPSL2>`__ license. For details, please refer to the `LICENSE <https://github.com/Scaleda/Scaleda/blob/master/LICENSE>`__ file.

Scaleda's development is guided by Professor Xu Yong from Harbin Institute of Technology (Shenzhen) and originates from the HIT (Shenzhen) - Zhongwei Semiconductor Artificial Intelligence Chip Joint Laboratory.

Thanks to JetBrains for their tool support.

.. image:: https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg
   :alt: JetBrains