package io.anaxo.rabbit.events

import io.anaxo.rabbit.broker.BaseSpec
import spock.util.concurrent.AsyncConditions

class PubSubSpec extends BaseSpec {

  Publisher publisher
  Consumer consumer

  void setup() {
    publisher = context.getBean(Publisher)
    consumer = context.getBean(Consumer)
  }

  void "publish 3 messages and consume them"() {
    given:
    3.times {
      publisher.publish "event.created", "Hello World!"
    }

    and: "An instance of AsyncConditions"
    def async = new AsyncConditions(3)

    when:
    consumer.consume({ res ->
      async.evaluate {
        res != null
      }
    })

    then:
    async.await(1)
  }

  void cleanup(){
    consumer.destroy()
  }

}
