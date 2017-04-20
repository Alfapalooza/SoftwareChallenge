package models.services.twitter

import play.api.libs.json.{JsObject, OWrites, Reads, _}

case class TrendingHashTag(private val _hashtag: String) {
  val hashtag = _hashtag.replaceAll("#", "")
  def toJson: JsObject = Json.obj (
    "hashtag" -> hashtag
  )
}

object TrendingHashTag {
  implicit def reads: Reads[TrendingHashTag] = (__ \ "name").read[String].map(TrendingHashTag.apply)
  implicit def writes: OWrites[TrendingHashTag] = new OWrites[TrendingHashTag] {
    override def writes(o: TrendingHashTag): JsObject = o.toJson
  }
}
