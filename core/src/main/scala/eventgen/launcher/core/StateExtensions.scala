package eventgen.launcher.core

import scalaz.{State, _}

/**
  * Created by Andrew on 16.02.2017.
  */
object StateExtensions {

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
