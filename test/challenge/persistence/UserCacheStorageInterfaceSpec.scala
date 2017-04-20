package challenge.persistence

import java.util.UUID

import challenge.utils.test.RestServiceResponseProducer
import models.User
import org.scalatestplus.play.PlaySpec
import utils.Fixture

class UserCacheStorageInterfaceSpec extends PlaySpec {
  protected class UserCacheStorageInterfaceFixture extends Fixture {
    override protected def restServiceConsumer: RestServiceResponseProducer = new RestServiceResponseProducer(Nil)
    val storage = modules.userStorageRedisInterface
  }
  "adding a user" should {
    "successfully return the added user with sequenced id" in new UserCacheStorageInterfaceFixture {
      val user = storage.upsertUser(User.apply("Allaire Martin", "test321"))
      user.id mustEqual storage.getUserSequence - 1
      user.username mustEqual "Allaire Martin"
      storage.removeUser(user.id)
    }
    "report added user in the bank of users when user added" in new UserCacheStorageInterfaceFixture {
      val countBefore = storage.getUserBank.size
      val user = storage.upsertUser(User.apply("Martin Allaire", "test123"))
      val countAfter = storage.getUserBank.size
      countBefore mustEqual countAfter - 1
      storage.removeUser(user.id)
    }
  }
  "update a user" should {
    "successfully add the user if he does not exist with sequenced id" in new UserCacheStorageInterfaceFixture {
      val uniqueUser = UUID.randomUUID().toString
      storage.getUserByUsername(uniqueUser) mustEqual None
      val user = storage.upsertUser(User.apply(uniqueUser, "test123"))
      user.id mustEqual storage.getUserSequence - 1
      user.username mustEqual uniqueUser
      storage.removeUser(user.id)
    }
    "successfully update user information with same sequenced id" in new UserCacheStorageInterfaceFixture {
      val uniqueUser = UUID.randomUUID().toString
      val user = storage.upsertUser(User.apply(uniqueUser, "test123"))
      user.username mustEqual uniqueUser
      val updatedUser = storage.upsertUser(user.copy(username = "Martin"))
      updatedUser.id mustEqual user.id
      updatedUser.username mustEqual "Martin"
      storage.removeUser(updatedUser.id)
    }
    "report unchanged bank of user when user updated" in new UserCacheStorageInterfaceFixture {
      val uniqueUser = UUID.randomUUID().toString
      val user = storage.upsertUser(User.apply(uniqueUser, "test123"))
      val countBefore = storage.getUserBank.size
      val updatedUser = storage.upsertUser(user.copy(username = "Martin"))
      val countAfter = storage.getUserBank.size
      countBefore mustEqual countAfter
      storage.removeUser(updatedUser.id)
    }
  }
  "remove a user" should {
    "successfully remove the user if he exists" in new UserCacheStorageInterfaceFixture {
      val user = storage.upsertUser(User.apply("Allaire Martin", "test321"))
      val removedUser = storage.removeUser(user.id)
      storage.getUser(removedUser.get.id) mustEqual None
    }
    "report removed user in the bank of users when user removed" in new UserCacheStorageInterfaceFixture {
      val user = storage.upsertUser(User.apply("Allaire Martin", "test321"))
      val countBefore = storage.getUserBank.size
      storage.removeUser(user.id)
      val countAfter = storage.getUserBank.size
      countBefore - 1 mustEqual countAfter
    }
  }
}
