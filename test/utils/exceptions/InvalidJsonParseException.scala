package utils.exceptions

import scala.util.control.NoStackTrace

sealed private[utils] case class InvalidJsonParseException(name: String, path: String) extends Throwable(s"Could not parse file as JsValue for: $path/$name") with NoStackTrace
