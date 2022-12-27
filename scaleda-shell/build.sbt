ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val scaledaShell = (project in file("."))
  .settings(
    name := "scaleda-shell",
    idePackagePrefix := Some("top.criwits.scaleda.shell")
  )
