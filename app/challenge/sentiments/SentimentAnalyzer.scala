package challenge.sentiments

import java.util.Properties

import challenge.sentiments.Sentiment.Sentiment
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.convert.wrapAll._

/**
  * From: shekhargulati/52-technologies-in-2016
  * https://github.com/shekhargulati/52-technologies-in-2016/tree/master/03-stanford-corenlp
  */
object SentimentAnalyzer {
  type Sentence = String
  private val props = {
    val innerProps = new Properties()
    innerProps.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
    innerProps
  }
  private val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  def mainSentiment(input: String): Sentiment = Option(input) match {
    case Some(text) if !text.isEmpty => summarizeSentiments(extractSentiments(input))
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  def mainSentiment(sentiments: List[(Sentence, Sentiment)]): Sentiment = summarizeSentiments(sentiments)

  def extractSentiments(text: String): List[(Sentence, Sentiment)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences
      .map(sentence => (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])))
      .map { case (sentence, tree) => (sentence.toString, Sentiment.toSentiment(RNNCoreAnnotations.getPredictedClass(tree))) }
      .toList
  }

  private def summarizeSentiments(sentiments: List[(Sentence, Sentiment)]) = {
    val (_, sentiment) = sentiments.maxBy { case (sentence, _) => sentence.length }
    sentiment
  }
}
