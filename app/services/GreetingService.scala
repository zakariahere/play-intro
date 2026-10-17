package services

import com.typesafe.scalalogging.LazyLogging
import play.api.Configuration

class GreetingService(config: Configuration) extends LazyLogging{
  def hellomk: String = {
    logger.info("Hello %s".formatted(config.get[String]("zak.greeting")))
    "hello there".mkString(",")
  }
}
