package controllers

import challenge.utils.auth.JsonWebTokenWrapper
import io.igl.jwt.Sub
import models.User
import org.scalatestplus.play.PlaySpec
import play.api.libs.ws.WSResponse
import play.api.mvc.Request
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.DefaultFixture

class SoftwareChallengeControllerSpec extends PlaySpec {
  protected class SoftwareChallengeControllerFixture(responses: (String, WSResponse)*) extends DefaultFixture(responses) {
    val jwtConfig = modules.configProvider.jwtClientConfig
    def tokenizeUser(user: User)(implicit request: Request[_]): String = {
      val claims = Seq(Sub(user.id.toString))
      val salt = request.headers.get("Client-IP").orElse(request.headers.get("X-Forwarded-For")).getOrElse(request.remoteAddress)
      JsonWebTokenWrapper(jwtConfig.copy(salt = Some(salt))).encode(claims)
    }
  }
//  Crypto library is still on a Play application dependent setup. Will have to figure out how to get the Play application spun-up,
//  as it stands spinning up an application doesn't work because the controllers require an instance of 'ApplicationModulesI', which is implemented by Factory for tests.
//  "feeling lucky" should {
//    "return sentiment page for trending tweets" in new SoftwareChallengeControllerFixture() {
//      val user = modules.userStorageRedisInterface.upsertUser(User("test123", "test123"))
//      val request = FakeRequest()
//      val result = new SoftwareChallengeController(modules).lucky()(request.withSession(jwtConfig.tokenName -> tokenizeUser(user)(request)))
//      status(result) mustEqual 200
//    }
//    "return \'no results\' page if trending hashtags conflict with the user's searches" in new SoftwareChallengeControllerFixture() {
//
//    }
//  }
}
