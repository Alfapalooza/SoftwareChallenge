package challenge.logs

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.RequestHeader

/*
  Author: martin.allaire 2016.
 */
trait LogContext {
  protected def rh: RequestHeader

  def details: JsObject = {
    val info = Json.obj(
      "header" -> rh.headers.toString,
      "cookies" -> rh.cookies.toString,
      "domain" -> rh.domain,
      "host" -> rh.host,
      "path" -> rh.path
    )
    info
  }
}