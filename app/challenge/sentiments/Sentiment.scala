package challenge.sentiments

/**
  * From: shekhargulati/52-technologies-in-2016
  * https://github.com/shekhargulati/52-technologies-in-2016/tree/master/03-stanford-corenlp
  */
object Sentiment extends Enumeration {
  type Sentiment = Value
  val POSITIVE, NEGATIVE, NEUTRAL = Value

  def toSentiment(sentiment: Int): Sentiment = sentiment match {
    case x if x <= 1 => Sentiment.NEGATIVE
    case 2 => Sentiment.NEUTRAL
    case x if x >= 3 => Sentiment.POSITIVE
  }
}
