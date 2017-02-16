package eventgen.launcher.core

import scalaz.std.int
import scalaz._

/**
  * Created by Andrew on 18.01.2017.
  */


object PrimitiveGenerators {

  trait RangeGenerator[T] {
    def generate(from: Int, to: Int): State[ImmutableRandom, T]
  }

  val intGen = State[ImmutableRandom, Int](state => state.nextInt)

  val doubleGen = State[ImmutableRandom, Double](state => state.nextDouble)

  val longGen = State[ImmutableRandom, Long](state => state.nextLong)

  val boolGen = State[ImmutableRandom, Boolean](state => state.nextBoolean)

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
