# Multi-module akka-streams-kafka template

Use this template create a multi-module sbt project based on
[akka-streams-kafka](https://doc.akka.io/docs/akka-stream-kafka/current/home.html), using
google protocol buffers for (de)serialization of messages from/to kafka.

In addition to the sbt modules, the template also comes with pre-configured two
`docker-compose.yml` files. One for starting `zookeeper` and `kafka`, and one that starts
up `zookeeper` and `kafka`, as well as the consumer and producer services.


## Usage

```
$ sbt new kpmeen/multimodule-akka-stream-kafka.g8
```

## License

