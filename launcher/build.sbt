import sbt._, Keys._
import dependencies._

libraryDependencies ++= Seq (
  scalaz.core,
  scalaz.streams,
  avro.core,
  _test(scalatest.core)
)

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"

libraryDependencies += "org.clapper" %% "classutil" % "1.1.2"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.10.1.2"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5")

console.settings
