package controllers

import cats.data.Validated
import model.{Blog, BlogCreateRequest, BlogId}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repository.BlogRepository

import scala.concurrent.{ExecutionContext, Future}

case class BlogController(val controllerComponents: ControllerComponents, val blogRepository: BlogRepository) extends BaseController {
  private implicit val ec: ExecutionContext = defaultExecutionContext

  def createBlog(): Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[BlogCreateRequest].fold(
        errors => Future.successful(BadRequest(JsError.toJson(errors))),
        payload => {
          validateBlog(payload).fold(errors => Future.successful(BadRequest(Json.obj("errors" -> errors.toList))),
            newBlog => {
              val blog = Blog(BlogId.generate, newBlog.slug, newBlog.description)
              blogRepository.save(blog).map(_ => Created(Json.toJson(blog)))
            }
          )
        }
      )
  }

  private def validateBlog(payload: BlogCreateRequest): Validated[List[String], BlogCreateRequest] = {
    payload.description match {
      case description => Validated.valid(payload)
      case _ => Validated.invalid(List("Description not ok"))
    }
  }

  def findBlog(id: String): Action[AnyContent] = Action.async {
    blogRepository.findById(BlogId(id)).map {
      case Some(blog) => Ok(Json.toJson(blog))
      case None => NotFound
    }
  }
}
