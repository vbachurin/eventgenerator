package eventgen.launcher

import java.io.File

import eventgen.launcher.core.ExternalGenerator
import org.clapper.classutil.ClassFinder

/**
  * Created by Andrew on 19.02.2017.
  */

object ClassLoaderImplicits {

  implicit val generatorLoader = new ExternalClassLoader[ExternalGenerator[_]] {
    override def getAllInheritors(classPath: String): Seq[ExternalGenerator[_]] = {
      val currentLoader = Thread.currentThread().getContextClassLoader
      ClassFinder(new File(classPath) :: Nil).getClasses()
        .filter(c => c.interfaces.exists(i => i == "eventgen.launcher.core.ExternalGenerator"))
        .map(classInfo => {
          val classToLoad = Class.forName(classInfo.name, false, currentLoader)
          val instance = classToLoad.newInstance
          instance.asInstanceOf[ExternalGenerator[_]]
        })
    }
  }

}


