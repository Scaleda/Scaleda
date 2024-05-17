# 构建 Scaleda

Scaleda 使用 Sbt 进行构建，因此您需要安装 Sbt 或者使用 IDEA 进行构建。

Sbt 运行过程中会自动下载依赖和开发中使用的 IntelliJ IDEA，因此需要预留约 5GiB 的磁盘空间。

## 生成 IDEA 插件

```shell
sbt 'packageArtifactZip; packageArtifactZip'
```

由于 `patchPluginXml` 可能会出现问题，因此最好连续运行两次。

生成的插件位于 `target/scaleda-x.x.x.zip`。版本号从 `.version` 文件中读取，并被填入 `plugin.xml`。插件的描述信息、更新信息从 `docs/plugin-description.html`、`docs/changelog.html` 中读取。

## 生成 CLI 工具

```shell
sbt 'kernel / assembly'
```

生成的 CLI 工具位于 `scaleda-kernel/target/scala-2.13/scaleda-kernel.jar`。

## 清理

需要分别清理默认 module 和 `kernel` module。

```shell
sbt 'clean; kernel / clean'
```

## 在 IDEA 中运行插件

```shell
sbt 'runIDE'
```

[GitHub Actions](https://github.com/Scaleda/Scaleda/actions) 中的构建过程会自动运行上述生成命令，产物会被打包上传到 Actions 或 Release。如果您需要手动构建，可以参考 CI：`.github/workflows/*.yml`。

