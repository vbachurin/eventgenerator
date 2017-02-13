package eventgen.launcher.core.avro

import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser

import scalaz.{\/, \/-}

/**
  * Created by Andrew on 09.02.2017.
  */
class AvroSchemaParser extends SchemaParser[Schema] {
  val nativeParser = new Parser

  override def parse(text: String): \/[String, Schema] = {
    for {
      validStr <- text.ifNotEmpty
      parsed <- validStr.parseRight
      validated <- validateAvroSchema(parsed)
    } yield validated
  }

  def validateAvroSchema(schema: Schema): String \/ Schema = \/-(schema)
}
