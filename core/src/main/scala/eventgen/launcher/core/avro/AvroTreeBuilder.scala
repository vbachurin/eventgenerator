package eventgen.launcher.core.avro

import eventgen.launcher.core.PrimitiveGenerators._
import eventgen.launcher.core.GenerationImplicits._
import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.Schema.{Field, Type}

import scala.collection.JavaConversions._
import scalaz._
import Scalaz._
import AvroImplicits._

/**
  * Created by Andrew on 09.02.2017.
  */
class AvroTreeBuilder(context: ExecutionContext) extends TreeBuilder[Schema] {

  def getExtendedField(schema: Schema, extGenerator: ExternalGenerator): State[RandomState, Any] = ???

  def getField(f: Field): String \/ State[RandomState, DataNode] = {
    val RangePattern = "Range\\[(Double|Int)\\]\\(from = ([-0-9]+), to = ([-0-9]+)\\)".r
    f.getProp("generator") match {
      case RangePattern(typeParam, Int(from), Int(to)) => typeParam match {
          // change to some implicit
        case "Double" => \/-(new DoubleRangeGenerator(from, to).get.map(x => AvroFieldNode(x)))
        case "Int" => \/-(new IntRangeGenerator(from, to).get.map(x => AvroFieldNode(x)))
      }
      case name => context.generators.get(name) match {
        case Some(extGenerator) => \/-(getExtendedField(f.schema(), extGenerator).map(x => AvroFieldNode(x)))
        case None => -\/(s"Cannot find generator $name")
      }
    }
  }

  override def buildTree(rootSchema: Schema): String \/ State[RandomState, DataNode] = {
    val fields = rootSchema.getFields.toList
    val fieldStates = fields.map(f => {
      val fieldVal = if (f.schema().getType == Type.RECORD)
        buildTree(f.schema())
      else
        getField(f)
      fieldVal.map((f.name(), _))
    })

    for (childrenMap <- fieldStates.sequenceU) yield composeStates(rootSchema, childrenMap.toMap)// AvroRecordNode(rootSchema, propertyMap.toMap)
  }
}
