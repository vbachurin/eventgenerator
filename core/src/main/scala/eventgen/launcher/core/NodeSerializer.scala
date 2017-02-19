package eventgen.launcher.core

/**
  * Created by Andrew on 18.02.2017.
  */
trait NodeSerializer[Metadata, Node, Output] {
  def serialize(metadata: Metadata, node: Node): Output
}
