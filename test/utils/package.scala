import akka.util.ByteString
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.libs.ws.{WSCookie, WSResponse}
import utils.exceptions.{InvalidJsonParseException, MissingResourceException}

import scala.io.{Codec, Source}
import scala.util.Try
import scala.xml.Elem

package object utils {
  def readFileAsJsValue(name: String, path: String): JsValue = Try(Json.parse(readFileAsString(s"$name", path))).getOrElse(throw InvalidJsonParseException(s"$name", path))
  def readFileAsString(name: String, path: String): String = {
    Option(getClass.getResourceAsStream(s"$path/$name")).map { in =>
      Source.fromInputStream(in)(Codec.UTF8).mkString
    } getOrElse {
      throw MissingResourceException(name, path)
    }
  }
  def jsonToWSResponse(_status: Int, _headers: Map[String, Seq[String]], _cookies: Seq[WSCookie], _body: JsValue) = new WSResponse {
    override def statusText: String = "TEST"
    override def underlying[T]: T = ???
    override def xml: Elem = ???
    override def body: String = _body.toString()
    override def header(key: String): Option[String] = allHeaders.get(key).map(_.mkString(" "))
    override def cookie(name: String): Option[WSCookie] = cookies.find(_.name == name)
    override def bodyAsBytes: ByteString = ???
    override def cookies: Seq[WSCookie] = _cookies
    override def status: Int = _status
    override def json: JsValue = _body
    override def allHeaders: Map[String, Seq[String]] = _headers
  }
  def stringToWSResponse(_status: Int, _headers: Map[String, Seq[String]], _cookies: Seq[WSCookie], _body: String) = new WSResponse {
    override def statusText: String = "TEST"
    override def underlying[T]: T = ???
    override def xml: Elem = ???
    override def body: String = _body
    override def header(key: String): Option[String] = allHeaders.get(key).map(_.mkString(" "))
    override def cookie(name: String): Option[WSCookie] = cookies.find(_.name == name)
    override def bodyAsBytes: ByteString = ???
    override def cookies: Seq[WSCookie] = _cookies
    override def status: Int = _status
    override def json: JsValue = Try(Json.parse(_body)).getOrElse(JsString(_body))
    override def allHeaders: Map[String, Seq[String]] = _headers
  }
  implicit def jsonToWSResponse(json: JsValue): WSResponse = jsonToWSResponse(200, Map.empty[String, Seq[String]], Nil, json)
  implicit def stringToWSResponse(string: String): WSResponse = stringToWSResponse(200, Map.empty[String, Seq[String]], Nil, string)
}
