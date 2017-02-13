//package eventgen.launcher.core
//
//import org.scalatest.{FunSuite, Matchers}
//
//import scalaz.{-\/, \/-}
//
///**
//  * Created by Andrew on 02.02.2017.
//  */
//class EventGeneratorSuite extends FunSuite with Matchers {
//
//  val simpleSchemaWithDouble = """{
//      "type": "record",
//      "name": "EmployeeCreated",
//      "doc": "describes a scenario when an employee has been successfully created ",
//      "fields": [ { "name": "newcomerBonus", "type": ["null","double"], "doc": "describes a bonus that newcomer may get", "generator": "Range[Double](from = 0, to = 2000)"} ]
//    }"""
//
//  test("should return parse error on invalid schema") {
//    // act
//    val generator = EventGenerator.forAvroSchema("invalid schema")
//
//    // assert
//    generator match {
//      case \/-(o) => fail("error is expected")
//      case -\/(e) => e should startWith("org.codehaus.jackson.JsonParseException: Unexpected character ('i' (code 105)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')")
//    }
//  }
//
//  test("should return avro event generator on valid schema") {
//    // act
//    val generator = EventGenerator.forAvroSchema(simpleSchemaWithDouble)
//
//    // assert
//    generator match {
//      case \/-(o) => ()
//      case -\/(e) => fail(e)
//    }
//  }
//
//  /*test("should return infinite stream on getRandomStream") {
//    // arrange
//    val target = new AvroEventGenerator(simpleSchemaWithDouble.parseRight.toOption.get)
//
//    // act
//    val stream = target.getRandomStream(ExecutionContext.get(0, Nil))
//    val props = stream.take(1).toList.head.properties
//
//    // assert
//    props should contain key ("newcomerBonus")
//  }*/
//
//}
