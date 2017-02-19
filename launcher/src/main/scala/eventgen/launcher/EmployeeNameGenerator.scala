package eventgen.launcher

import eventgen.launcher.core.{ExternalGenerator, ImmutableRandom}

import scalaz._

/**
  * Created by Andrew on 19.02.2017.
  */
class EmployeeNameGenerator extends ExternalGenerator[String] {
  override val name: String = "EmployeeNameGenerator"

  override def get: State[ImmutableRandom, String] = State.state[ImmutableRandom, String]("Arnold Schwarzenegger")
}
