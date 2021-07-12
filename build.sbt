name := "scala-concurrency-exercise"

version := "1.0"

scalaVersion := "2.13.2"

lazy val AkkaVersion = "2.6.15"
lazy val ZioVersion = "1.0.9"
lazy val ZioLoggingVersion = "0.5.11"
lazy val MonixVersion = "3.2.1"

scalacOptions ++= Seq(
  // See other posts in the series for other helpful options
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-unchecked",
  //    "-deprecation",
  "-Xfuture",
  //    "-Xfatal-warnings",
  //  "-Yno-adapted-args",
  //    "-Ywarn-dead-code",
  //    "-Ywarn-numeric-widen",
  //    "-Ywarn-value-discard",
  //    "-Ywarn-unused",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-language:reflectiveCalls",
  "-language:postfixOps",
  "-Ymacro-annotations"
)

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
