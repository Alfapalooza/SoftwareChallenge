package utils

import challenge.guice.ApplicationModulesI
import challenge.utils.test.RestServiceResponseProducer
import challenge.utils.test.guice.ApplicationModulesTest
import play.api.inject.Injector
import play.api.libs.ws.WSResponse

trait Fixture {
  self =>
  protected lazy val injector: Injector = SingletonInjector.injector
  lazy val modules: ApplicationModulesI = injector.instanceOf[ApplicationModulesTest.Factory].apply(restServiceConsumer)
  protected def restServiceConsumer: RestServiceResponseProducer
  def withResponses(responses: (String, WSResponse)*) = new Fixture {
    override protected val restServiceConsumer = new RestServiceResponseProducer(responses)
    override protected lazy val injector = self.injector
    override lazy val modules = self.modules
  }
}

object Fixture {
  def apply(responses: (String, WSResponse)*) = new Fixture {
    override protected val restServiceConsumer = new RestServiceResponseProducer(responses)
  }
}
