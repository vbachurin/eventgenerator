package eventgen.launcher.core

import scalaz.Reader

/**
  * Represents basic node for generated data of any schema and any format
  */
trait DataNode {
  def get: Any
}