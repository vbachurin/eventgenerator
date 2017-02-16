package eventgen.launcher.core

import eventgen.launcher.core.avro.AvroNode
import org.codehaus.jackson.annotate.JsonTypeInfo.Id

import scalaz._

/**
  * Created by Andrew on 08.02.2017.
  */
trait TreeBuilder[S] {
  def buildTree(metadata: S): String \/ State[ImmutableRandom, AvroNode[_]]
}
