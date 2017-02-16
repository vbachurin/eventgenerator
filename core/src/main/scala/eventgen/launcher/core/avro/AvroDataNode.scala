package eventgen.launcher.core.avro

import org.apache.avro.generic.GenericData

/**
  * Created by Andrew on 08.02.2017.
  */


sealed trait AvroNode[V] {
  val value: V
}

case class AvroRecord(value: GenericData.Record) extends AvroNode[GenericData.Record]

case class AvroField[V](value: V) extends AvroNode[V]

