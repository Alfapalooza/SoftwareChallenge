package utils

import java.util

import challenge.guice.modules.{ApplicationModule, ConfigProviderModule, PlayComponentsModule, UserStorageModule}
import challenge.utils.test.guice.modules.{ApplicationFactoryModule, PlayComponentsFactoryModule}
import com.typesafe.play.redis.RedisModule
import play.api.{Configuration, Environment}
import play.api.cache.EhCacheModule
import play.api.i18n.I18nModule
import play.api.inject.BuiltinModule
import play.api.inject.guice.GuiceInjectorBuilder
import play.api.libs.openid.OpenIDModule
import play.api.libs.ws.ahc.AhcWSModule

object SingletonInjector {
  lazy val injector = {
    val boundCaches = new util.ArrayList[String]()
    boundCaches.add("user-storage")
    val environment = Environment.simple()
    val configuration = Configuration.load(environment) ++ Configuration(
      "play.cache.bindCaches" -> boundCaches
    )
    new GuiceInjectorBuilder(
      environment = environment,
      configuration = configuration,
      modules = Seq(
        new AhcWSModule,
        new OpenIDModule,
        new EhCacheModule,
        new BuiltinModule,
        new I18nModule,
        new ApplicationFactoryModule,
        new PlayComponentsFactoryModule,
        new ConfigProviderModule,
        new UserStorageModule
      ),
      disabled = Seq(
        classOf[ApplicationModule],
        classOf[PlayComponentsModule],
        classOf[RedisModule]
      )
    ).build()
  }
}
