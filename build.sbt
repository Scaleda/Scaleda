import org.jetbrains.sbtidea.Keys._

val publicScalaVersion = "2.13.10"

ThisBuild / scalaVersion := publicScalaVersion
ThisBuild / intellijPluginName := "Scaleda"
ThisBuild / intellijPlatform := IntelliJPlatform.IdeaCommunity
ThisBuild / intellijBuild := "231.9011.34"
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
Global / intellijAttachSources := true

val junitInterfaceVersion = "0.11"
val jacksonVersion        = "2.14.2"
val thisVersion           = "0.0.2-SNAPSHOT"

lazy val commonSettings = Seq(
  version := thisVersion,
  Global / javacOptions ++= Seq("-source", "17", "-target", "17"),
  Global / scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xasync"
  ),
  assembly / assemblyMergeStrategy := {
    case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
    // case _ => MergeStrategy.preferProject
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  },
  // assembly / fullClasspath := (Compile / fullClasspath).value,
  assembly / mainClass := Some("top.scaleda.kernel.shell.ScaledaShellMain"),
  commands += Command.command("assemblyTrulyFatJar") { state =>
    """set assembly / fullClasspath := (Compile / fullClasspath).value""" :: "assembly" :: state
  }
)

lazy val publicLibraryDependencies = Seq(
  "com.novocode"                % "junit-interface"         % junitInterfaceVersion % Test,
  "org.antlr"                   % "antlr4-intellij-adaptor" % "0.1",
  "org.antlr"                   % "antlr4"                  % "4.12.0",
  "org.antlr"                   % "antlr4-runtime"          % "4.12.0",
  "io.circe"                   %% "circe-yaml"              % "0.14.2",
  "com.github.scopt"           %% "scopt"                   % "4.1.0",
  "ch.qos.logback"              % "logback-classic"         % "1.4.6",
  "com.typesafe.scala-logging" %% "scala-logging"           % "3.9.5",
  "org.scalactic"              %% "scalactic"               % "3.2.15",
  "org.scalatest"              %% "scalatest"               % "3.2.15"              % "test",
  // for logger
  "com.lihaoyi" %% "sourcecode" % "0.3.0",
  // for color print
  "com.lihaoyi" %% "fansi" % "0.4.0",
  // https://mvnrepository.com/artifact/com.hubspot.jinjava/jinjava
  "com.hubspot.jinjava" % "jinjava" % "2.7.0",
  // https://mvnrepository.com/artifact/com.google.guava/guava
  "com.google.guava" % "guava" % "31.1-jre",
  // https://mvnrepository.com/artifact/commons-io/commons-io
  "commons-io" % "commons-io" % "2.11.0",
  // https://mvnrepository.com/artifact/log4j/log4j
  "log4j"                   % "log4j"         % "1.2.17",
  "org.scala-lang.modules" %% "scala-async"   % "1.0.1",
  "org.scala-lang"          % "scala-reflect" % publicScalaVersion % Provided,
  // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
  // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % jacksonVersion,
  // https://mvnrepository.com/artifact/com.github.serceman/jnr-fuse
  "com.github.serceman" % "jnr-fuse" % "0.5.7",
  // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
  "org.xerial" % "sqlite-jdbc" % "3.41.0.0",
  // https://mvnrepository.com/artifact/com.auth0/java-jwt
  "com.auth0" % "java-jwt" % "4.3.0",
  // https://mvnrepository.com/artifact/commons-codec/commons-codec
  "commons-codec" % "commons-codec" % "1.15",
  // for GRPC, same version in IDEA
  // "io.grpc" % "grpc-netty-shaded" % "1.53.0",
  // "io.grpc" % "grpc-netty-shaded" % "1.55.1",
  // scalapb
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)

lazy val kernel = project
  .in(file("scaleda-kernel"))
  .disablePlugins(SbtIdeaPlugin)
  .settings(
    commonSettings,
    intellijPlugins := Seq(),
    libraryDependencies ++= publicLibraryDependencies,
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % "1.55.1",
    // libraryDependencies += "io.grpc" % "grpc-netty-shaded" % "1.55.1" % Provided,
    // libraryDependencies += "io.grpc" % "grpc-netty" % "1.55.1",
    libraryDependencies += "io.grpc" % "grpc-netty" % "1.55.1",
    packageLibraryMappings := Seq.empty, // allow scala-library
    assembly / assemblyJarName := "scaleda-kernel.jar",
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value
    ),
  )

lazy val scaleda = project
  .in(file("."))
  .dependsOn(kernel)
  .settings(
    commonSettings,
    ideBasePackages := Seq("top.scaleda"),
    intellijPlugins := Seq(
      "com.intellij.properties",
      "com.intellij.java",
      "com.intellij.java-i18n"
      // "antlr4-intellij-plugin-sample"
    ).map(_.toPlugin),
    patchPluginXml := pluginXmlOptions { xml =>
      xml.version = version.value
    },
    assembly / assemblyJarName := "scaleda.jar",
    libraryDependencies ++= publicLibraryDependencies,
    // assembly / assemblyShadeRules := Seq(
    //   ShadeRule.rename("io.grpc.netty.shaded.**" -> "scaleda.@1").inAll
    // ),
    assembly / assemblyMergeStrategy := {
      // case PathList("io", "grpc", "netty", "shaded", xs @ _*) => MergeStrategy.discard
      // case PathList("io", "grpc", "netty", xs @ _*) => MergeStrategy.discard
      // case PathList("io", "grpc", xs @ _*) => MergeStrategy.preferProject
      // case PathList("io", "grpc", "netty", xs @ _*) => MergeStrategy.discard
      // case PathList("io", "netty", xs @ _*) => MergeStrategy.discard
      // case PathList("org", "antlr", xs @ _*) => MergeStrategy.preferProject
      // case PathList("com", "thoughtworks", xs @ _*) => MergeStrategy.discard
      // case PathList("ch", "qos", "logback", xs @ _*) => MergeStrategy.discard
      // case PathList("org", "slf4j", xs @ _*) => MergeStrategy.discard
      // case PathList("META-INF/versions", xs @ _*) => MergeStrategy.discard
      case _ => MergeStrategy.preferProject
      // case x =>
      //   val oldStrategy = (assembly / assemblyMergeStrategy).value
      //   oldStrategy(x)
    }
  )
  .enablePlugins(SbtIdeaPlugin)

lazy val root = (project in file("."))
  .aggregate(kernel, scaleda)
