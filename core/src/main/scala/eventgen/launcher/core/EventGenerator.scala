//package eventgen.launcher.core
//
//import java.io.ByteArrayOutputStream
//import eventgen.launcher.core.PrimitiveGenerator._
//import fs2.Pure
//import org.apache.avro.Schema
//import org.apache.avro.Schema.Type
//import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
//import org.apache.avro.io.EncoderFactory
//
//import scala.collection.JavaConversions._
//import scalaz._
//import Scalaz._
//
///**
//  * Created by Andrew on 18.01.2017.
//  */
//
//trait EventGenerator[S] {
//  def generate(executionContext: ExecutionContext): String \/ Output
//}
//
//object EventGenerator {
//
//  def getAvroSchema(schemaText: String)(): String \/ AvroEventGenerator = {
//    for {
//      validStr <- schemaText.ifNotEmpty
//      parsed <- validStr.parseRight
//      validated <- validateAvroSchema(parsed)
//    } yield validated
//  }
//
//  def validateAvroSchema(schema: Schema): String \/ Schema = \/-(schema)
//}
//
//sealed class AvroEventGenerator(val schema: Schema) extends EventGenerator[ByteArrayOutputStream] {
//  override def generate(context: ExecutionContext): String \/ ByteArrayOutputStream = {
//    val writer = new GenericDatumWriter[GenericRecord]
//    writer.setSchema(schema)
//    val outputStream = new ByteArrayOutputStream
//    val encoder = EncoderFactory.get().jsonEncoder(schema, outputStream, true)
//    getRandomStream(context, new RandomState(new util.Random)).take(context.count).toList.foreach(n => writer.write(n.dataRecord, encoder))
//    encoder.flush()
//    \/-(outputStream)
//  }
//
//  def generateField(generatorDescription: String, context: ExecutionContext): Reader[RandomState, Any] = {
//    val RangePattern = "Range\\[(Double|Int)\\]\\(from = ([-0-9]+), to = ([-0-9]+)\\)".r
//    generatorDescription match {
//      case RangePattern(typeParam, Int(from), Int(to)) => typeParam match {
//        case "Double" => generateRange[Double](from, to).map(x => x.asInstanceOf[Any])
//        case "Int" => generateRange[Int](from, to).map(x => x.asInstanceOf[Any])
//      }
//      /*case name => context.generators.get(name).get.genera match {
//        case Some(extGenerator) => \/-(extGenerator.generate())
//        case None => -\/(s"Cannot find generator $name")
//      }*/
//    }
//  }
//
//  def getRandomStream(context: ExecutionContext, random: RandomState): fs2.Stream[Pure, DataNode] = {
//    fs2.Stream.emit[Pure, DataNode](generateRandomNode(schema, context).run(random))
//  }
//
//  def generateRandomNode(schema: Schema, context: ExecutionContext): Reader[RandomState, DataNode] = {
//    val fields = schema.getFields.toList
//    val fieldValues = fields.map(f => {
//      val fieldVal = if (f.schema().getType == Type.RECORD)
//        generateRandomNode(f.schema(), context)
//      else
//        generateField(f.getProp("generator"), context)
//      fieldVal.map((f.name(), _))
//    })
//
//    Reader(x => new DataNode(schema, Map()))
//    //for (propertyMap <- fieldValues.sequenceU) yield new DataNode(schema, propertyMap.toMap)
//  }
//}
//
//
//
//
