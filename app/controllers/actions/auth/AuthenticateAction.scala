package controllers.actions.auth

import challenge.logs.LogContext
import challenge.utils.auth.JsonWebTokenWrapper
import challenge.utils.auth.conf.JwtConfig
import io.igl.jwt.Jwt
import play.api.mvc._
import challenge.services.ServiceResponse

import scala.concurrent.Future

case class AuthenticatedRequest[A](jwt: Jwt, request: Request[A]) extends WrappedRequest[A](request)
case class AuthorizedAction(jwtConfig: JwtConfig,  _failSafe: Option[Call] = None,  saltInstructions: Option[Request[_] => String] = Some(r => r.headers.get("Client-IP").orElse(r.headers.get("X-Forwarded-For")).getOrElse(r.remoteAddress))) extends ActionBuilder[AuthenticatedRequest] {
  lazy val failsafe: Future[Result] = Future.successful(_failSafe.fold(ServiceResponse(play.api.http.Status.FORBIDDEN, play.api.http.Status.FORBIDDEN, "Forbidden").toResult)(Results.Redirect(_).withNewSession))
  override def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]): Future[Result] = {
    implicit val lc: LogContext = new LogContext {
      override protected def rh: RequestHeader = request
    }
    val tokenOpt = request.headers.get(jwtConfig.tokenName).orElse(request.session.get(jwtConfig.tokenName)).orElse(request.cookies.get(jwtConfig.tokenName).fold(Option.empty[String])(c => Some(c.value)))
    tokenOpt.fold (failsafe) { token =>
      val configWSalt = saltInstructions.fold(jwtConfig)(instructions => jwtConfig.copy(salt = Some(instructions(request))))
      JsonWebTokenWrapper(configWSalt).decode(token) match {
        case Some(jwt) => block(AuthenticatedRequest(jwt, request))
        case _ => failsafe
      }
    }
  }
}

