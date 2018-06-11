package io.anaxo.rabbit

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.sse.Event
import io.reactivex.Flowable
import org.reactivestreams.Publisher

import javax.inject.Inject
import java.util.concurrent.TimeUnit

@Controller("/")
class HelloController {

  @Inject
  io.anaxo.rabbit.events.Publisher publisher

  @Get("/hello/{name}")
  String hello(String name) {
    return "Hello $name!"
  }

  @Get("/count")
  Publisher<Event<Integer>> count() {
    return Flowable.just(1, 2, 3, 4, 5)
      .zipWith(Flowable.interval(500, TimeUnit.MILLISECONDS),      { item, interval -> Event.of(item)})
      .doOnComplete{-> } as Publisher<Event<Integer>>
  }

  @Post(uri = "/publish", consumes = [MediaType.TEXT_PLAIN])
  HttpResponse publish(@Body String event) {
    publisher.publish("event.created", event)
    HttpResponse.ok()
  }

}
