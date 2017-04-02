package services

import challenge.utils.exceptions.ExceptionServiceResponse

// Commons codes are between 1 - 1000;
object CommonsServiceResponseDictionary {
  // OK
  object E0200 extends DefaultServiceResponse {
    override def msg: String = "Ok"
    override def status: Int = 200
    override def code: Int = 0
  }
  // Server errors (500 - 999)
  object E0500 extends ExceptionServiceResponse("Internal Server Error", 500, 500)
  object E0504 extends ExceptionServiceResponse("Service timeout", 501, 504)
}