package eventgen.launcher

import org.scalatest.FunSuite

import scalaz.{Failure, NonEmptyList, Success}

/**
  * Created by Andrew on 16.01.2017.
  */
class CliParserSuite extends FunSuite {

  test("should return all parameters on getOptions") {
    // arrange
    val inputArgs = "--count 5000 --include ~/generators --schema ~/schema/event-1.asvc --output stdout".split(" ").toList

    // act
    val result = CliParser.getOptions(inputArgs)

    // assert
    assertResult(Success(RunContext(5000, Some("~/generators"), "~/schema/event-1.asvc", StdOut)))(result)
  }

  test("should return all parameters failures on getOptions") {
    // arrange
    val inputArgs = "--include ~/generators --schema ~/schema/event-1.asvc --output invalidValue".split(" ").toList

    // act
    val result = CliParser.getOptions(inputArgs)

    // assert
    assertResult(Failure(NonEmptyList("--count is mandatory parameter", "Cannot convert invalidValue")))(result)
  }

}
