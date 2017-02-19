package eventgen.launcher.core

import java.io.ByteArrayOutputStream

import eventgen.launcher.core.avro._
import org.apache.avro.Schema

import scalaz._

/**
  * Created by Andrew on 19.02.2017.
  */
trait RandomGenerationService[Metadata, Node, Output] {

  val schemaParser: SchemaParser[Metadata]
  val treeBuilder: TreeBuilder[Metadata, Node]
  val nodeSerializer: NodeSerializer[Metadata, Node, Output]

  def getRandomOutputs(metadata: String, context: ExecutionContext): String \/ Kleisli[Seq, ImmutableRandom, Output] = {
    for {
      schema <- schemaParser.parse(metadata)
      nodeGenerator <- treeBuilder.buildTree(schema, context)
    } yield Kleisli[Seq, ImmutableRandom, Output](seed => getRandomStream(nodeGenerator)(seed).take(context.count).toList.map(x => nodeSerializer.serialize(schema, x)))
  }

  def getRandomStream(nodeGenerator: State[ImmutableRandom, Node])(seed: ImmutableRandom): Stream[Node] = {
    val (newState, node) = nodeGenerator(seed)
    node #:: getRandomStream(nodeGenerator)(newState)
  }

}

object RandomGenerationService {
  def getAvro = new RandomGenerationService[Schema, AvroNode[_], ByteArrayOutputStream] {
    val schemaParser = new AvroSchemaParser
    val treeBuilder = new AvroTreeBuilder
    val nodeSerializer = new AvroNodeSerializer
  }
}