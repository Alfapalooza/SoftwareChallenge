package challenge.utils.test.guice.modules

import challenge.guice.PlayModulesI
import challenge.utils.test.guice.PlayModulesTest
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import play.api.libs.concurrent.AkkaGuiceSupport

import scala.reflect.ClassTag

class PlayComponentsFactoryModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() =
    install(new FactoryModuleBuilder()
      .implement(classOf[PlayModulesI], classOf[PlayModulesTest])
      .build(implicitly[ClassTag[PlayModulesTest.Factory]].runtimeClass))
}
