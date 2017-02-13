package eventgen.launcher.core

import scalaz.{Reader, State}

/**
  * Created by Andrew on 18.01.2017.
  */

object GenerationImplicits {
  implicit val intGen = State[RandomState, Int](state => state.nextInt)

  implicit val doubleGen = State[RandomState, Double](state => state.nextDouble)

  implicit val longGen = State[RandomState, Long](state => state.nextLong)

  implicit val boolGen = State[RandomState, Boolean](state => state.nextBoolean)
}

object PrimitiveGenerators {

  import GenerationImplicits._

  class IntRangeGenerator(from: Int, to: Int)(implicit generator: State[RandomState, Int]) extends Generator[Int] {
    override def get: State[RandomState, Int] = generator.map(value => {
      val h = to - from
      from + value * h
    })
  }

  class DoubleRangeGenerator(from: Int, to: Int)(implicit generator: State[RandomState, Double]) extends Generator[Double] {
    override def get: State[RandomState, Double] = generator.map(value => {
      val h = to - from
      from + value * h
    })
  }

}
