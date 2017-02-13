package eventgen.launcher.core

import scalaz.State

/**
  * Created by Andrew on 08.02.2017.
  */
trait Generator[V] {
  def get: State[RandomState, V]
}

trait ExternalGenerator extends Generator[Any] {
  val name: String
}