package challenge.config.exceptions

import scala.util.control.NoStackTrace

case class ConfigurationNotFoundException(configuration: String) extends Exception(s"Missing configuration: $configuration") with NoStackTrace
