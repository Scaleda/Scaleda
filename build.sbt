import org.jetbrains.sbtidea.Keys.*
import org.jetbrains.sbtidea.verifier.*
import scala.io.Source

val publicScalaVersion = "2.13.12"
// val ideaVersion = "2023.1.3"
val ideaVersion = "241.15989.150"
// val ideaVersion = "2024.1.1"

ThisBuild / scalaVersion := publicScalaVersion
ThisBuild / intellijPluginName := "Scaleda"
ThisBuild / intellijPlatform := IntelliJPlatform.IdeaCommunity
ThisBuild / intellijBuild := ideaVersion

// ThisBuild / githubWorkflowTargetTags += "v*.*.*"
// ThisBuild / githubWorkflowJavaVersions += JavaSpec.temurin("17")
// ThisBuild / githubWorkflowPublish := Seq(
//   WorkflowStep.Sbt(
//     commands = List("publishPlugin"),
//     name = Some("Publish this plugin"),
//   )
// )

Global / intellijAttachSources := true

val junitInterfaceVersion = "0.11"
val jacksonVersion        = "2.17.0"

val thisVersion = {
  val versionSource = Source.fromFile(new File(".version"))
  val result        = versionSource.getLines().toSeq.head.strip()
  versionSource.close()
  result
}

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
    case PathList(ps @ _*) if ps.last endsWith ".class"      => MergeStrategy.first
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
  "com.novocode" % "junit-interface" % junitInterfaceVersion % Test,
  //  "org.antlr"                   % "antlr4-intellij-adaptor" % "0.1" // imported with source code
  "org.antlr"                   % "antlr4"          % "4.13.1",
  "org.antlr"                   % "antlr4-runtime"  % "4.13.1",
  "io.circe"                   %% "circe-yaml"      % "1.15.0",
  "com.github.scopt"           %% "scopt"           % "4.1.0",
  "ch.qos.logback"              % "logback-classic" % "1.5.6",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5",
  "org.scalactic"              %% "scalactic"       % "3.2.18",
  "org.scalatest"              %% "scalatest"       % "3.2.18" % "test",
  // for logger
  "com.lihaoyi" %% "sourcecode" % "0.4.1",
  // for color print
  "com.lihaoyi" %% "fansi" % "0.5.0",
  // https://mvnrepository.com/artifact/com.hubspot.jinjava/jinjava
  "com.hubspot.jinjava" % "jinjava" % "2.7.2",
  // https://mvnrepository.com/artifact/com.google.guava/guava
  "com.google.guava" % "guava" % "33.1.0-jre",
  // https://mvnrepository.com/artifact/commons-io/commons-io
  "commons-io" % "commons-io" % "2.16.1",
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
  "org.xerial" % "sqlite-jdbc" % "3.45.3.0",
  // https://mvnrepository.com/artifact/com.auth0/java-jwt
  "com.auth0" % "java-jwt" % "4.4.0",
  // https://mvnrepository.com/artifact/commons-codec/commons-codec
  "commons-codec" % "commons-codec" % "1.16.1",
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
    // CAN BE DIFFERENT FROM IDEA VERSION
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % "1.62.2",
    // libraryDependencies += "io.grpc" % "grpc-netty" % "1.55.1",
    packageLibraryMappings := Seq.empty, // allow scala-library
    assembly / assemblyJarName := "scaleda-kernel.jar",
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value
    )
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
      xml.sinceBuild = (ThisBuild / intellijBuild).value
    },
    pluginVerifierOptions := pluginVerifierOptions.value.copy(
      version = "1.365", // use a specific verifier version
      offline = true,    // forbid the verifier from reaching the internet
      // overrideIDEs = Seq("IC", "IU").map(_ + "-" + ideaVersion),  // verify against specific products instead of 'intellijBuild'
      failureLevels = Set(FailureLevel.DEPRECATED_API_USAGES) // only fail if deprecated APIs are used
    ),
    assembly / assemblyJarName := "scaleda.jar",
    libraryDependencies ++= publicLibraryDependencies,
    // ANTLR IntelliJ Adapter
    Compile / unmanagedSourceDirectories += baseDirectory.value / "antlr4-intellij-adaptor/src/main/java"
  )
  .enablePlugins(SbtIdeaPlugin)

lazy val root = (project in file("."))
  .aggregate(kernel, scaleda)

javaOptions += "-Didea.log.debug.categories=top.scaleda"
