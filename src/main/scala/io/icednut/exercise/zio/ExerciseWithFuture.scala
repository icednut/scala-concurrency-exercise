package io.icednut.exercise.zio

import zio.logging.log
import zio.logging.slf4j.Slf4jLogger
import zio.{ExitCode, URIO, ZIO}

import scala.concurrent.Future

object ExerciseWithFuture extends zio.App {
  val env = Slf4jLogger.make { (_, message) => message }
  val myAppLogic = for {
    result <- ZIO.fromFuture { implicit ec =>
      Future {
        log.info("[THIS_IS_SIDE_EFFECT] future inside").provideCustomLayer(env)
        1 + 2
      }
    }
    nextValue1 = result + 3
    nextValue2 = nextValue1 + 4
    _ <- log.info(s"result is ${nextValue2}").provideCustomLayer(env)
  } yield result

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = myAppLogic.exitCode
}
