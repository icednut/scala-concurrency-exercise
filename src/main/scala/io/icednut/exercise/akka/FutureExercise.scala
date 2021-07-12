package io.icednut.exercise.akka

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.util.Timeout
import io.icednut.exercise.akka.rule.ResponsePrinter.onComplete
import io.icednut.exercise.akka.rule.{Response, RuleAggregate}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object FutureExercise {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(RuleAggregate(), "exercise")

    implicit val timeout: Timeout = 10000.minute
    implicit val actorSystem = system
    implicit val executionContext = system.executionContext
    implicit val logger = system.log

    val message = RuleAggregate.Check("Charles", _)
    val future1: Future[Response] = system ? message

    val originalFuture = future1
    val afterFuture: Future[Response] = future1.flatMap(v => {
      logger.info("**** 여기는 비동기 실행 영역 ****")
      Future(v)
    })

    Thread.sleep(3 * 1000)
    logger.info("Some business logic")

    system.log.info(s"originalFuture == originalFuture is ${originalFuture == originalFuture}")
    system.log.info(s"originalFuture == afterFuture is ${originalFuture == afterFuture}")
    system.log.info(s"originalFuture's hashCode is ${originalFuture.hashCode()}")
    system.log.info(s"afterFuture's hashCode is ${afterFuture.hashCode()}")

    onComplete(originalFuture)
    onComplete(afterFuture)

    //    val response = Await.result(future1, 100.millis)
    //    logger.info(s"response is ${response}")
  }
}
