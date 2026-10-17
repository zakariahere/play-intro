package controllers

import cats.data.{Validated, ValidatedNec}
import cats.implicits.{catsSyntaxTuple2Semigroupal, catsSyntaxValidatedIdBinCompat0}
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
          validateBlog(payload).fold(errors => {
            val badResult = BadRequest(Json.obj("errors" -> errors.toChain.toList.map(e => Json.obj("code" -> e.toString, "message" -> e.message))))
            Future.successful(badResult)
          },
            newBlog => {
              val blog = Blog(BlogId.generate, newBlog.slug, newBlog.description)
              blogRepository.save(blog).map(_ => Created(Json.toJson(blog)))
            }
          )
        }
      )
  }

  type ValidationResult[A] = ValidatedNec[TodoError, A]

  sealed trait TodoError{def message: String}
  object TodoError {
    case object DescriptionBlank extends TodoError{val message = "Description is not on point."}
    case object SlugBlank extends TodoError{val message = "Slug is not on point."}
  }

  private def validateBlog(payload: BlogCreateRequest): ValidationResult[BlogCreateRequest] = {
    (validateSlug(payload.slug), validateDescription(payload.description)).mapN(BlogCreateRequest(_, _))
  }

  import TodoError._
  private def validateDescription(description: String):ValidationResult[String] = {
    if(description.trim.isBlank)
      DescriptionBlank.invalidNec
    else
      description.validNec
  }

  private def validateSlug(slug: String):ValidationResult[String] = {
    if(slug.trim.isBlank)
      SlugBlank.invalidNec
    else
      slug.validNec
  }

  def findBlog(id: String): Action[AnyContent] = Action.async {
    blogRepository.findById(BlogId(id)).map {
      case Some(blog) => Ok(Json.toJson(blog))
      case None => NotFound
    }
  }
}
