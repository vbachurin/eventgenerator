package eventgen.launcher.core

/**
  * Created by Andrew on 18.01.2017.
  */

trait PrimitiveGenerator[+T] {

  def generate(): T

}

trait Range[T] {

  def generate(from: T, to: T): T

}

object PrimitiveGenerator {
  implicit val intGen = new PrimitiveGenerator[Int] {
    override def generate: Int = {
      util.Random.nextInt
    }
  }

  implicit val doubleGen = new PrimitiveGenerator[Double] {
    override def generate: Double = {
      util.Random.nextDouble
    }
  }

  implicit val longGen = new PrimitiveGenerator[Long] {
    override def generate: Long = {
      util.Random.nextLong
    }
  }

  implicit val boolGen = new PrimitiveGenerator[Boolean] {
    override def generate: Boolean = {
      util.Random.nextBoolean
    }
  }

  implicit val doubleRange = new Range[Double] {
    override def generate(from: Double, to: Double): Double = {
      val r = doubleGen.generate
      val h = to - from
      from + r * h
    }
  }

  implicit val intRange = new Range[Int] {
    override def generate(from: Int, to: Int): Int = {
      val r = intGen.generate
      val h = to - from
      from + r * h
    }
  }

  sealed trait RangeSelector[T] {
    def generateRange(generated: => T): T
  }

  def generatePrimitive[T](implicit gen: PrimitiveGenerator[T]): T = {
    gen.generate
  }

  def generateRange[T](from: T, to: T)(implicit range: Range[T]): T = {
    range.generate(from, to)
  }
}
