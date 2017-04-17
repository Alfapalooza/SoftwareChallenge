package challenge.guice

import challenge.config.ConfigProviderI
import challenge.persistence.UserStorageRedisInterfaceI
import com.google.inject.Inject
import challenge.services.twitter.TwitterService

import scala.concurrent.ExecutionContext

class ApplicationModules @Inject()(
	override val playComponents: PlayModulesI,
	override val configProvider: ConfigProviderI,
	override val userStorageRedisInterface: UserStorageRedisInterfaceI
)(implicit val ec: ExecutionContext) extends ApplicationModulesI {
	override lazy val twitterService = new TwitterService(playComponents.ws, configProvider.twitterConfig)
}

trait ApplicationModulesI {
	def playComponents: PlayModulesI
	def configProvider: ConfigProviderI
	def userStorageRedisInterface: UserStorageRedisInterfaceI
	def twitterService: TwitterService
}

