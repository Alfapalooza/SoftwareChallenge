package utils.exceptions

import scala.util.control.NoStackTrace

sealed private[utils] case class MissingResourceException(name: String, path: String) extends Throwable(s"Cannot find resource for: $path/$name") with NoStackTrace
