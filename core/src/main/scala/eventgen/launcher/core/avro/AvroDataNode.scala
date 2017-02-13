package eventgen.launcher.core.avro

import eventgen.launcher.core._
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.Record

import scalaz.{Applicative, Id, Kleisli, Reader}


/**
  * Created by Andrew on 08.02.2017.
  */

/*
trait AvroDataNode[V] extends DataNode[Schema, V]

case class AvroRecord(schema: Schema, children: Map[String, AvroDataNode[_]]) extends AvroDataNode[GenericData.Record] {
  val childrenCalls = for ((name, value) <- children) yield (name, value.get)
  def get: RandomReader[Record] = Reader[RandomState, GenericData.Record](x => {
    val genericRecord = new GenericData.Record(schema)
    for ((name, value) <- childrenCalls) genericRecord.put(name, value.get.run(x))
    genericRecord
  })
}

case class AvroField[V](schema: Schema, generator: Generator[V]) extends AvroDataNode[V] {
  def get: RandomReader[V] = generator.get
}
*/

case class AvroRecordNode(val record: GenericData.Record) extends DataNode {
  override def get: Any = record
}

case class AvroFieldNode(value: Any) extends DataNode {
  override def get: Any = value
}

object AvroImplicits {

  implicit object AvroRecordMonoid extends NamedMonoid[DataNode, Schema] {
    override def zero(metadata: Schema): DataNode = new AvroRecordNode(new GenericData.Record(metadata))

    override def append(m1: DataNode, m2: (String, DataNode)): DataNode = {
      m1 match {
        case AvroRecordNode(record) => {
          record.put(m2._1, m2._2.get)
          m1
        }
      }
    }
  }

}

