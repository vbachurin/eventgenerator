package eventgen.launcher.core

import scalaz.\/

/**
  * Created by Andrew on 09.02.2017.
  */
trait SchemaParser[S] {
  def parse(schema: String): String \/ S
}