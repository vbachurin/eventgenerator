package eventgen.launcher.core

import org.apache.avro.Schema
import org.apache.avro.Schema.Parser

import scala.util.control.NonFatal
import scalaz._

/**
  * Created by Andrew on 31.01.2017.
  */
object Extensions {
  implicit class StringExtension(s: String) {
    def parseRight(): String \/ Schema = {
      try {
        val parser = new Parser
        \/-(parser.parse(s))
      } catch {
        case NonFatal(exc) => -\/(exc.toString)
      }
    }
  }

  object Int {
    def unapply(s: String): Option[Int] = try {
      Some(s.toInt)
    } catch {
      case _: java.lang.NumberFormatException => None
    }
  }
}
