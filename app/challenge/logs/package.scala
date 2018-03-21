package challenge.logs

import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSRequest

package object log {
  implicit class RequestHolderCookies(val ws: WSRequest) extends AnyVal {
    def cookies: Map[String, String] = {
      val assign = """([^=]*)=(.*)""".r
      ws.headers.getOrElse("Cookie", Seq()).flatMap(_.split(";")).collect { case assign(k, v) => k -> v }.toMap
    }
  }

  implicit class RichLogger(logger: play.api.Logger) {
    val logTimeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ")

    private[this] def timestamp = logTimeFormatter.format(System.currentTimeMillis())

    def error(json: => JsObject) =
      if (logger.isErrorEnabled) logger.error(Json.obj("@timestamp" -> timestamp, "@fields" -> (Json.obj("logLevel" -> "error") ++ json)).toString)

    def warn(json: => JsObject) =
      if (logger.isWarnEnabled) logger.warn(Json.obj("@timestamp" -> timestamp, "@fields" -> (Json.obj("logLevel" -> "warn") ++ json)).toString)

    def info(json: => JsObject) =
      if (logger.isInfoEnabled) logger.info(Json.obj("@timestamp" -> timestamp, "@fields" -> (Json.obj("logLevel" -> "info") ++ json)).toString)

    def debug(json: => JsObject) =
      if (logger.isDebugEnabled) logger.debug(Json.obj("@timestamp" -> timestamp, "@fields" -> (Json.obj("logLevel" -> "debug") ++ json)).toString)

    def trace(json: => JsObject) =
      if (logger.isTraceEnabled) logger.trace(Json.obj("@timestamp" -> timestamp, "@fields" -> (Json.obj("logLevel" -> "trace") ++ json)).toString)
  }
}
