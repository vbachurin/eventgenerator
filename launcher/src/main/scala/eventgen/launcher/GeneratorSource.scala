package eventgen.launcher

import eventgen.launcher.core.ExternalGenerator

import scalaz.effect.IO

/**
  * Created by Andrew on 23.01.2017.
  */

trait PluginSource {

  def plugins: IO[List[ExternalGenerator]]

}

object PluginSource {

  def getFromFile(path: String): PluginSource = new PluginSource {

    def plugins: IO[List[ExternalGenerator]] = IO(Nil)

  }

 /* def getExternalGenerators(generatorsPathOpt: Option[String]): IO[List[ExternalGenerator]] = {
    generatorsPathOpt match {
      case Some(generatorsPath) => IO(Nil)
      case None => IO(Nil)
    }
  }*/

}
