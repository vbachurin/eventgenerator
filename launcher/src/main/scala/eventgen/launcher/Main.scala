package eventgen.launcher

import java.io.{File, FileOutputStream}

import eventgen.launcher.core._
import eventgen.launcher.ClassLoaderImplicits._

import scalaz._
import scalaz.effect._

/**
  * Created by Andrew on 15.01.2017.
  */

object Main extends SafeApp {

  val usage =
    """
      |Usage:eventgen [--include generators-dir-path --output-path output-dir-path] --output output-type --schema schema-file-path --count events-count
    """.stripMargin

  override def runl(args: List[String]): IO[Unit] =
    CliParser(args, usage)(runContext => IO {
      val externalGenerators = runContext.generatorsPath match {
        case Some(generatorsPath) => ExternalClassLoader.load[ExternalGenerator[_]](generatorsPath)
        case None => Nil
      }

      val schemaContent = scala.io.Source.fromFile(runContext.schemaPath).mkString
      val context = ExecutionContext.get(runContext.count, externalGenerators)
      val initialSeed = new ImmutableRandom(new util.Random)
      RandomGenerationService.getAvro.getRandomOutputs(schemaContent, context) match {
        case -\/(error) => println(error)
        case \/-(randomGenerator) =>
          runContext.outputType match {
            case StdOut => randomGenerator(initialSeed).foreach(println(_))
            case FileOutput(directory) => {
              randomGenerator(initialSeed).zipWithIndex.foreach { case (output, i) => {
                val outputStream = new FileOutputStream(new File(directory, s"$i.avro.txt"))
                output.writeTo(outputStream)
              }
              }
            }
            case KafkaOutput => {
              ???
            }
          }

      }
    })
}
