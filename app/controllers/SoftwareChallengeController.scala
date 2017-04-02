package controllers

import javax.inject._

import challenge.sentiments.SentimentAnalyzer
import guice.ApplicationModulesI
import models.{Alert, Level}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SoftwareChallengeController @Inject() (modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context {
  def index = Action {
    Ok(views.html.index(alertOpt = None))
  }
  def sentiments = Action.async { implicit request =>
    case class SearchTerm(input: String)
    Form(mapping("searchTerm" -> text(maxLength = 500, minLength = 1))(SearchTerm.apply)(SearchTerm.unapply)).bindFromRequest.fold(
      errors => Future.successful(BadRequest(views.html.index(alertOpt = Some(Alert(Level.Error, errors.errors.headOption.fold("Couldn't process your search request")(_.message)))))),
      searchTerm => modules.twitterService.search(searchTerm.input).map { twitterTopicSearchResponse =>
        if (twitterTopicSearchResponse.response.tweets.nonEmpty) {
          val mainSentiment = SentimentAnalyzer.mainSentiment(twitterTopicSearchResponse.response.tweets.flatMap(_.sentiments).toList)
          Ok(views.html.sentiment(searchTerm.input, mainSentiment, twitterTopicSearchResponse.response.tweets))
        } else {
          BadRequest(views.html.index(alertOpt = Some(Alert(Level.Error, s"No results for search: ${searchTerm.input}"))))
        }
      }
    )
  }
}
