package io.icednut.exercise.akka

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.util.Timeout
import io.icednut.exercise.akka.rule.ResponsePrinter.onComplete
import io.icednut.exercise.akka.rule.{Response, RuleAggregate}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object AkkaAskExercise {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(RuleAggregate(), "exercise")

    implicit val timeout: Timeout = 10000.minute
    implicit val actorSystem = system
    implicit val executionContext = system.executionContext
    implicit val logger = system.log

    val message = RuleAggregate.Check("Charles", _)
    val future1: Future[Response] = system ? message
    val future2: Future[Response] = system ? message

    onComplete(future1)
    onComplete(future2)
  }
}
