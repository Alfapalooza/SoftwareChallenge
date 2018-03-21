package challenge.logs

import challenge.utils.Timer

import java.util.concurrent.atomic.AtomicLong

import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.{WSRequest, WSResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object ServiceLog {
  private val serviceRequestCounter = new AtomicLong(1)

  val logger = Logger("serviceProfiler")

  def logRESTService(serviceName: String,
                     requestHolder: WSRequest,
                     requestBody: Option[String] = None)
                    (block: => Future[WSResponse])(implicit lc: LogContext, ec: ExecutionContext): Future[JsValue] = {
    def json(string: String) = Try(Json.parse(string)).getOrElse(JsString(string))

    val serviceRequestId = serviceRequestCounter.getAndIncrement()
    val log = {
      Json.obj(
        "serviceRequestId" -> serviceRequestId,
        "event" -> "request",
        "service" -> serviceName,
        "serviceKey" -> requestHolder.uri.getPath,
        "request" -> (Json.obj(
          "url" -> requestHolder.url,
          "headers" -> requestHolder.headers.map { case (k, v) => k -> v.mkString(" ") }
        ) ++ requestBody.fold(Json.obj("body" -> JsNull))(body => Json.obj("body" -> body)))
      )
    }
    logger.info((lc.details ++ log).toString())
    val timer = Timer()
    block map { response =>
      val responseTime = timer.elapsedTime
      val log =
        Json.obj(
          "serviceRequestId" -> serviceRequestId,
          "event" -> "response",
          "service" -> serviceName,
          "serviceKey" -> requestHolder.uri.getPath,
          "responseTimeMs" -> responseTime,
          "response" -> (Json.obj(
            "headers" -> {
              val keys = response.allHeaders.keySet
              keys.map(key => key -> response.header(key).getOrElse("")).toMap[String, String]
            },
            "cookies" -> response.cookies.map(_.toString)
          ) ++ Json.obj("body" -> json(response.body))
            )
        )
      logger.info((lc.details ++ log).toString())
      Json.parse(response.body)
    } recover {
      case ex: Throwable =>
        val responseTime = timer.elapsedTime
        val log =
          Json.obj(
            "serviceRequestId" -> serviceRequestId,
            "event" -> ex.getMessage,
            "service" -> serviceName,
            "serviceKey" -> requestHolder.uri.getPath,
            "responseTimeMs" -> responseTime
          )
        logger.info((lc.details ++ log).toString())
        throw ex
    }
  }

  def logService[T](serviceName: String, serviceKey: String)(block: => Future[T])(implicit lc: LogContext, ec: ExecutionContext): Future[T] = {
    val serviceRequestId = serviceRequestCounter.getAndIncrement()
    val log = {
      Json.obj(
        "serviceRequestId" -> serviceRequestId,
        "event" -> "request",
        "service" -> serviceName,
        "serviceKey" -> serviceKey
      )
    }
    logger.info((lc.details ++ log).toString())
    val timer = Timer()
    block map { result =>
      val responseTime = timer.elapsedTime
      val log =
        Json.obj(
          "serviceRequestId" -> serviceRequestId,
          "event" -> "response",
          "service" -> serviceName,
          "serviceKey" -> serviceKey,
          "responseTimeMs" -> responseTime
        )
      logger.info((lc.details ++ log).toString())
      result
    } recover {
      case ex: Throwable =>
        val responseTime = timer.elapsedTime
        val log =
          Json.obj(
            "serviceRequestId" -> serviceRequestId,
            "event" -> ex.getMessage,
            "service" -> serviceName,
            "serviceKey" -> serviceKey,
            "responseTimeMs" -> responseTime
          )
        logger.info((lc.details ++ log).toString())
        throw ex
    }
  }
}