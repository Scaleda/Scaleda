import org.jetbrains.sbtidea.Keys._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / intellijPluginName := "Scaleda"
ThisBuild / intellijPlatform := IntelliJPlatform.IdeaCommunity
ThisBuild / intellijBuild := "221.5787.30"
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))
Global / intellijAttachSources := true

val junitInterfaceVersion = "0.11"

lazy val scaleda = project.in(file(".")).enablePlugins(SbtIdeaPlugin).settings(
  version := "0.0.1-SNAPSHOT",
  // Compile / scalaSource := baseDirectory.value / "src",
  // Test / scalaSource := baseDirectory.value / "test",
  // Compile / resourceDirectory := baseDirectory.value / "resources",
  Global / javacOptions ++= Seq("-source", "11", "-target", "11"),
  Global / scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
  ),
  ideBasePackages := Seq("top.criwits.scaleda"),
  intellijPlugins := Seq("com.intellij.properties", "com.intellij.java", "com.intellij.java-i18n",
    // "antlr4-intellij-plugin-sample"
  ).map(_.toPlugin),
  libraryDependencies ++= Seq(
    "com.novocode" % "junit-interface" % junitInterfaceVersion % Test,
    "org.antlr" % "antlr4-intellij-adaptor" % "0.1",
    "org.antlr" % "antlr4" % "4.11.1",
    "org.antlr" % "antlr4-runtime" % "4.11.1",
    "io.circe" %% "circe-yaml" % "0.14.2",
    "com.github.scopt" %% "scopt" % "4.1.0",
  ),
  packageLibraryMappings := Seq.empty, // allow scala-library
  patchPluginXml := pluginXmlOptions { xml =>
    xml.version = version.value
  },
  assembly / assemblyJarName := "scaleda.jar",
  assembly / mainClass := Some("top.criwits.scaleda.shell.ScaledaShellMain"),
)
