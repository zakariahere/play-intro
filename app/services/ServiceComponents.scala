package services

import play.api.BuiltInComponents

trait ServiceComponents {
  self: BuiltInComponents =>
  lazy val greetingService = new GreetingService(configuration)
}
