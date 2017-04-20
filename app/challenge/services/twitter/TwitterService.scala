package challenge.services.twitter

import challenge.logs.LogContext
import challenge.services.twitter.conf.TwitterConfig
import models.services.twitter.{TwitterTopicSearchResponse, TwitterTrendingHashTagResponse}
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.ws.WSClient
import challenge.services.{RestServiceExecutor, ServiceResponse}

import scala.concurrent.ExecutionContext

class TwitterService(wSClient: WSClient, twitterConfig: TwitterConfig)(implicit ec: ExecutionContext) extends RestServiceExecutor {
  override val serviceName: String = "TwitterService"

  def search(input: String)(implicit lc: LogContext) = {
    val requestUrl = (twitterConfig.serviceUrl / "search" / "tweets.json") ?& ("q" -> input) ?& ("result_type" -> "popular") ?& ("count" -> "20")
    prepareExecutor(wSClient.url(requestUrl.toString).sign(OAuthCalculator(twitterConfig.consumerKey, twitterConfig.requestToken))).get[TwitterTopicSearchResponse].map { twitterTopicSearchResponse =>
      ServiceResponse.apply(play.api.http.Status.OK, 0, twitterTopicSearchResponse, "Twitter topic search response")
    }
  }

  def trendingHashTags(implicit lc: LogContext) = {
    val requestUrl = (twitterConfig.serviceUrl / "trends" / "place.json") ?& ("id" -> "1")
    prepareExecutor(wSClient.url(requestUrl.toString).sign(OAuthCalculator(twitterConfig.consumerKey, twitterConfig.requestToken))).get[TwitterTrendingHashTagResponse].map { twitterTrendingHashTagResponse =>
      ServiceResponse.apply(play.api.http.Status.OK, 0, twitterTrendingHashTagResponse, "Twitter topic search response")
    }
  }
}
