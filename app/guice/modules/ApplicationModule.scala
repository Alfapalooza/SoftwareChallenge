package guice.modules

import com.google.inject.AbstractModule
import guice.{ApplicationModules, ApplicationModulesI}

class ApplicationModule extends AbstractModule{
	override def configure(): Unit =
		bind(classOf[ApplicationModulesI]).to(classOf[ApplicationModules])
}
