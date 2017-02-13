package eventgen.launcher.core

import scalaz._

/**
  * Created by Andrew on 08.02.2017.
  */
trait TreeBuilder[S] {
  def buildTree(metadata: S): String \/ State[RandomState, DataNode]

  def composeStates[A, B](metadata: S, fields: Map[String, State[A, B]])(implicit m: NamedMonoid[B, S]): State[A, B] = {
    val z = State[A, B](a => (a, m.zero(metadata)))
    fields.foldLeft(z) {
      case (f1, (fieldName, f2)) => State[A, B](a => {
        val (a1, b1) = f1(a)
        val (a2, b2) = f2(a1)
        (a2, m.append(b1, (fieldName, b2)))
      })
    }
  }
}
