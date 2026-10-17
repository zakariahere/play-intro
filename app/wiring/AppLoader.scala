package wiring

import controllers.{BlogController, HomeController}
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.filters.HttpFiltersComponents
import repository.RepositoryComponents
import router.Routes
import services.{ServiceComponents}

class AppLoader extends ApplicationLoader{
  override def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach(
      _.configure(context.environment, context.initialConfiguration, Map.empty))
    new AppComponents(context).application
  }
}

class AppComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with ServiceComponents with RepositoryComponents{
  lazy val homeController = new HomeController(controllerComponents, greetingService)
  lazy val blogController = new BlogController(controllerComponents, blogRepository)
  lazy val router: Router = new Routes(httpErrorHandler, homeController, blogController)
}