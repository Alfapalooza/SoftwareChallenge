package challenge.utils.auth.conf

import challenge.config.exceptions.ConfigurationNotFoundException
import io.igl.jwt._
import org.apache.commons.codec.binary.Base64
import play.api.Configuration

object JwtConfig{
	/**
		* Derive JwtConfig from configuration, must follow this format,
		* {{{
		* token = "TOKEN_NAME", for defining session header name
		* signature = "SECRET", for defining the binding secret
		* encryption = oneOf: [
		* "HS256",
		* "HS384",
		* "HS512
		* ]
		* }}}
		*
		* @param config Configuration containing jwt block as formatted above */
	def apply(config: Configuration): JwtConfig = apply("token", "signature", "encryption", "claims", config)

	def apply(_token: String, _signature: String, _encryption: String, _claims: String, config: Configuration): JwtConfig = {
		val token = config.getString(_token).getOrElse(
			throw ConfigurationNotFoundException(_token)
		)
		val signature = config.getString(_signature).getOrElse(
			throw ConfigurationNotFoundException(_signature)
		)
		val encryption = config.getString(_encryption).getOrElse(
			throw ConfigurationNotFoundException(_encryption)
		)
		val claims = config.getStringSeq(_claims).getOrElse(Seq.empty[String])

		apply(token, signature, encryption, claims = claims)
	}

	private def apply(
    token: String,
		signature: String,
		encryption: String,
		headers: Seq[String] = Seq.empty,
		claims: Seq[String] = Seq.empty): JwtConfig = {
		val resolvedEncryption = encryption match {
			case "HS256" => Algorithm.HS256
			case "HS384" => Algorithm.HS384
			case "HS512" => Algorithm.HS512
			case _ 			 => Algorithm.HS256
		}

		val resolvedClaims: Seq[ClaimField] = claims.map {
				case "sub" 	 => Sub
				case "aud" 	 => Aud
		}

		JwtConfig(
			token,
			signature,
			resolvedEncryption,
			Set.empty[HeaderField],
			resolvedClaims.toSet,
			None
		)
	}
}

case class JwtConfig(
  tokenName: String,
  private val secret: String,
  encryptionAlgorithm: Algorithm,
  headers: Set[HeaderField],
  claims: Set[ClaimField],
  salt: Option[String]){
	lazy val signature: String = {
		salt.fold(secret) { s =>
			Base64.encodeBase64URLSafeString(secret.getBytes("UTF-8") ++ s.getBytes("UTF-8"))
		}
	}
}

