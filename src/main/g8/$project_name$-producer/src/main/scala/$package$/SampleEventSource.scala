package net.scalytica.tweetstream

import akka.stream.ThrottleMode.Shaping
import akka.stream.scaladsl._

import net.scalytica.tweetstream.schemas.sample.SampleEvent

import scala.concurrent.duration._

object SampleEventSource {

  val throttled =
    Source
      .fromIterator(() => (1 to Long.MaxValue).iterator)
      .map(i => SampleEvent(i, s"Sample event $i"))
      .throttle(
        elements = 10,
        per = 1 second,
        maximumBurst = 15,
        mode = Shaping
      )

}
