package challenge.persistence

import com.google.inject.Inject
import models.User
import play.api.cache.CacheApi
import play.cache.NamedCache

class UserStorageRedisInterface @Inject()(@NamedCache("user-storage") cache: CacheApi) extends UserStorageRedisInterfaceI {
  override def getUser(id: Long): Option[User] = cache.get[User](id.toString)
  override def getUserByUsernamePassword(username: String, password: String): Option[User] =
    getUserBank.find {
      case (id, user) => user.username == username && user.validatePassword(password)
    }.map(_._2)

  override def removeUser(id: Long): Option[User] =
    getUser(id).map { user =>
      cache.remove(user.id.toString)
      removeUserBank(user)
      user
    }
  override def upsertUser(user: User): User =
    if(user.id == 0) {
      val id = getUserSequence
      val newUser = user.copy(id = id)
      cache.set(id.toString, newUser)
      upsertUserBank(newUser)
      newUser
    } else {
      cache.set(user.id.toString, user)
      upsertUserBank(user)
      user
    }
  private def getUserSequence: Long = {
    val id = cache.get[Long]("user-sequence").getOrElse(0l) + 1
    cache.set("user-sequence", id)
    id
  }
  private def getUserBank: Map[Long, User] =
    cache.get[Map[Long, User]]("users").getOrElse(Map.empty[Long, User])
  private def removeUserBank(user: User): Map[Long, User] = {
    val userBank = cache.get[Map[Long, User]]("users").getOrElse(Map.empty[Long, User]) - user.id
    cache.set("users", userBank)
    userBank
  }
  private def upsertUserBank(user: User): Map[Long, User] = {
    val userBank = cache.get[Map[Long, User]]("users").getOrElse(Map.empty[Long, User]) + (user.id -> user)
    cache.set("users", userBank)
    userBank
  }
}

trait UserStorageRedisInterfaceI {
  def getUser(id: Long): Option[User]
  def getUserByUsernamePassword(username: String, password: String): Option[User]
  def removeUser(id: Long): Option[User]
  def upsertUser(user: User): User
}