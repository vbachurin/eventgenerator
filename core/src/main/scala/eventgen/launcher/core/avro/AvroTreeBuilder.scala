package eventgen.launcher.core.avro

import eventgen.launcher.core.PrimitiveGenerators._
import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Field, Type}

import scala.collection.JavaConversions._
import scalaz._
import Scalaz._
import org.apache.avro.generic.GenericData

/**
  * Created by Andrew on 09.02.2017.
  */
class AvroTreeBuilder extends TreeBuilder[Schema, AvroNode[_]] {

  def getCustomFieldState(schema: Schema, extGenerator: ExternalGenerator[_]): State[ImmutableRandom, _] = extGenerator.get

  def getRangeFieldState[T](from: Int, to: Int)(implicit rangeGen: RangeGenerator[T]): State[ImmutableRandom, AvroNode[_]] = {
    rangeGen.generate(from, to).map(AvroField[T](_))
  }

  def getFieldState(f: Field, context: ExecutionContext): String \/ State[ImmutableRandom, AvroNode[_]] = {
    val RangePattern = "Range\\[(Double|Int)\\]\\(from = ([-0-9]+), to = ([-0-9]+)\\)".r
    f.getProp("generator") match {
      case RangePattern(typeParam, Int(from), Int(to)) => typeParam match {
        case "Double" => \/-(getRangeFieldState[Double](from, to))
        case "Int" => \/-(getRangeFieldState[Int](from, to))
      }
      case name => context.generators.get(name) match {
        case Some(extGenerator) => \/-(extGenerator.get.map(AvroField(_)))
        case None => -\/(s"Cannot find generator $name")
      }
    }
  }

  override def buildTree(rootSchema: Schema, executionContext: ExecutionContext): String \/ State[ImmutableRandom, AvroNode[_]] = {
    val fields = rootSchema.getFields.toList
    val fieldStates = fields.map(f => {
      if (f.schema().getType == Type.RECORD)
        buildTree(f.schema(), executionContext).map((f.name(), _))
      else
        getFieldState(f, executionContext).map((f.name(), _))
    })

    for (childrenMap <- fieldStates.sequenceU) yield generateNodeState(rootSchema, childrenMap.toMap)
  }

  def generateNodeState(rootSchema: Schema, childrenStates: Map[String, State[ImmutableRandom, AvroNode[_]]]) = {
    State[ImmutableRandom, AvroNode[_]](rand => {
      val nativeRecord = new GenericData.Record(rootSchema)
      val (rand2, childNodes) = childrenStates.invertStatesMap(rand)
      childNodes.foreach {
        case (fieldName, node) => nativeRecord.put(fieldName, node.value)
      }
      (rand2, AvroRecord(nativeRecord))
    })
  }
}
