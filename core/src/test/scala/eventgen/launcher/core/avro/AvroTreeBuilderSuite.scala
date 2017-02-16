package eventgen.launcher.core.avro

import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.scalatest.FunSuite

import scalaz.\/-

/**
  * Created by Andrew on 11.02.2017.
  */
class AvroTreeBuilderSuite extends FunSuite {

  test("should return expected pseudo random fields on the given seed") {
    val builder = new AvroTreeBuilder(ExecutionContext.get(0, Nil))
    val simpleSchemaWithDouble =
      """{
          "type": "record",
          "name": "EmployeeCreated",
          "doc": "describes a scenario when an employee has been successfully created ",
          "fields": [
                { "name": "newcomerBonus", "type": ["null","double"], "doc": "describes a bonus that newcomer may get", "generator": "Range[Double](from = 0, to = 2000)"},
                { "name": "someIntField", "type": "int", "doc": "bla", "generator": "Range[Int](from = -20, to = 20)"} ]
        }""".parseRight.toOption.get

    val treeOrError = builder.buildTree(simpleSchemaWithDouble)
    val (state, node) = treeOrError match {
      case \/-(rootState) => rootState(new ImmutableRandom(new util.Random(100)))
    }

    val avroRecord = node.value.asInstanceOf[GenericData.Record]
    assertResult(1444.0193097192869)(avroRecord.get("newcomerBonus"))
    assertResult(9)(avroRecord.get("someIntField"))
  }

}
