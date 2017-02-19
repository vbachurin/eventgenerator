package eventgen.launcher.core.avro

import java.io.ByteArrayOutputStream

import eventgen.launcher.core.NodeSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import org.apache.avro.io.EncoderFactory

/**
  * Created by Andrew on 18.02.2017.
  */
class AvroNodeSerializer extends NodeSerializer[Schema, AvroNode[_], ByteArrayOutputStream] {
  override def serialize(metadata: Schema, node: AvroNode[_]): ByteArrayOutputStream = {
    val record = node.asInstanceOf[AvroRecord]
    val writer = new GenericDatumWriter[GenericRecord]
    writer.setSchema(metadata)
    val outputStream = new ByteArrayOutputStream
    val encoder = EncoderFactory.get().jsonEncoder(metadata, outputStream, true)
    writer.write(record.value, encoder)
    encoder.flush()
    outputStream
  }
}