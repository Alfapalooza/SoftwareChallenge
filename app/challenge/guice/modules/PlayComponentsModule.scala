package challenge.guice.modules

import com.google.inject.AbstractModule
import challenge.guice.{PlayModules, PlayModulesI}

class PlayComponentsModule extends AbstractModule {
	override def configure(): Unit =
		bind(classOf[PlayModulesI]).to(classOf[PlayModules])
}