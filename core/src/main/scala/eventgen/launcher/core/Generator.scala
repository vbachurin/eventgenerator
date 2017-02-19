package eventgen.launcher.core

import scalaz.State

/**
  * Created by Andrew on 08.02.2017.
  */

trait ExternalGenerator[V] {
  val name: String

  def get: State[ImmutableRandom, V]
}