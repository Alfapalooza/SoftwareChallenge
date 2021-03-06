package challenge.utils

case class Timer (t0: Long = System.currentTimeMillis) {
  val startTime: Long = System.currentTimeMillis
  def currentTime: Long = System.currentTimeMillis
  def elapsedTime: Long = System.currentTimeMillis - t0
}