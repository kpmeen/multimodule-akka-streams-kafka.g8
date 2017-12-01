package $package$

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class ProducerConfig(
    kafkaUrl: String,
    kafkaTopic: String
)

object ProducerConfig {

  implicit def fromConfig(cfg: Config): ProducerConfig =
    cfg.getConfig("$project_name$-producer").as[ProducerConfig]

}
