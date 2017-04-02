package guice

import com.google.inject.Inject
import services.twitter.TwitterService

import scala.concurrent.ExecutionContext

class ApplicationModules @Inject()(
	override val playComponents: PlayModulesI
)(implicit val ec: ExecutionContext) extends ApplicationModulesI {
	override lazy val twitterService = new TwitterService(playComponents.ws, playComponents.configuration)
}

trait ApplicationModulesI {
	def playComponents: PlayModulesI
	def twitterService: TwitterService
}

