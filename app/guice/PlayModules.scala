package guice

import akka.actor.ActorSystem
import com.google.inject.Inject
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

//Play dependencies that need to be injected packaged in one place.
class PlayModules @Inject()(
	override val configuration: Configuration,
	override val actorSystem: ActorSystem,
	override val ws: WSClient)(implicit ec: ExecutionContext) extends PlayModulesI

trait PlayModulesI {
	def configuration: Configuration
	def actorSystem: ActorSystem
	def ws: WSClient
}
