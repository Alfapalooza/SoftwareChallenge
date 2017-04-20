package models.services.twitter

import challenge.services.exceptions.ModelValidationException
import play.api.libs.json._

case class TwitterTrendingHashTagResponse(trendingHashTags: Seq[TrendingHashTag]) {
  def toJson: JsObject = Json.obj(
    "hashtags" -> trendingHashTags
  )
}

object TwitterTrendingHashTagResponse {
  implicit def reads: Reads[TwitterTrendingHashTagResponse] = __.read[Seq[JsValue]].map(_.headOption.fold(Seq.empty[TrendingHashTag]) { head =>
    (head \ "trends").validate[Seq[TrendingHashTag]] match {
      case JsSuccess(trendingHashTags, _) => trendingHashTags
      case JsError(errors) => throw ModelValidationException[TrendingHashTag](errors)
    }
  }).map(TwitterTrendingHashTagResponse.apply)
  implicit def writes: OWrites[TwitterTrendingHashTagResponse] = new OWrites[TwitterTrendingHashTagResponse] {
    override def writes(o: TwitterTrendingHashTagResponse): JsObject = o.toJson
  }
}