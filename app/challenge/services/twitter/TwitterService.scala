package challenge.services.twitter

import challenge.logs.LogContext
import challenge.services.twitter.conf.TwitterConfig
import models.services.twitter.TwitterTopicSearchResponse
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.ws.WSClient
import challenge.services.{RestServiceExecutor, ServiceResponse}

import scala.concurrent.ExecutionContext

class TwitterService(wSClient: WSClient, twitterConfig: TwitterConfig)(implicit ec: ExecutionContext) extends RestServiceExecutor {
  override val serviceName: String = "TwitterService"

  def search(input: String)(implicit lc: LogContext) = {
    val requestUrl = twitterConfig.serviceUrl ?& ("q" -> input) ?& ("result_type" -> "popular") ?& ("count" -> "20")
    executeRequest(wSClient.url(requestUrl.toString).sign(OAuthCalculator(twitterConfig.consumerKey, twitterConfig.requestToken))).get { response =>
      ServiceResponse.apply(play.api.http.Status.OK, 0, TwitterTopicSearchResponse(response), "Twitter topic search response")
    }
  }
}
