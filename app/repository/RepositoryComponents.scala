package repository

import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.BuiltInComponents

import scala.concurrent.Future

trait RepositoryComponents {
  self: BuiltInComponents =>
  lazy val mongoClient: MongoClient = {
    val client = MongoClient(configuration.get[String]("mongo.uri"))
    applicationLifecycle.addStopHook(() => Future.successful(client.close()))
    client
  }
  lazy val mongoDatabase: MongoDatabase = mongoClient.getDatabase(configuration.get[String]("mongo.database"))
  lazy val blogRepository: BlogRepository = new MongoBlogRepository(mongoDatabase)(executionContext)
}
