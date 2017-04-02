package models

case class Alert(level: Level, msg: String)
sealed class Level(val level: String)
object Level {
  case object Info extends Level("info")
  case object Warning extends Level("warning")
  case object Error extends Level("danger")
}