package challenge.utils.test.guice.modules

import challenge.guice.ApplicationModulesI
import challenge.utils.test.guice.ApplicationModulesTest
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

import scala.reflect.ClassTag

class ApplicationFactoryModule extends AbstractModule {
  override def configure() =
    install(new FactoryModuleBuilder()
      .implement(classOf[ApplicationModulesI], classOf[ApplicationModulesTest])
      .build(implicitly[ClassTag[ApplicationModulesTest.Factory]].runtimeClass))
}
