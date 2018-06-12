package io.anaxo.rabbit.handlers

import io.anaxo.rabbit.broker.BaseSpec
import io.anaxo.rabbit.events.Publisher
import spock.util.concurrent.AsyncConditions

class HandlerSpec extends BaseSpec {

  Publisher publisher
  Handler handler

  void setup() {
    publisher = context.getBean(Publisher)
    handler = context.getBean(Handler)
  }

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
        assert res != null
        assert res == "\"Hello World!\""
      }
    }

    then:
    async.await(1)
  }
}
