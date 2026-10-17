package wiring

import controllers.HomeController
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.filters.HttpFiltersComponents
import router.Routes
import services.GreetingService

class AppLoader extends ApplicationLoader{
  override def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach(
      _.configure(context.environment, context.initialConfiguration, Map.empty))
    new AppComponents(context).application
  }
}

class AppComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context) with HttpFiltersComponents{
  lazy val greetingService = new GreetingService(configuration)
  lazy val homeController = new HomeController(controllerComponents, greetingService)
  lazy val router: Router = new Routes(httpErrorHandler, homeController)
}