package services

import com.typesafe.scalalogging.LazyLogging
import play.api.Configuration

class GreetingService(config: Configuration) extends LazyLogging{

  trait PaymentMethod
  object PaymentMethod {
    case class Paypal(email: String) extends PaymentMethod
    case class IBAN(iban: String) extends PaymentMethod
    case class NOT_SUP() extends PaymentMethod
  }
  def hellomk: String = {
    logger.info("Hello %s".formatted(config.get[String]("zak.greeting")))
    val paymentMethod: PaymentMethod = PaymentMethod.Paypal("zakaria.bouazza@fastmail.com")

    import PaymentMethod._
    val result = paymentMethod match {
      case IBAN(iban) => s"Iban $iban"
      case NOT_SUP() => "Not supported try another payment method"
      case Paypal(email) => s"Paypal with email $email"
    }

    result
  }
}
