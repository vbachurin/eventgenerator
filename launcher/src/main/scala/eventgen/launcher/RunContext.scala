package eventgen.launcher

import java.io.File

/**
  * Created by Andrew on 16.01.2017.
  */
case class RunContext(count: Int, generatorsPath: Option[String], schemaPath: String, outputType: OutputType, outputPath: Option[String])

sealed trait OutputType

case object StdOut extends OutputType

case class FileOutput(directory: File) extends OutputType

case object KafkaOutput extends OutputType