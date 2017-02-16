package eventgen.launcher.core

import scalaz.State

/**
  * Created by Andrew on 08.02.2017.
  */
trait Generator[V] {
  def get: State[ImmutableRandom, V]
}

trait ExternalGenerator[V] extends Generator[V] {
  val name: String
}