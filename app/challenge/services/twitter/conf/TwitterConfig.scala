package challenge.services.twitter.conf

import challenge.config.exceptions.ConfigurationNotFoundException
import challenge.utils.{Host, Protocol, Url}
import play.api.Configuration
import play.api.libs.oauth.{ConsumerKey, RequestToken}

object TwitterConfig{
	def apply(config: Configuration): TwitterConfig = apply("host", "consumerKey", "consumerSecret", "tokenKey", "tokenSecret", config)
	def apply(_host: String, _consumerKey: String, _consumerSecret: String, _tokenKey: String, _tokenSecret: String, config: Configuration): TwitterConfig = {
		val consumerKey = ConsumerKey(
			config.getString(_consumerKey).getOrElse(throw ConfigurationNotFoundException("consumerKey")),
			config.getString(_consumerSecret).getOrElse(throw ConfigurationNotFoundException("consumerSecret"))
		)
		val requestToken = RequestToken(
			config.getString(_tokenKey).getOrElse(throw ConfigurationNotFoundException("tokenKey")),
			config.getString(_tokenSecret).getOrElse(throw ConfigurationNotFoundException("tokenSecret"))
		)
		val serviceUrl = Url(Host(Protocol.https, config.getString(_host).getOrElse("api.twitter.com"))) / "1.1" / "search" / "tweets.json"
		TwitterConfig(consumerKey, requestToken, serviceUrl)
	}
}

case class TwitterConfig(
	consumerKey: ConsumerKey,
	requestToken: RequestToken,
	serviceUrl: Url
)

