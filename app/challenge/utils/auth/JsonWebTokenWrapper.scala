package challenge.utils.auth

import challenge.logs.{ApplicationLog, ErrorLog, LogContext}
import challenge.utils.auth.conf.JwtConfig
import io.igl.jwt.{Alg, ClaimValue, DecodedJwt, Jwt}

import scala.util.{Failure, Success}

/**
  * Created by malla on 9/25/2016.
  */
case class JsonWebTokenWrapper(jwtConfig: JwtConfig) {
  def encode[A](claims: Seq[ClaimValue]): String = {
    new DecodedJwt(Seq(Alg(jwtConfig.encryptionAlgorithm)), claims).encodedAndSigned(jwtConfig.signature)
  }
  def decode[A](token: String)(implicit lc: LogContext): Option[Jwt] = {
    DecodedJwt.validateEncodedJwt(token, jwtConfig.signature, jwtConfig.encryptionAlgorithm, jwtConfig.headers, jwtConfig.claims) match {
      case Success(jwt) =>
        Some(jwt)
      case Failure(ex) =>
        ErrorLog.logException(ex)
        Option.empty[Jwt]
      case _ =>
        ApplicationLog.warn("decode: _")
        Option.empty[Jwt]
    }
  }
}
