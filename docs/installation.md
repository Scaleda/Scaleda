# Scaleda 安装

Scaleda 目前支持 **2024.1.1 及以上版本** 的除了 Gateway 以外的 IJ 平台，包括 IDEA Community Edition 和 IDEA Ultimate Edition 等。
可以在 [此处](https://www.jetbrains.com/idea/download/) 下载 IDEA。
您可以选择在插件市场搜索「Scaleda」，或直接下载编译好的插件 zip 包，或者自行编译插件。

[访问插件市场：![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/21863?color=%2315559aff)](https://plugins.jetbrains.com/plugin/21863)

服务器端程序请访问 [Github Release](https://github.com/Scaleda/Scaleda/releases) 下载 `scaleda-kernel.jar`。

## 下载 zip 文件来安装

IJ 平台的插件以 zip 压缩包的形式进行发布，您可以直接下载编译好的插件 zip 包，然后在 IJ 中安装。
在 [Releases](https://github.com/Scaleda/Scaleda/releases) 页面中，您可以找到最新的插件 zip 包，它的名字类似于 `Scaleda-0.2.0.zip`。
由于 IntelliJ 市场需要审核，市场版本发布会有约 1-3 天的延迟。

在启动画面左方选择「Plugins / 插件」，然后点击右侧上方的齿轮按钮，选择「Install Plugin from Disk / 从磁盘安装插件」。

![Install Plugin from Disk](images/splash-install.png)

然后，选择 `zip` 格式的插件包，点击「OK」即可安装。
注意不要解压插件包，否则无法安装。

如果您已经使用过 IDEA，可能看不见上面的启动画面。
您可以选择「File / 文件」菜单，然后选择「Settings / 设置」，在弹出的窗口中选择「Plugins / 插件」，然后点击右侧上方的齿轮按钮，选择「Install Plugin from Disk / 从磁盘安装插件」来安装。

## 从源码编译

参考 [构建](build) 章节。