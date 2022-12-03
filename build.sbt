import org.jetbrains.sbtidea.Keys._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / intellijPluginName := "Scaleda"
ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity
ThisBuild / intellijBuild := "221.5787.30"
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
Global    / intellijAttachSources := true

val junitInterfaceVersion = "0.11"

lazy val scaleda = project.in(file(".")).enablePlugins(SbtIdeaPlugin).settings(
    version := "0.0.1-SNAPSHOT",
    Compile / scalaSource := baseDirectory.value / "src",
    Test / scalaSource := baseDirectory.value / "test",
    Compile / resourceDirectory := baseDirectory.value / "resources",
    Global / javacOptions ++= Seq("-source", "17", "-target", "17"),
    Global / scalacOptions ++= Seq(
        "-deprecation",
        "-feature",
        "-unchecked",
        "-Xfatal-warnings",
    ),
    ideBasePackages := Seq("top.criwits.scaleda"),
    intellijPlugins := Seq("com.intellij.properties", "com.intellij.java", "com.intellij.java-i18n").map(_.toPlugin),
    libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % junitInterfaceVersion % Test,
    ),
    packageLibraryMappings := Seq.empty, // allow scala-library
    patchPluginXml := pluginXmlOptions { xml =>
        xml.version = version.value
    }
)
