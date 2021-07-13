name := "scala-concurrency-exercise"

version := "1.0"

scalaVersion := "2.13.2"

lazy val AkkaVersion = "2.6.15"
lazy val ZioVersion = "1.0.9"
lazy val ZioLoggingVersion = "0.5.11"
lazy val MonixVersion = "3.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,

  // Concurrency
  "dev.zio" %% "zio" % ZioVersion,
  "io.monix" %% "monix" % MonixVersion,

  // logging
  "dev.zio" %% "zio-logging" % ZioLoggingVersion,
  "dev.zio" %% "zio-logging-slf4j" % ZioLoggingVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",

  // Test
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)
