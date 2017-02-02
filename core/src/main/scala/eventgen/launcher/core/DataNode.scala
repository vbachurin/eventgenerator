package eventgen.launcher.core

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData

/**
  * Created by Andrew on 31.01.2017.
  */
case class DataNode(schema: Schema, val properties: Map[String, Any]) {
  val dataRecord = new GenericData.Record(schema)

  def unwrap(value: Any): Any = value match {
    case record: DataNode => record.dataRecord
    case _ => value
  }

  properties.foreach({ case (name, value) => dataRecord.put(name, unwrap(value)) })
}
