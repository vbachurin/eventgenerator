package eventgen.launcher.core

import scalaz.std.int
import scalaz._

/**
  * Created by Andrew on 18.01.2017.
  */

trait RangeGenerator[T] {
  def generate(from: Int, to: Int): State[ImmutableRandom, T]
}

object PrimitiveGenerators {

  val intGen = State[ImmutableRandom, Int](state => {
    val i = state.mutableRandom.nextInt
    (ImmutableRandom(state.mutableRandom), i)
  })

  val doubleGen = State[ImmutableRandom, Double](state => {
    val d = state.mutableRandom.nextDouble
    (ImmutableRandom(state.mutableRandom), d)
  })

  val longGen = State[ImmutableRandom, Long](state => {
    val l = state.mutableRandom.nextLong
    (ImmutableRandom(state.mutableRandom), l)
  })

  val boolGen = State[ImmutableRandom, Boolean](state => {
    val b = state.mutableRandom.nextBoolean
    (ImmutableRandom(state.mutableRandom), b)
  })

  implicit val intRangeGenerator = new RangeGenerator[Int] {
    override def generate(from: Int, to: Int): State[ImmutableRandom, Int] = intGen.map(value => {
      val h = to - from
      from + value % h
    })
  }

  implicit val doubleRangeGenerator = new RangeGenerator[Double] {
    override def generate(from: Int, to: Int): State[ImmutableRandom, Double] = doubleGen.map(value => {
      val h = to - from
      from + value * h
    })
  }

}
