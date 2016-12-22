import sbt._

object dependencies {

  def _test     (module: ModuleID): ModuleID = module % "test"
  def _provided (module: ModuleID): ModuleID = module % "provided"

  object versions {
    val scalaz     = "7.1.*"
    val stream     = "0.8.4"
    val scalatest  = "2.2.6"
    val shapeless  = "2.3.2"
    val simulacrum = "0.10.0"
    val http4s     = "0.15.0"
    val argonaut   = "6.1"
    val journal    = "2.3.16"
    val knobs      = "3.12.+"
    val commons    = "3.5"
  }

  object journal {
    val core = "io.verizon.journal" %% "core" % versions.journal
  }

  object knobs {
    val core = "io.verizon.knobs" %% "core" % versions.knobs
  }

  object scalatest {
    val core = "org.scalatest" %% "scalatest" % versions.scalatest
  }

  object argonaut {
    val core = "io.argonaut" %% "argonaut" % "6.1"
  }

  object scalaz {
    val core    = "org.scalaz"        %% "scalaz-core"   % versions.scalaz
    val streams = "org.scalaz.stream" %% "scalaz-stream" % versions.stream
  }

  object shapeless {
    val core = "com.chuusai" %% "shapeless" % versions.shapeless
  }

  object simulacrum {
    val core = "com.github.mpilquist" %% "simulacrum" % versions.simulacrum
  }

  object apache {
    val lang = "org.apache.commons" % "commons-lang3" % versions.commons
  }
}
