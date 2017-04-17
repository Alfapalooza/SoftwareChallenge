package challenge.services.exceptions

import play.api.libs.json.Writes
import challenge.services.CommonsServiceResponseDictionary.E0500
import challenge.services.{DefaultServiceResponse, ServiceResponse}

case class ExceptionServiceResponse(
  private val _msg: String,
  private val _code: Int,
  private val _status: Int
) extends Throwable(_msg) with DefaultServiceResponse {
  override def msg: String = _msg
  override def status: Int = _status
  override def code: Int = _code
}

object ExceptionServiceResponse {
  def apply[T](serviceResponse: ServiceResponse[T])(implicit writes: Writes[T]): ExceptionServiceResponse =
    ExceptionServiceResponse(
      serviceResponse.msg,
      serviceResponse.code,
      serviceResponse.status
    )
  def apply(ex: Throwable): ExceptionServiceResponse = E0500.copy(_msg = ex.getMessage)
}