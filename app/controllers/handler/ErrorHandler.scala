package controllers.handler

import javax.inject.Singleton

import challenge.logs.ErrorLog
import controllers.Context
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import challenge.services.exceptions.ExceptionServiceResponse
import play.api.UsefulException

import scala.concurrent._

@Singleton
class ErrorHandler extends HttpErrorHandler with Context {
  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    val acceptExtractors = new AcceptExtractors {}
    request match {
      case acceptExtractors.Accepts.Json() =>
        Future.successful(Status(statusCode)(Json.toJson(message)))
      case acceptExtractors.Accepts.Html() =>
        Future.successful(Status(statusCode).apply(views.html.defaultpages.badRequest(request.method, request.uri, message)))
    }
  }
  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    implicit val requestHolder = request
    val acceptExtractors = new AcceptExtractors {}
    request match {
      case acceptExtractors.Accepts.Json() =>
        exception match {
          case ex: ExceptionServiceResponse =>
            ErrorLog.logException(ex)
            Future.successful(ex.toResult)
          case ex =>
            ErrorLog.logException(ex)
            Future.successful(ExceptionServiceResponse.apply(ex).toResult)
        }
      case acceptExtractors.Accepts.Html() =>
        def serverError(ex: Throwable, status: Int) =
          Status(status).apply(views.html.errors.serverError(ex))
        exception match {
          case ex: ExceptionServiceResponse =>
            ErrorLog.logException(ex)
            Future.successful(serverError(ex, ex.status))
          case ex =>
            ErrorLog.logException(ex)
            Future.successful(serverError(ex, play.api.http.Status.INTERNAL_SERVER_ERROR))
        }
    }
  }
}