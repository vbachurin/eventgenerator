package eventgen.launcher.core

import scalaz._

/**
  * Created by Andrew on 08.02.2017.
  */
trait TreeBuilder[Metadata, Node] {
  def buildTree(metadata: Metadata, executionContext: ExecutionContext): String \/ State[ImmutableRandom, Node]
}
