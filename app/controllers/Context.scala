package controllers

import challenge.logs.LogContext
import play.api.mvc.RequestHeader

trait Context {
  implicit def lc(implicit request: RequestHeader): LogContext = new LogContext {
    override protected def rh: RequestHeader = request
  }
}
