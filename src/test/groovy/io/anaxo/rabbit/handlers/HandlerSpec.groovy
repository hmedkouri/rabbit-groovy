package io.anaxo.rabbit.handlers

import io.anaxo.rabbit.Config
import io.anaxo.rabbit.broker.BaseSpec
import io.anaxo.rabbit.events.Publisher
import io.anaxo.rabbit.handlers.Module as HANDLERS
import spock.guice.UseModules
import spock.util.concurrent.AsyncConditions

import javax.inject.Inject

@UseModules([
  HANDLERS
])
class HandlerSpec extends BaseSpec {

  @Inject
  Publisher publisher

  @Inject
  Handler handler

  @Inject
  Config config

  void "publish 3 messages and consume them"() {
    given:
    3.times {
      this.publisher.publish "event.created", "Hello World!"
    }

    and: "An instance of AsyncConditions"
    def async = new AsyncConditions(3)

    and:
    org.reactivestreams.Publisher<String> h = handler.createPublisher()

    when:
    h.log("io.anaxo.rabbit.handlers").subscribe{
      res ->
      async.evaluate {
        res != null
      }
    }

    then:
    async.await(1)

  }

}
