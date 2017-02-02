package eventgen.launcher

/**
  * Created by Andrew on 16.01.2017.
  */
case class RunContext(count: Int, generatorsPath: Option[String], schemaPath: String, outputType: OutputType)

sealed trait OutputType

case object StdOut extends OutputType

case object File extends OutputType