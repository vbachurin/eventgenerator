package eventgen.launcher.core

import java.io.ByteArrayOutputStream

import eventgen.launcher.core.Extensions._
import eventgen.launcher.core.PrimitiveGenerator._
import fs2.Pure
import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import org.apache.avro.io.EncoderFactory

import scala.collection.JavaConversions._
import scalaz._
import Scalaz._

/**
  * Created by Andrew on 18.01.2017.
  */
trait EventGenerator[Output] {
  def generate(executionContext: ExecutionContext): String \/ Output
}

object EventGenerator {
  def forAvroSchema(schemaText: String): String \/ AvroEventGenerator = {
    for {
      validStr <- schemaText.ifNotEmpty
      parsed <- validStr.parseRight
    } yield new AvroEventGenerator(parsed)
  }
}

sealed class AvroEventGenerator(val schema: Schema) extends EventGenerator[ByteArrayOutputStream] {
  override def generate(context: ExecutionContext): String \/ ByteArrayOutputStream = {
    val writer = new GenericDatumWriter[GenericRecord]
    writer.setSchema(schema)
    val outputStream = new ByteArrayOutputStream
    val encoder = EncoderFactory.get().jsonEncoder(schema, outputStream, true)
    getRandomStream(schema, context).take(context.count).toList.foreach(n => writer.write(n.dataRecord, encoder))
    encoder.flush()
    \/-(outputStream)
  }

  def generateField(generatorDescription: String, context: ExecutionContext): String \/ Any = {
    val RangePattern = "Range\\[(Double|Int)\\]\\(from = ([-0-9]+), to = ([-0-9]+)\\)".r
    generatorDescription match {
      case RangePattern(typeParam, Int(from), Int(to)) => typeParam match {
        case "Double" => \/-(generateRange[Double](from, to))
        case "Int" => \/-(generateRange[Int](from, to))
      }
      case name => context.generators.get(name) match {
        case Some(extGenerator) => \/-(extGenerator.generate())
        case None => -\/(s"Cannot find generator $name")
      }
    }
  }

  def getRandomStream(schema: Schema, context: ExecutionContext): fs2.Stream[Pure, DataNode] = {
    generateRandomNode(schema, context) match {
      case \/-(node) => fs2.Stream.emit[Pure, DataNode](node) ++ getRandomStream(schema, context)
      case -\/(error) => fs2.Stream.fail(new Exception(error))
    }
  }

  def generateRandomNode(schema: Schema, context: ExecutionContext): String \/ DataNode = {
    val fields = schema.getFields.toList
    val fieldValues = fields.map(f => {
      val fieldVal = if (f.schema().getType == Type.RECORD)
        generateRandomNode(f.schema(), context)
      else
        generateField(f.getProp("generator"), context)
      fieldVal.map((f.name(), _))
    })

    for (propertyMap <- fieldValues.sequenceU) yield new DataNode(schema, propertyMap.toMap)
  }
}




