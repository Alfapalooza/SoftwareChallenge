package models

import org.mindrot.jbcrypt.BCrypt

case class User (
  id: Long,
  searches: Seq[String],
  username: String,
  password: String
) {
  def withNewPassword(newPassword: String) = copy(password = BCrypt.hashpw(newPassword, BCrypt.gensalt()))
  def validatePassword(attempt: String): Boolean = {
    BCrypt.checkpw(attempt, password)
  }
}

object User {
  def apply(username: String, password: String) = new User(0, Nil, username, BCrypt.hashpw(password, BCrypt.gensalt()))
}
