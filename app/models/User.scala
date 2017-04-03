package models

import org.mindrot.jbcrypt.BCrypt

case class User (
  id: Long,
  username: String,
  password: String
) {
  def withNewPassword(newPassword: String) = copy(password = BCrypt.hashpw(newPassword, BCrypt.gensalt()))
  def validatePassword(attempt: String): Boolean = {
    BCrypt.checkpw(password, attempt)
  }
}

object User {
  def apply(username: String, password: String) = new User(0, username, BCrypt.hashpw(password, BCrypt.gensalt()))
}
