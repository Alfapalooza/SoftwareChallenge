package challenge.services.twitter

import challenge.config.exceptions.ConfigurationNotFoundException
import challenge.logs.LogContext
import challenge.utils.{Host, Protocol, Url}
import models.services.twitter.TwitterTopicSearchResponse
import play.api.Configuration
import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}
import play.api.libs.ws.WSClient
import challenge.services.{RestServiceExecutor, ServiceResponse}

import scala.concurrent.ExecutionContext

class TwitterService(wSClient: WSClient, configuration: Configuration)(implicit ec: ExecutionContext) extends RestServiceExecutor {
  override val serviceName: String = "TwitterService"
  private val consumerKey = ConsumerKey(
    configuration.getString("challenge.services.twitter.consumerKey").getOrElse(throw ConfigurationNotFoundException("challenge.services.twitter.consumerKey")),
    configuration.getString("challenge.services.twitter.consumerSecret").getOrElse(throw ConfigurationNotFoundException("challenge.services.twitter.consumerSecret"))
  )
  private val requestToken = RequestToken(
    configuration.getString("challenge.services.twitter.tokenKey").getOrElse(throw ConfigurationNotFoundException("challenge.services.twitter.tokenKey")),
    configuration.getString("challenge.services.twitter.tokenSecret").getOrElse(throw ConfigurationNotFoundException("challenge.services.twitter.tokenSecret"))
  )
  private val baseUrl = Url(Host(Protocol.https, configuration.getString("challenge.services.twitter.host").getOrElse("api.twitter.com"))) / "1.1" / "search" / "tweets.json"
  def search(input: String)(implicit lc: LogContext) = {
    val requestUrl = baseUrl ?& ("q" -> input) ?& ("result_type" -> "popular") ?& ("count" -> "20")
    executeRequest(wSClient.url(requestUrl.toString).sign(OAuthCalculator(consumerKey, requestToken))).get { response =>
      ServiceResponse.apply(play.api.http.Status.OK, 0, TwitterTopicSearchResponse(response), "Twitter topic search response")
    }
  }
}
