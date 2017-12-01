package net.scalytica.tweetstream

import java.util.Properties

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import net.scalytica.tweetstream.ProducerConfig.fromConfig
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.config.TopicConfig.{
  CLEANUP_POLICY_COMPACT,
  CLEANUP_POLICY_CONFIG
}
import org.apache.kafka.common.serialization.{LongSerializer, ByteArraySerializer}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ProducerApp extends App {

  private val config = ConfigFactory.load()

  private val logger = LoggerFactory.getLogger(getClass)

  private implicit val actorSystem: ActorSystem =
    ActorSystem("$project_name$-producer-system", config)

  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val producerSettings =
    ProducerSettings(
      system = actorSystem,
      keySerializer = new LongSerializer,
      valueSerializer = new ByteArraySerializer
    ).withBootstrapServers(config.kafkaUrl)

  private val bufSize: Int = 1000

  def createKafkaAdmin(kafkaUrl: String): AdminClient = {
    val p = new Properties()
    p.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl)

    AdminClient.create(p)
  }

  private def newTopic(name: String) = {
    new NewTopic(name, 3, 1)
      .configs(Map(CLEANUP_POLICY_CONFIG -> CLEANUP_POLICY_COMPACT).asJava)
  }

  val src = SampleEventSource.throttled

  try {

    val adminClient = createKafkaAdmin(config.kafkaUrl)

    adminClient
      .listTopics()
      .names()
      .get()
      .asScala
      .find(_.equalsIgnoreCase(config.kafkaTopic))
      .fold(
        adminClient
          .createTopics(Seq(newTopic(config.kafkaTopic)).asJavaCollection)
      )(_)

    val done =
      src.map { t =>
        logger.debug(s"Received \$t")
        new ProducerRecord(config.kafkaTopic, Long.box(t.id), t.toByteArray)
      }.runWith(Producer.plainSink(producerSettings))

    Await.result(done, Duration.Inf) match {
      case Done => logger.info("Producer done")
    }
  } finally {
    Await.result(actorSystem.terminate(), Duration.Inf) match {
      case _ => logger.info("Producer actor system terminated.")
    }
  }
}
