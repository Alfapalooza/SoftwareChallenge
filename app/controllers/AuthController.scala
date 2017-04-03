package controllers

import javax.inject.{Inject, Singleton}

import challenge.utils.auth.JsonWebTokenWrapper
import challenge.utils.auth.conf.JwtConfig
import challenge.guice.ApplicationModulesI
import io.igl.jwt.Sub
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

@Singleton
class AuthController @Inject() (modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context{
  val jwtConfig: JwtConfig = modules.configProvider.jwtClientConfig
  def authenticate: Action[AnyContent] = Action { implicit request =>
    case class Credentials(username: String, password: String)
    Form(mapping("username" -> text, "password" -> text)(Credentials.apply)(Credentials.unapply)).bindFromRequest.fold(
      errors => BadRequest(views.html.auth.signin()).withNewSession,
      credentials =>
        modules.userStorageRedisInterface.getUserByUsernamePassword(credentials.username, credentials.password).fold(BadRequest(views.html.auth.signin()).withNewSession) { user =>
          val claims = Seq(Sub(user.id.toString))
          val salt = request.headers.get("Client-IP").orElse(request.headers.get("X-Forwarded-For")).getOrElse(request.remoteAddress)
          val jwt = JsonWebTokenWrapper(jwtConfig.copy(salt = Some(salt))).encode(claims)
          Ok.withSession(jwtConfig.tokenName -> jwt)
        }
    )
  }
}
