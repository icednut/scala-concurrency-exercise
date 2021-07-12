package io.icednut.exercise.zio

import com.typesafe.scalalogging.LazyLogging
import zio.logging.log
import zio.logging.slf4j.Slf4jLogger
import zio.{ExitCode, URIO, ZIO}

object ExerciseWithFiber extends zio.App with LazyLogging {

  val env = Slf4jLogger.make { (_, message) => message }

  val myAppLogic = for {
    fiber1 <- (
      log.info("fiber1 start!!").provideCustomLayer(env) *>
        ZIO.effectTotal {
          logger.info("[THIS_IS_SIDE_EFFECT] from fiber1")
          1 + 2
        }
      ).fork
    fiber2 <- (
      log.info("fiber2 start!!").provideCustomLayer(env) *>
        ZIO.succeed {
          logger.info("[THIS_IS_SIDE_EFFECT] from fiber2")
          3 + 4
        }
      ).fork
    res1 <- fiber1.join
    res2 <- fiber2.join
    _ <- log.info(s"result is ${res1 + res2}").provideCustomLayer(env)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = myAppLogic.exitCode
}
