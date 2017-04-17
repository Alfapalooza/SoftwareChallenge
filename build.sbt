name := """software-challenge"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

resolvers += "google-sedis-fix" at "http://pk11-scratch.googlecode.com/svn/trunk"

libraryDependencies ++= Seq(
  "com.typesafe.play.modules" %% "play-modules-redis" % "2.5.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "io.igl" %% "jwt" % "1.2.0",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp")),
  cache,
  ws
)

