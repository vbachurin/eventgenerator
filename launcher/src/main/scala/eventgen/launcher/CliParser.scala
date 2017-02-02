package eventgen.launcher

import scalaz._
import scalaz.Scalaz._
import scalaz.effect.IO
import scalaz.effect.IO._

/**
  * Created by Andrew on 16.01.2017.
  */
object CliParser {

  type OptionMap = Map[Symbol, String]

  def getOutputType(outputTypeStr: String): String \/ OutputType = outputTypeStr match {
    case "stdout" => \/-(StdOut)
    case "file" => \/-(File)
    case str => -\/(s"Cannot convert $str")
  }

  def mapParseIntNel(countStr: ValidationNel[String, String]): ValidationNel[String, Int] =
    countStr match {
      case Success(x) => x.parseInt.leftMap(
        e => s"must be int: ${e.getMessage}"
      ).toValidationNel
      case f@Failure(r) => f
    }

  def getRunInfo(map: OptionMap): ValidationNel[String, RunContext] = {
    val countStr = map.get('count).toSuccessNel("--count is mandatory parameter")
    val count = mapParseIntNel(countStr)
    val generatorsPath = map.get('generatorsPath)
    val schemaPath = map.get('schemaPath).toSuccessNel("--schema is mandatory parameter")
    val outputType = map.get('outputType).toRightDisjunction("--output is mandatory parameter").flatMap(x => getOutputType(x)).validation.toValidationNel
    (count ⊛ schemaPath ⊛ outputType) (RunContext(_, generatorsPath, _, _))
  }

  def getOptions(args: List[String]): ValidationNel[String, RunContext] = {
    def nextOption(map: OptionMap, list: List[String]): String \/ OptionMap = list match {
      case Nil => \/-(map)
      case "--count" :: eventsCount :: tail => nextOption(map + ('count -> eventsCount), tail)
      case "--include" :: generatorsPath :: tail => nextOption(map + ('generatorsPath -> generatorsPath), tail)
      case "--output" :: outputType :: tail => nextOption(map + ('outputType -> outputType), tail)
      case "--schema" :: schemaPath :: tail => nextOption(map + ('schemaPath -> schemaPath), tail)
      case _@invalid => -\/(invalid.mkString(" "))
    }

    nextOption(Map(), args) match {
      case \/-(options) => getRunInfo(options)
      case -\/(error) => Failure(s"Invalid parameter $error").toValidationNel
    }
  }

  def apply(args: List[String], usage: String)(runOnValidParams: RunContext => IO[Unit]): IO[Unit] = {
    if (args.length == 0)
      putStrLn(usage)
    else
      CliParser.getOptions(args) match {
        case Success(runInfo) => runOnValidParams(runInfo)
        case Failure(errors) => putStr(errors.toList.mkString(sys.props("line.separator")))
      }
  }
}
