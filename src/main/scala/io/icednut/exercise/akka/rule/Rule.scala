package io.icednut.exercise.akka.rule

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Rule {

  def apply(name: String, targetMessage: String): Behavior[Command] =
    Behaviors.receive {
      case (_, MessageCheck(message, reply)) =>
        val result = targetMessage == message
        reply ! RuleResult(ruleName = name, result = result)
        Behaviors.same
    }

  sealed trait Command

  sealed trait Event

  case class MessageCheck(message: String, reply: ActorRef[Event]) extends Command

  case class RuleResult(ruleName: String, result: Boolean) extends Event

}
