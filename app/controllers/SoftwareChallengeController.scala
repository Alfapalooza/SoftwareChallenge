package controllers

import javax.inject._

import challenge.sentiments.SentimentAnalyzer
import controllers.actions.auth.AuthorizedAction
import challenge.guice.ApplicationModulesI
import challenge.services.ServiceResponse
import models.services.twitter.TwitterTopicSearchResponse
import models.{Alert, Level}
import play.api.mvc.{Call, Controller}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

@Singleton
class SoftwareChallengeController @Inject() (modules: ApplicationModulesI)(implicit ec: ExecutionContext) extends Controller with Context {
  private val jwtConfig = modules.configProvider.jwtClientConfig
  private val failsafe = Some(Call("GET", "/sign-in"))
  def index = AuthorizedAction(jwtConfig, failsafe)(ec) {
    Ok(views.html.index(alertOpt = None))
  }
  def profile = AuthorizedAction(jwtConfig, failsafe)(ec) { implicit request =>
    request.userId.fold(Redirect(failsafe.get)) { id =>
      modules.userStorageRedisInterface.getUser(id).fold(Redirect(failsafe.get)) { user =>
        Ok(views.html.profile(user.username, user.searches))
      }
    }
  }
  def sentiments(_term: String) = AuthorizedAction(jwtConfig, failsafe)(ec).async { implicit request =>
    val term = if (_term == "") "empty" else _term
    request.userId.fold(Future.successful(Redirect(failsafe.get))) { id =>
      modules.userStorageRedisInterface.getUser(id).fold(Future.successful(Redirect(failsafe.get))) { user =>
        modules.userStorageRedisInterface.upsertUser(user.copy(searches = user.searches :+ term))
        modules.twitterService.search(term).map(tweetsToSentimentResult(term, _))
      }
    }
  }
  def lucky = AuthorizedAction(jwtConfig, failsafe)(ec).async { implicit request =>
    request.userId.fold(Future.successful(Redirect(failsafe.get))) { id =>
      for {
        trendingHashtags <- modules.twitterService.trendingHashTags
        serendipitousSearchTerm = {
          val fallback = "Whoopsie"
          modules.userStorageRedisInterface.getUser(id).fold(fallback) { user =>
            scala.util.Random.shuffle(trendingHashtags.response.trendingHashTags.filterNot(hashtag => user.searches.contains(hashtag.hashtag))).headOption.fold(fallback)(_.hashtag)
          }
        }
        twitterTopicSearchResponse <- modules.twitterService.search(serendipitousSearchTerm)
      } yield tweetsToSentimentResult(serendipitousSearchTerm, twitterTopicSearchResponse)
    }
  }
  private def tweetsToSentimentResult(term: String, twitterTopicSearchResponse: ServiceResponse[TwitterTopicSearchResponse]) = {
    if (twitterTopicSearchResponse.response.tweets.nonEmpty) {
      val mainSentiment = SentimentAnalyzer.mainSentiment(twitterTopicSearchResponse.response.tweets.flatMap(_.sentiments))
      Ok(views.html.search(term, mainSentiment, twitterTopicSearchResponse.response.tweets))
    } else {
      BadRequest(views.html.index(alertOpt = Some(Alert(Level.Error, s"No results for search: $term"))))
    }
  }
}
