package io.icednut.exercise.akka.rule

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Timeout
import io.icednut.exercise.akka.rule.Rule.{MessageCheck, RuleResult}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object RuleAggregate {

  def apply(): Behavior[Command] = {
    Behaviors.setup { context =>
      val rule1 = context.spawn(Rule(name = "Rule1", targetMessage = "hello"), "rule1")
      val rule2 = context.spawn(Rule(name = "Rule2", targetMessage = "greet"), "rule2")

      Behaviors.receiveMessage {
        case Check(message, reply) =>
          implicit val timeout: Timeout = 10.millis
          implicit val scheduler = context.system.scheduler

          val future1 = rule1 ? (MessageCheck(message, _))
          val future2 = rule2 ? (MessageCheck(message, _))

          implicit val executionContext = context.executionContext
          val future: Future[Iterable[Rule.Event]] = Future.sequence(Iterable(future1, future2))

          context.pipeToSelf(future) {
            case Success(value: Iterable[RuleResult]) => Reply(
              value.find(_.result == true).map(v => s"${v.ruleName} Checked!!"),
              reply
            )
            case Failure(exception) => Reply(Some(exception.getMessage), reply)
          }
          Behaviors.same
        case Reply(replyMessage, reply) =>
          replyMessage match {
            case Some(result) => reply ! Response(result)
            case _ => reply ! Response("rule is empty")
          }
          Behaviors.same
      }
    }
  }

  sealed trait Command

  case class Check(message: String, reply: ActorRef[Response]) extends Command

  case class Reply(replyMessage: Option[String], reply: ActorRef[Response]) extends Command
}
