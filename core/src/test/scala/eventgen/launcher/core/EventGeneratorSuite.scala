package eventgen.launcher.core

import org.scalatest.FunSuite

import scalaz.-\/

/**
  * Created by Andrew on 02.02.2017.
  */
class EventGeneratorSuite  extends FunSuite {

  test("should return all parameters on getOptions") {
    // act
    val result = EventGenerator.forAvroSchema("invalid schema")

    // assert
    assertResult(-\/("org.codehaus.jackson.JsonParseException: Unexpected character ('i' (code 105)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n at [Source: java.io.StringReader@5bda8e08; line: 1, column: 2]"))(result)
  }

}
