package eventgen.launcher.core.avro

import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.scalatest.FunSuite

import scalaz._

/**
  * Created by Andrew on 11.02.2017.
  */
class AvroTreeBuilderSuite extends FunSuite {

  def tryRunWithSeed(generator: String \/ State[ImmutableRandom, AvroNode[_]])(seed: Int): AvroNode[_] = {
    val (state, node) = generator match {
      case \/-(rootState) => rootState(new ImmutableRandom(new util.Random(seed)))
      case -\/(error) => fail(error)
    }
    node
  }

  test("should return expected avro record for the plain schema") {
    val builder = new AvroTreeBuilder()
    val plainSchemaWithPrimitives =
      """{
          "type": "record",
          "name": "EmployeeCreated",
          "fields": [
                { "name": "newcomerBonus", "type": ["null","double"], "generator": "Range[Double](from = 0, to = 2000)"},
                { "name": "someIntField", "type": "int", "generator": "Range[Int](from = -20, to = 20)"} ]
        }""".parseRight.toOption.get

    val nodeGenerator: Int => AvroNode[_] = tryRunWithSeed(builder.buildTree(plainSchemaWithPrimitives, ExecutionContext.get(0, Nil)))
    val avroRecord = nodeGenerator(100).value.asInstanceOf[GenericData.Record]
    assertResult(1444.0193097192869)(avroRecord.get("newcomerBonus"))
    assertResult(9)(avroRecord.get("someIntField"))
  }

  test("should return expected avro record for the hierarchical schema") {
    val builder = new AvroTreeBuilder()
    val complexSchemaWithDoubles =
      """{
           "type": "record",
           "name": "EmployeeCreated",
           "fields": [
             {
               "name": "baseSalary",
               "type": "double",
               "generator": "Range[Double](from = 75000, to = 120000)"
             },
             {
         	     "name": "nestedField",
         	     "type": {
         	       "type": "record",
                 "name": "nestedSchema",
                 "fields": [
                   {
                     "name": "baseSalary",
                     "type": "double",
                     "generator": "Range[Double](from = 75000, to = 120000)"
                   }
                 ]
         	    }
         	  }
          ]
        }""".parseRight.toOption.get

    val nodeGenerator: Int => AvroNode[_] = tryRunWithSeed(builder.buildTree(complexSchemaWithDoubles, ExecutionContext.get(0, Nil)))
    val avroRecord = nodeGenerator(101).value.asInstanceOf[GenericData.Record]
    assertResult(107486.40261838248)(avroRecord.get("baseSalary"))
    val nestedRecord = avroRecord.get("nestedField").asInstanceOf[GenericData.Record]
    assertResult(91403.41972202668)(nestedRecord.get("baseSalary"))
  }

}
