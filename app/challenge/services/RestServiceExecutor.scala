package challenge.services

import challenge.logs.{LogContext, ServiceLog}
import play.api.libs.json.JsValue
import play.api.libs.ws.{WSRequest, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

abstract class RestServiceExecutor (implicit val ec: ExecutionContext) {
  val serviceName: String
  def executeRequest(
    request: WSRequest,
    _timeout: Option[FiniteDuration] = None,
    requestHook: WSRequest => WSRequest = identity,
    responseHook: WSResponse => WSResponse = identity)(implicit lc: LogContext): RestService = {
    new RestService {
      override protected val requestHolder: WSRequest = requestHook(request)
      override val timeout: FiniteDuration = _timeout.getOrElse(10 seconds)
      override protected def wrapRequest[A](body: Option[String])(req: => Future[WSResponse], resp: JsValue => A)(implicit ec: ExecutionContext): Future[A] = {
        ServiceLog.logServiceResponse(serviceName, requestHolder, body)(req.map(responseHook)).map(resp)
      }
    }
  }
}
