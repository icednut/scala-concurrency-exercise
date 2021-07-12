package io.icednut.exercise.zio

import com.typesafe.scalalogging.LazyLogging
import zio.{Fiber, ZIO}

object ExerciseWithFiberTP extends App with LazyLogging {
  val fiber1: ZIO[Any, Nothing, Fiber.Runtime[Nothing, Int]] = ZIO.effectTotal {
    logger.info("[THIS_IS_SIDE_EFFECT] from fiber1")
    1 + 2
  }.fork
  val fiber2: ZIO[Any, Nothing, Fiber.Runtime[Nothing, Int]] = fiber1.flatMap(ZIO.effectTotal(_))

  logger.info(s"fiber1 == fiber2 is ${fiber1 == fiber2}")
}
