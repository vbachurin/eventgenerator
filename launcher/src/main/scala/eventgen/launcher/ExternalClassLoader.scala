package eventgen.launcher

/**
  * Created by Andrew on 19.02.2017.
  */
trait ExternalClassLoader[Interface] {
  def getAllInheritors(classPath: String): Seq[Interface]
}

object ExternalClassLoader {
  def load[Interface](classPath: String)(implicit loader: ExternalClassLoader[Interface]): Seq[Interface] = loader.getAllInheritors(classPath)
}