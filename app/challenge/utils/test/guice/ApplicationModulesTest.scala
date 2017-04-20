package challenge.utils.test.guice

import challenge.config.ConfigProviderI
import challenge.guice.{ApplicationModulesI, PlayModulesI}
import challenge.persistence.UserCacheStorageInterfaceI
import challenge.services.twitter.TwitterService
import challenge.utils.test.RestServiceResponseProducer
import com.google.inject.assistedinject.{Assisted, AssistedInject}

import scala.concurrent.ExecutionContext

class ApplicationModulesTest @AssistedInject() (
  override val configProvider: ConfigProviderI,
  override val userStorageRedisInterface: UserCacheStorageInterfaceI,
  val playModulesFactory: PlayModulesTest.Factory,
  @Assisted() val restServiceConsumer: RestServiceResponseProducer
)(implicit ec: ExecutionContext) extends ApplicationModulesI {
  override def playComponents: PlayModulesI = playModulesFactory(new WSClientTest(restServiceConsumer))
  override def twitterService: TwitterService = new TwitterService(playComponents.ws, configProvider.twitterConfig)
}

object ApplicationModulesTest {
  trait Factory {
    def apply(restServiceConsumer: RestServiceResponseProducer): ApplicationModulesTest
  }
}

