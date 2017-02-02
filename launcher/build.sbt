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