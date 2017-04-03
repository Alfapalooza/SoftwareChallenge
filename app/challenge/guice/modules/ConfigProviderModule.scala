package challenge.guice.modules

import challenge.config.{ConfigProvider, ConfigProviderI}
import com.google.inject.AbstractModule

class ConfigProviderModule extends AbstractModule{
	override def configure(): Unit =
		bind(classOf[ConfigProviderI]).to(classOf[ConfigProvider]).asEagerSingleton()
}
