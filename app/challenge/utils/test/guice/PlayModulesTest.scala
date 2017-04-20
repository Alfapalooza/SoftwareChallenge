package challenge.utils.test.guice

import akka.actor.ActorSystem
import challenge.guice.PlayModulesI
import com.google.inject.assistedinject.{Assisted, AssistedInject}
import play.api.Configuration
import play.api.libs.ws.WSClient

class PlayModulesTest @AssistedInject() (
  override val configuration: Configuration,
  override val actorSystem: ActorSystem,
  @Assisted() override val ws: WSClient
) extends PlayModulesI

object PlayModulesTest {
  trait Factory {
    def apply(wSClient: WSClient): PlayModulesTest
  }
}
