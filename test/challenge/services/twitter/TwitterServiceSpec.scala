package challenge.services.twitter

import challenge.utils.test.RestServiceResponseProducer
import controllers.Context
import org.scalatestplus.play.PlaySpec
import play.api.libs.ws.WSResponse
import play.api.test.FakeRequest
import utils._

import scala.concurrent.Await
import scala.concurrent.duration._

class TwitterServiceSpec extends PlaySpec with Context {
  protected class TwitterServiceFixture(responses: (String, WSResponse)*) extends Fixture {
    override protected def restServiceConsumer: RestServiceResponseProducer = new RestServiceResponseProducer(responses)
    val twitterService = modules.twitterService
  }
  "search" should {
    "successfully parse service response" in new TwitterServiceFixture (
      "https://api.twitter.com/1.1/search/tweets.json?q=test&result_type=popular&count=20" -> readFileAsJsValue("twitter_search_test.json", "/challenge/services/twitter")
    ) {
      implicit val request = FakeRequest()
      val response = Await.result(twitterService.search("test"), 20.seconds)
      response.msg mustEqual "Twitter topic search response"
      response.response.tweets.size mustEqual 15
      response.response.tweets.head.tweet mustEqual "NEW VID LIVE\n\nWe get hooked up to a lie detector test to find out the truth about each other?\n\nhttps://t.co/2DVExLqIjJ\n\n#DolanTwinsNewVideo"
    }
  }
  "trending hashtags" should {
    "successfully parse service response" in new TwitterServiceFixture(
      "https://api.twitter.com/1.1/trends/place.json?id=1" -> readFileAsJsValue("twitter_hashtag_trends.json", "/challenge/services/twitter")
    ) {
      implicit val request = FakeRequest()
      val response = Await.result(twitterService.trendingHashTags, 20.seconds)
      response.response.trendingHashTags.size mustEqual 3
      response.response.trendingHashTags.head.hashtag mustEqual "ChainedToTheRhythm"
    }
  }
}
