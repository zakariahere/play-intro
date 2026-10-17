package repository

import model.{Blog, BlogId}

import scala.concurrent.Future

trait BlogRepository {
  def save(blog: Blog): Future[Unit]
  def findAll(): Future[Seq[Blog]]
  def findById(id: BlogId): Future[Option[Blog]]
}
