import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq (
  scalaz.core,
  scalaz.streams,
  avro.core,
  _test(scalatest.core)
)

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"

console.settings

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-scalaz" % "0.2.0",
  "co.fs2" %% "fs2-io" % "0.9.2"
)