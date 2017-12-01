package $package$

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import $package$.ConsumerConfig._
import $package$.schemas.sample.SampleEvent
import org.apache.kafka.common.serialization.{
  LongDeserializer,
  ByteArrayDeserializer
}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ConsumerApp extends App {

  private val config = ConfigFactory.load()

  private val logger = LoggerFactory.getLogger(getClass)

  private implicit val actorSystem: ActorSystem =
    ActorSystem("$project_name$-consumer-system", config)

  implicit val mat: ActorMaterializer = ActorMaterializer()

  val consumerSettings = ConsumerSettings(
    actorSystem,
    new LongDeserializer,
    new ByteArrayDeserializer
  ).withBootstrapServers(config.kafkaUrl).withGroupId("$project_name$Consumer")

  try {
    val done = Consumer
      .plainSource(
        settings = consumerSettings,
        subscription = Subscriptions.topics(config.kafkaTopic)
      )
      .map(cm => SampleEvent.parseFrom(cm.value()))
      .runWith(Sink.foreach(se => logger.debug(s"record processed => \$se")))

    Await.result(done, Duration.Inf) match {
      case Done => logger.info("Consumer done")
    }
  } finally {
    Await.result(actorSystem.terminate(), Duration.Inf) match {
      case _ => logger.info("Consumer actor system terminated")
    }
  }

}
