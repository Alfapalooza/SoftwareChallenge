package controllers

import javax.inject._

import guice.ApplicationModulesI
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class SoftwareChallengeWSController @Inject()(modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context {
  def search(input: String) = Action.async { implicit request =>
    modules.twitterService.search(input).map(tweets => Ok(Json.toJson(tweets)))
  }
}
