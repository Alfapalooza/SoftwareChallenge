package challenge.services.exceptions

import play.api.data.validation.ValidationError
import play.api.libs.json.JsPath

import scala.reflect.ClassTag
import scala.reflect._

case class ModelValidationException[A: ClassTag](errors: Seq[(JsPath, Seq[ValidationError])]) extends Throwable(s"Error parsing: ${classTag[A].runtimeClass}, errors: ${errors.foldLeft("")((acc, err) => acc + s"(path: ${err._1.toString()}, errors: ${err._2.map(_.messages.mkString(" ")).mkString(", ")})")}")
