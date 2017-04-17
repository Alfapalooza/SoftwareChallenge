package challenge.config

import challenge.config.exceptions.ConfigurationNotFoundException
import challenge.services.twitter.conf.TwitterConfig
import challenge.utils.auth.conf.JwtConfig
import com.google.inject.Inject
import play.api.Configuration

class ConfigProvider @Inject()(configuration: Configuration) extends ConfigProviderI{
	override lazy val jwtClientConfig: JwtConfig = JwtConfig.apply(configuration.getConfig("jwt-config.client").getOrElse(throw ConfigurationNotFoundException("jwt-config.client")))
	override lazy val twitterConfig: TwitterConfig = TwitterConfig.apply(configuration.getConfig("services.twitter").getOrElse(throw ConfigurationNotFoundException("services.twitter")))
}

trait ConfigProviderI {
	def jwtClientConfig: JwtConfig
	def twitterConfig: TwitterConfig
}



