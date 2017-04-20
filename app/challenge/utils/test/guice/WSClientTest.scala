package challenge.utils.test.guice

import java.io.IOException

import challenge.utils.test.RestServiceResponseProducer
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws._

import scala.concurrent.Future
import scala.concurrent.duration.Duration

class WSClientTest (restServiceConsumer: RestServiceResponseProducer) extends WSClient {
  override def underlying[T]: T = ???
  override def url(_url: String): WSRequest = new WSRequest {
    override def stream(): Future[StreamedResponse] = ???
    @scala.deprecated("Use `WS.stream()` instead.")
    override def streamWithEnumerator(): Future[(WSResponseHeaders, Enumerator[Array[Byte]])] = ???
    override def withHeaders(hdrs: (String, String)*): WSRequest = this
    override def withAuth(username: String, password: String, scheme: WSAuthScheme): WSRequest = this
    override def withQueryString(parameters: (String, String)*): WSRequest = this
    override def withRequestFilter(filter: WSRequestFilter): WSRequest = this
    override def execute(): Future[WSResponse] = Future.successful(restServiceConsumer.consumeServiceResponse(_url))
    override def sign(calc: WSSignatureCalculator): WSRequest = this
    override def withVirtualHost(vh: String): WSRequest = this
    override def withRequestTimeout(timeout: Duration): WSRequest = this
    override def withMethod(method: String): WSRequest = this
    override def withProxyServer(proxyServer: WSProxyServer): WSRequest = this
    override def withFollowRedirects(follow: Boolean): WSRequest = this
    override def withBody(body: WSBody): WSRequest = this
    override val calc: Option[WSSignatureCalculator] = None
    override val url: String = _url
    override val queryString: Map[String, Seq[String]] = Map.empty[String, Seq[String]]
    override val method: String = ""
    override val followRedirects: Option[Boolean] = None
    override val body: WSBody = EmptyBody
    override val requestTimeout: Option[Int] = None
    override val virtualHost: Option[String] = None
    override val proxyServer: Option[WSProxyServer] = None
    override val auth: Option[(String, String, WSAuthScheme)] = None
    override val headers: Map[String, Seq[String]] = Map.empty[String, Seq[String]]
  }
  @scala.throws[IOException]
  override def close(): Unit = ???
}
