package challenge.guice.modules

import challenge.persistence.{UserStorageRedisInterface, UserStorageRedisInterfaceI}
import com.google.inject.AbstractModule

class UserStorageModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[UserStorageRedisInterfaceI]).to(classOf[UserStorageRedisInterface])
}
