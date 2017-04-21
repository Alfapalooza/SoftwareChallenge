package controllers

import models.User
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.DefaultFixture

class AuthControllerSpec extends PlaySpec with Results {
  "authenticate" should {
//    Crypto library is still on a Play application dependent setup. Will have to figure out how to get the Play application spun-up,
//    as it stands spinning up an application doesn't work because the controllers require an instance of 'ApplicationModulesI', which is implemented by Factory for tests.
//    "create a valid jwt token" in new DefaultFixture() {
//      modules.userStorageRedisInterface.upsertUser(User("test321", "test123"))
//      val request = FakeRequest().withFormUrlEncodedBody("username" -> "test321", "password" -> "test123")
//      val result = new AuthController(modules).authenticate("/")(request)
//      status(result) mustEqual 200
//    }
    "block an existing user with a wrong password" in new DefaultFixture() {
      modules.userStorageRedisInterface.upsertUser(User("test321", "test123"))
      val request = FakeRequest().withFormUrlEncodedBody("username" -> "test321", "password" -> "test321")
      val result = new AuthController(modules).authenticate("/")(request)
      status(result) mustEqual 403
    }
  }
}
