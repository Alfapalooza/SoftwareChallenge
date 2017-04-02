package services

import play.api.http._
import play.api.libs.json.JsValue
import play.api.libs.ws.{WSRequest, WSResponse}

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

abstract class RestService {
  protected val requestHolder: WSRequest
  protected val timeout: Duration

  protected def wrapRequest[A](body: Option[String])(req: => Future[WSResponse], resp: JsValue => A)(implicit ec: ExecutionContext): Future[A]

  def get[A](func: JsValue => A)(implicit ec: ExecutionContext): Future[A] = {
    wrapRequest(None)(requestHolder.withRequestTimeout(timeout).get(), func)
  }

  def post[A, T](body: T)(func: JsValue => A)(implicit wrt: Writeable[T], ct: ContentTypeOf[T], ec: ExecutionContext): Future[A] = {
    wrapRequest(Some(body.toString))(requestHolder.withRequestTimeout(timeout).post(body), func)
  }
}
