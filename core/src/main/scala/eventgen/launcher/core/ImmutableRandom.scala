package eventgen.launcher.core

import scala.util.Random

/**
  * Created by Andrew on 09.02.2017.
  */
case class ImmutableRandom(mutableRandom: Random) {
  def nextInt: (ImmutableRandom, Int) = {
    val i = mutableRandom.nextInt
    (ImmutableRandom(mutableRandom), i)
  }

  def nextDouble: (ImmutableRandom, Double) = {
    val d = mutableRandom.nextDouble
    (ImmutableRandom(mutableRandom), d)
  }

  def nextLong = {
    val l = mutableRandom.nextLong
    (ImmutableRandom(mutableRandom), l)
  }

  def nextBoolean = {
    val b = mutableRandom.nextBoolean
    (ImmutableRandom(mutableRandom), b)
  }
}
