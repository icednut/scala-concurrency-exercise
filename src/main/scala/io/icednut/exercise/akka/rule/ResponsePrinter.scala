package io.icednut.exercise.akka.rule

import org.slf4j.Logger

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object ResponsePrinter {
  def onComplete[T](future: Future[T])(implicit ec: ExecutionContext, logger: Logger) = {
    future.onComplete {
      case Success(checkResult: Response) =>
        logger.info(s"result == ${checkResult.result}")
      case Success(checkResult: String) =>
        logger.info(s"result == $checkResult")
      case Success(checkResult: Int) =>
        logger.info(s"result == $checkResult")
      case Failure(exception) =>
        logger.error(exception.getMessage, exception)
    }
  }
}
