package model

import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}
import org.mongodb.scala.bson.annotations.BsonProperty
import play.api.libs.json.{JsString, Json, Reads, Writes}

import java.util.UUID

final case class BlogId(value: String)

object BlogId {
  def generate = BlogId(UUID.randomUUID().toString)

  val codec: Codec[BlogId] = new Codec[BlogId] {
    override def decode(reader: BsonReader, decoderContext: DecoderContext): BlogId = BlogId(reader.readString())

    override def encode(writer: BsonWriter, value: BlogId, encoderContext: EncoderContext): Unit = writer.writeString(value.value)

    override def getEncoderClass: Class[BlogId] = classOf[BlogId]
  }
  implicit val writes: Writes[BlogId] = Writes(id => JsString(id.value))
}

final case class Blog(@BsonProperty("_id") id: BlogId, slug: String, description: String = "To fill in")

object Blog {
  implicit val writes: Writes[Blog] = Json.writes
}

final case class BlogCreateRequest(slug: String, description: String = "To fill in")

object BlogCreateRequest {
  implicit val reads: Reads[BlogCreateRequest] = Json.using[Json.WithDefaultValues].reads
}
