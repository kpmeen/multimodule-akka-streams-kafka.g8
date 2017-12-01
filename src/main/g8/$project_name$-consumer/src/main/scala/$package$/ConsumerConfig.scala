package net.scalytica.tweetstream

import com.typesafe.config.Config

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class ConsumerConfig(
    kafkaUrl: String,
    kafkaTopic: String
)

object ConsumerConfig {
  implicit def fromConfig(cfg: Config): ConsumerConfig =
    cfg.getConfig("$project_name$-consumer").as[ConsumerConfig]
}
