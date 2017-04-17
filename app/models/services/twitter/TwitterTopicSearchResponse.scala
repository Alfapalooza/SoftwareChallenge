package models.services.twitter

import challenge.logs.LogContext
import play.api.libs.json.{JsObject, JsValue, Json, OWrites}

case class TwitterTopicSearchResponse(tweets: List[Tweet]) {
  def toJson: JsObject = Json.obj(
    "tweets" -> tweets
  )
}
object TwitterTopicSearchResponse {
  implicit def writes: OWrites[TwitterTopicSearchResponse] = new OWrites[TwitterTopicSearchResponse] {
    override def writes(o: TwitterTopicSearchResponse): JsObject = o.toJson
  }
  def apply(json: JsValue)(implicit lc: LogContext): TwitterTopicSearchResponse = {
    val tweets = (json \ "statuses").asOpt[List[JsValue]].fold(List.empty[Tweet])(_.flatMap(_.asOpt[Tweet]))
    new TwitterTopicSearchResponse(tweets)
  }
}
