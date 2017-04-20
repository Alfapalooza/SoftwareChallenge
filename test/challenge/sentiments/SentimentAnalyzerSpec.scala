package challenge.sentiments

import org.scalatestplus.play.PlaySpec

class SentimentAnalyzerSpec extends PlaySpec {
  "search analyzer" should {
    "pickup positive emotion" in {
      SentimentAnalyzer.mainSentiment("I am happy!") mustEqual Sentiment.POSITIVE
    }
    "pickup negative emotion" in {
      SentimentAnalyzer.mainSentiment("I am sad!") mustEqual Sentiment.NEGATIVE
    }
  }
}
