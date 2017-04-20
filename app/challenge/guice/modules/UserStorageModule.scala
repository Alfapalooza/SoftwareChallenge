package challenge.guice.modules

import challenge.persistence.{UserCacheStorageInterface, UserCacheStorageInterfaceI}
import com.google.inject.AbstractModule

class UserStorageModule extends AbstractModule {
  override def configure(): Unit =
    bind(classOf[UserCacheStorageInterfaceI]).to(classOf[UserCacheStorageInterface])
}
