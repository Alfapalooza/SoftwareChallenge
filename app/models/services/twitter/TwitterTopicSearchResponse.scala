package models.services.twitter

import play.api.libs.json._

case class TwitterTopicSearchResponse(tweets: Seq[Tweet]) {
  def toJson: JsObject = Json.obj(
    "tweets" -> tweets
  )
}
object TwitterTopicSearchResponse {
  implicit def reads: Reads[TwitterTopicSearchResponse] = (__ \ "statuses").readNullable[Seq[Tweet]].map { tweetsOpt =>
    TwitterTopicSearchResponse.apply(tweetsOpt.getOrElse(Nil))
  }
  implicit def writes: OWrites[TwitterTopicSearchResponse] = new OWrites[TwitterTopicSearchResponse] {
    override def writes(o: TwitterTopicSearchResponse): JsObject = o.toJson
  }
}
