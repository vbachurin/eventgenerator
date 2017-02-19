package eventgen.launcher.core.avro

import eventgen.launcher.core.{ExecutionContext, ImmutableRandom, NodeSerializer, RandomGenerationService, SchemaParser, TreeBuilder}
import org.scalatest.FunSuite

import scalaz._

/**
  * Created by Andrew on 19.02.2017.
  */
class RandomGenerationServiceSuite extends FunSuite {

  test("should return specific count of random nodes") {
    // arrange
    val target = new RandomGenerationService[String, Int, String] {
      val schemaParser = new SchemaParser[String] {
        def parse(schema: String): \/[String, String] = if (schema == "raw schema") \/-("parsed schema") else -\/("error")
      }
      val treeBuilder = new TreeBuilder[String, Int] {
        def buildTree(metadata: String, executionContext: ExecutionContext): \/[String, State[ImmutableRandom, Int]] = {
          if (metadata == "parsed schema") {
            \/-(State[ImmutableRandom, Int](r => {
              val i = r.mutableRandom.nextInt
              (ImmutableRandom(r.mutableRandom), i)
            }))
          }
          else {
            -\/("error")
          }
        }
      }
      val nodeSerializer = new NodeSerializer[String, Int, String] {
        def serialize(metadata: String, node: Int): String = node.toString
      }
    }

    // act
    val randomNodes = target.getRandomOutputs("raw schema", ExecutionContext.get(5, Nil)).toOption.get

    // assert
    assertResult(Seq("-1244746321", "1060493871", "-1826063944", "1976922248", "-230127712"))(randomNodes(ImmutableRandom(new util.Random(1000))))
  }

}
