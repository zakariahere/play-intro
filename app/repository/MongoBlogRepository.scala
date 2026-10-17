package repository

import model.{Blog, BlogId}
import org.bson.codecs.configuration.CodecRegistries.{fromCodecs, fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import scala.concurrent.{ExecutionContext, Future}

class MongoBlogRepository(db: MongoDatabase)(implicit ec: ExecutionContext) extends BlogRepository {
  private val codecs: CodecRegistry = fromRegistries(fromCodecs(BlogId.codec),
    fromProviders(classOf[Blog]), MongoClient.DEFAULT_CODEC_REGISTRY)
  private val collection: MongoCollection[Blog] = db.withCodecRegistry(codecs).getCollection[Blog]("blogs")

  override def save(blog: Blog): Future[Unit] = collection.replaceOne(equal("_id", blog.id.value), blog, ReplaceOptions().upsert(true)).toFuture().map(_ => ())
  override def findAll(): Future[Seq[Blog]] = collection.find().toFuture()
  override def findById(id: BlogId): Future[Option[Blog]] = collection.find(equal("_id", id.value)).headOption()
}
