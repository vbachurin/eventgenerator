package eventgen.launcher.core

/**
  * Created by Andrew on 13.02.2017.
  */
trait NamedMonoid[T, M] {
  def zero(metadata: M): T

  def append(m1: T, m2: (String, T)): T
}
