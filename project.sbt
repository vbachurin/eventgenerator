organization in Global := "glbox"

scalaVersion in Global := "2.11.8"

lazy val eventgen = project.in(file(".")).aggregate(core, launcher)

lazy val core     = project

lazy val launcher = project.dependsOn(core)

settings.common
