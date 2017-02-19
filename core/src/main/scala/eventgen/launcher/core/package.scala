package eventgen.launcher

import org.apache.avro.Schema
import org.apache.avro.Schema.Parser

import scala.util.control.NonFatal
import scalaz._

/**
  * Created by Andrew on 08.02.2017.
  */
package object core {

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

  implicit class MapOfStates[Key, StateType, Node](map: Map[Key, State[StateType, Node]]) {
    def invertStatesMap: State[StateType, Map[Key, Node]]
    = {
      val z = State.state[StateType, Map[Key, Node]](Map())
      map.foldLeft(z) {
        case (rootState, (fieldKey, fieldState)) => State[StateType, Map[Key, Node]](state1 => {
          val (state2, map) = rootState(state1)
          val (state3, newNode) = fieldState(state2)
          (state3, map + (fieldKey -> newNode))
        })
      }
    }
  }

}
