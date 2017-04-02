package models.services.twitter

import challenge.sentiments.Sentiment.Sentiment
import challenge.sentiments.SentimentAnalyzer
import challenge.sentiments.SentimentAnalyzer.Sentence
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Tweet(id: Long, user: String, profileImageUrl: String, tweet: String) {
  lazy val sentiments: List[(Sentence, Sentiment)] = SentimentAnalyzer.extractSentiments(tweet)
  lazy val mainSentiment: Sentiment = SentimentAnalyzer.mainSentiment(sentiments)
  def toJson: JsObject = Json.obj(
    "id" -> id,
    "tweet" -> tweet,
    "user" -> user,
    "profileImageUrl" -> profileImageUrl,
    "sentiment" -> mainSentiment.toString
  )
}

object Tweet {
  implicit def reads: Reads[Tweet] = (
    (__ \ "id").read[Long] and
    (__ \ "user" \ "screen_name").read[String] and
    (__ \ "user" \ "profile_image_url").read[String] and
    (__ \ "text").read[String]
  )(Tweet.apply _)
  implicit def writes: OWrites[Tweet] = new OWrites[Tweet] {
    override def writes(o: Tweet): JsObject = o.toJson
  }
}
