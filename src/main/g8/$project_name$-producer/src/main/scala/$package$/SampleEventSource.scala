package $package$

import akka.stream.ThrottleMode.Shaping
import akka.stream.scaladsl._

import org.joda.time.DateTime.now

import $package$.schemas.sample.SampleEvent

import scala.concurrent.duration._

object SampleEventSource {

  val throttled =
    Source
      .fromIterator(() => (1 to Int.MaxValue).iterator)
      .map(i => SampleEvent(i.toLong, now.getMillis, s"Sample event \$i"))
      .throttle(
        elements = 10,
        per = 1 second,
        maximumBurst = 15,
        mode = Shaping
      )

}
