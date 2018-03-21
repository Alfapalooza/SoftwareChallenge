package challenge.logs

import play.api.Logger
import play.api.libs.json.{JsObject, Json}

object ErrorLog {
  val logger = Logger("error-profiler")

  private def serializeException(ex: Throwable): JsObject = {
    val causedBy =
      Option(ex.getCause) match {
        case Some(cause) =>
          Json.obj("causedBy" -> serializeException(cause))
        case None =>
          Json.obj()
      }
    Json.obj(
      "class" -> ex.getClass.getName(),
      "message" -> ex.getMessage,
      "stackTrace" -> ex.getStackTrace.map(_.toString)
    ) ++ causedBy
  }

  def logException(ex: Throwable)(implicit lc: LogContext): Unit = {
    def info = Json.obj(
      "message" -> ex.getMessage,
      "exception" -> serializeException(ex)
    )
    logger.error((lc.details ++ info).toString())
  }
}
