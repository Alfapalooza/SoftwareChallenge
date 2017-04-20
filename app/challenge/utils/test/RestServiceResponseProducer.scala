package challenge.utils.test

import play.api.libs.ws.WSResponse

import scala.util.control.NoStackTrace

sealed private case class MissingServiceResponseException(url: String) extends Throwable(s"Missing service response for url: $url") with NoStackTrace
class RestServiceResponseProducer(var restServiceResponses: Seq[(String, WSResponse)]) {
  def consumeServiceResponse(url: String): WSResponse =
    restServiceResponses.find(_._1 == url) match {
      case Some(response) => response._2
      case _ => throw MissingServiceResponseException(url)
    }
}
