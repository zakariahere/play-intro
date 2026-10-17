package controllers

import play.api.mvc._
import services.GreetingService


class HomeController(val controllerComponents: ControllerComponents, val greetingService: GreetingService) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Play up and running! %s".formatted(greetingService.hellomk))
  }
}
