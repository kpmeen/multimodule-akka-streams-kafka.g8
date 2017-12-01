import sbt._
import com.trueaccord.scalapb.compiler.Version.scalapbVersion

object Dependencies {
  // scalastyle:off

  // Library versions
  val AkkaVersion       = "2.5.7"
  val AkkaKafkaVersion  = "0.18"
  val AkkaHttpVersion   = "10.0.10"
  val ProtobufVersion   = "3.4.0"
  val FicusVersion      = "1.4.3"
  val JodaVersion       = "2.9.9"
  val Slf4jVersion      = "1.7.25"
  val LogbackVersion    = "1.2.3"
  val ScalatestVersion  = "3.0.4"
  val EmbedKafkaVersion = "1.0.0"

  // Akka stuff
  val AkkaStreamKafka   = "com.typesafe.akka" %% "akka-stream-kafka"   % AkkaKafkaVersion
  val AkkaHttp          = "com.typesafe.akka" %% "akka-http"           % AkkaHttpVersion
  val AkkaActor         = "com.typesafe.akka" %% "akka-actor"          % AkkaVersion
  val AkkaStream        = "com.typesafe.akka" %% "akka-stream"         % AkkaVersion
  val AkkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
  val AkkaSlf4j         = "com.typesafe.akka" %% "akka-slf4j"          % AkkaVersion

  // Protocol buffers
  val Protobuf      = "com.google.protobuf"    % "protobuf-java"    % ProtobufVersion
  val ScalaProtobuf = "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf"

  // misc
  val Ficus      = "com.iheart"     %% "ficus"                    % FicusVersion
  val JodaTime   = "joda-time"      % "joda-time"                 % JodaVersion
  val Slf4jApi   = "org.slf4j"      % "slf4j-api"                 % Slf4jVersion
  val Logback    = "ch.qos.logback" % "logback-classic"           % LogbackVersion
  val Scalatest  = "org.scalatest"  %% "scalatest"                % ScalatestVersion % Test
  val EmbedKafka = "net.manub"      %% "scalatest-embedded-kafka" % EmbedKafkaVersion % Test exclude ("log4j", "log4j")

}
