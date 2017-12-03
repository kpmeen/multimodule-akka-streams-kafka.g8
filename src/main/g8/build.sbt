import Dependencies._
import Settings._

// scalastyle:off
name := "$name$"

lazy val root = (project in file("."))
  .settings(NoPublish: _*)
  .aggregate(
    schema,
    producer,
    consumer
  )

lazy val schema = BaseProject("$project_name$-pb-schema")
  .settings(NoPublish: _*)
  .settings(libraryDependencies += ScalaProtobuf)
  .settings(scalacOptions -= "-Ywarn-unused:imports")
  .settings(
    PB.targets in Compile := Seq(
      PB.gens.java -> (sourceManaged in Compile).value,
      scalapb
        .gen(javaConversions = true, grpc = false) -> (sourceManaged in Compile).value
    )
  )

lazy val producer = DockerProject("$project_name$-producer")
  .settings(
    libraryDependencies ++= Seq(
      AkkaStreamKafka,
      AkkaActor,
      AkkaStream,
      AkkaHttp,
      AkkaSlf4j,
      Protobuf,
      Ficus,
      JodaTime,
      Slf4jApi,
      Logback,
      Scalatest,
      EmbedKafka,
      AkkaStreamTestkit
    )
  )
  .dependsOn(schema)

lazy val consumer = DockerProject("$project_name$-consumer")
  .settings(
    libraryDependencies ++= Seq(
      AkkaStreamKafka,
      AkkaActor,
      AkkaStream,
      AkkaSlf4j,
      Protobuf,
      Ficus,
      JodaTime,
      Slf4jApi,
      Logback,
      Scalatest,
      EmbedKafka,
      AkkaStreamTestkit
    )
  )
  .dependsOn(schema)
