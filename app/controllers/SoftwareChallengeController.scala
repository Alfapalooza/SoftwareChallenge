package controllers

import javax.inject._

import challenge.sentiments.SentimentAnalyzer
import controllers.actions.auth.AuthorizedAction
import challenge.guice.ApplicationModulesI
import models.{Alert, Level}
import play.api.mvc.{Call, Controller}

import scala.concurrent.ExecutionContext

@Singleton
class SoftwareChallengeController @Inject() (modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context {
  private val jwtConfig = modules.configProvider.jwtClientConfig
  private val failsafe = Some(Call("GET", "/sign-in"))
  def index = AuthorizedAction(jwtConfig, failsafe)(ec) {
    Ok(views.html.index(alertOpt = None))
  }
  def sentiments(term: String) = AuthorizedAction(jwtConfig, failsafe)(ec).async { implicit request =>
    modules.twitterService.search(term).map { twitterTopicSearchResponse =>
      if (twitterTopicSearchResponse.response.tweets.nonEmpty) {
        val mainSentiment = SentimentAnalyzer.mainSentiment(twitterTopicSearchResponse.response.tweets.flatMap(_.sentiments))
        Ok(views.html.sentiment(term, mainSentiment, twitterTopicSearchResponse.response.tweets))
      } else {
        BadRequest(views.html.index(alertOpt = Some(Alert(Level.Error, s"No results for search: $term"))))
      }
    }
  }
}
