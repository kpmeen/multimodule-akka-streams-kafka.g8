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

lazy val producer = BaseProject("$project_name$-producer")
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(CommonSettings: _*)
  .settings(DockerSettings: _*)
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

lazy val consumer = BaseProject("$project_name$-consumer")
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(CommonSettings: _*)
  .settings(DockerSettings: _*)
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
