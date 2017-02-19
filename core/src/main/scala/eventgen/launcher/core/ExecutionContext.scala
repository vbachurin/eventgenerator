package eventgen.launcher.core

/**
  * Created by Andrew on 31.01.2017.
  */
trait ExecutionContext {

  val generators: Map[String, ExternalGenerator[_]]

  val count: Int

}

object ExecutionContext {

  def get(c: Int, gens: Seq[ExternalGenerator[_]]) = new ExecutionContext {
    val generators: Map[String, ExternalGenerator[_]] = gens.map(g => (g.name -> g)).toMap
    val count: Int = c
  }

}
