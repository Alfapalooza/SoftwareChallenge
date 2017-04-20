package challenge.services

import challenge.logs.{ErrorLog, LogContext, ServiceLog}
import challenge.services.exceptions.ModelValidationException
import play.api.libs.json.{JsError, JsSuccess, Reads}
import play.api.libs.ws.{WSRequest, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

abstract class RestServiceExecutor (implicit val ec: ExecutionContext) {
  val serviceName: String
  def prepareExecutor(
    request: WSRequest,
    _timeout: Option[FiniteDuration] = None,
    requestHook: WSRequest => WSRequest = identity,
    responseHook: WSResponse => WSResponse = identity)(implicit lc: LogContext): RestService = {
    new RestService {
      override protected val requestHolder: WSRequest = requestHook(request)
      override val timeout: FiniteDuration = _timeout.getOrElse(10 seconds)
      override protected def wrapRequest[A: ClassTag](body: Option[String])(req: => Future[WSResponse])(implicit ec: ExecutionContext, reads: Reads[A]): Future[A] = {
        ServiceLog.logServiceResponse(serviceName, requestHolder, body)(req.map(responseHook)).map { response =>
          response.validate[A] match {
            case JsSuccess(validated, _) => validated
            case JsError(errors) =>
              val ex = ModelValidationException[A](errors)
              ErrorLog.logException(ex)
              throw ex
          }
        }
      }
    }
  }
}
