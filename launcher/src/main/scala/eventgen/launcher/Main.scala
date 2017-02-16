package eventgen.launcher

import eventgen.launcher.core.ExecutionContext

import scalaz.effect.{IO, SafeApp}

/**
  * Created by Andrew on 15.01.2017.
  */

object Main extends SafeApp {
  val usage =
    """
      |Usage:eventgen [--include generators-dir-path] [--output output-type] --schema schema-file-path --count events-count
    """.stripMargin

  override def runl(args: List[String]): IO[Unit] =
    CliParser(args, usage)(runContext => {
      for {
        schemaFileContent <- IO(scala.io.Source.fromFile(runContext.schemaPath).mkString)
        externalGenerators <- PluginSource.getFromFile(runContext.generatorsPath.get).plugins
        _ <- IO {
          ()
//          for {
//
//            generator <- EventGenerator.forAvroSchema(schemaFileContent)
//            outputStream <- generator.generate(ExecutionContext.get(runContext.count, externalGenerators))
//          } println(outputStream)
          /*if (runContext.outputType == StdOut) { */
        }
      } yield ()
    })
}
