package controllers

import javax.inject.{Inject, Singleton}

import challenge.utils.auth.JsonWebTokenWrapper
import challenge.utils.auth.conf.JwtConfig
import challenge.guice.ApplicationModulesI
import io.igl.jwt.Sub
import models.{Alert, Level, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

@Singleton
class AuthController @Inject() (modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context{
  val jwtConfig: JwtConfig = modules.configProvider.jwtClientConfig
  def signInPage = Action(Ok(views.html.auth.signin(None)).withNewSession)
  def authenticate(redirect: String): Action[AnyContent] = Action { implicit request =>
    case class Credentials(username: String, password: String)
    def tokenizeUser(user: User): String = {
      val claims = Seq(Sub(user.id.toString))
      val salt = request.headers.get("Client-IP").orElse(request.headers.get("X-Forwarded-For")).getOrElse(request.remoteAddress)
      JsonWebTokenWrapper(jwtConfig.copy(salt = Some(salt))).encode(claims)
    }
    Form(mapping("username" -> text, "password" -> text)(Credentials.apply)(Credentials.unapply)).bindFromRequest.fold (
      errors => BadRequest(views.html.auth.signin(Some(Alert(Level.Error, errors.errors.mkString(", "))))).withNewSession,
      credentials =>
        modules.userStorageRedisInterface.getUserByUsername(credentials.username).fold {
          val newUser = modules.userStorageRedisInterface.upsertUser(User.apply(credentials.username, credentials.password))
          val jwt = tokenizeUser(newUser)
          Redirect(redirect).withSession(jwtConfig.tokenName -> jwt)
        }{ user =>
          if(user.validatePassword(credentials.password)) {
            val jwt = tokenizeUser(user)
            Redirect(redirect).withSession(jwtConfig.tokenName -> jwt)
          } else {
            BadRequest(views.html.auth.signin(Some(Alert(Level.Error, s"Invalid password for user, ${credentials.username}")))).withNewSession
          }
        }
    )
  }
}
