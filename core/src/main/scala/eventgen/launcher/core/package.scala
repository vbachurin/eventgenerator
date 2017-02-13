package eventgen.launcher

import org.apache.avro.Schema
import org.apache.avro.Schema.Parser

import scala.util.control.NonFatal
import scalaz._

/**
  * Created by Andrew on 08.02.2017.
  */
package object core {

  type RandomReader[T] = Kleisli[Id.Id, RandomState, T]

  implicit class StringExtension(s: String) {
    def parseRight: String \/ Schema = {
      try {
        val parser = new Parser
        \/-(parser.parse(s))
      } catch {
        case NonFatal(exc) => -\/(exc.getMessage)
      }
    }

    def ifNotEmpty: String \/ String = {
      if (s.isEmpty) {
        -\/("Schema is empty")
      }
      else {
        \/-(s)
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
