package eventgen.launcher.core

import scala.util.Random

/**
  * Created by Andrew on 09.02.2017.
  */
case class RandomState(mutableRandom: Random) {
  def nextInt: (RandomState, Int) = {
    val i = mutableRandom.nextInt
    (RandomState(mutableRandom), i)
  }

  def nextDouble: (RandomState, Double) = {
    val d = mutableRandom.nextDouble
    (RandomState(mutableRandom), d)
  }

  def nextLong = {
    val l = mutableRandom.nextLong
    (RandomState(mutableRandom), l)
  }

  def nextBoolean = {
    val b = mutableRandom.nextBoolean
    (RandomState(mutableRandom), b)
  }
}