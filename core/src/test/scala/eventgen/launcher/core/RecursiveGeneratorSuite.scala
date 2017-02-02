package eventgen.launcher.core

import org.scalacheck.Prop._
import org.scalacheck.{Properties, _}

/**
  * Created by Andrew on 30.01.2017.
  */
object RecursiveGeneratorSuite extends Properties("RecursiveGeneratorSuite") {

  val rangeBounds = for {
    from <- Gen.choose[Int](-2000, 1000)
    to <- Gen.choose[Int](from, 2000)
  } yield (from, to)

  /*property("generateField Range[Double]") = forAll(rangeBounds) {
    case (from, to) =>
      val generated = EventGenerator.generateField(s"Range[Double](from = $from, to = $to)", null).asInstanceOf[Double]
      (generated >= from) && (generated <= to)
  }*/

}
