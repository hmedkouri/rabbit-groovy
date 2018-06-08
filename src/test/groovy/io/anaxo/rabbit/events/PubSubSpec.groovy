package io.anaxo.rabbit.events

import io.anaxo.rabbit.Config
import io.anaxo.rabbit.broker.BaseSpec
import spock.guice.UseModules
import spock.util.concurrent.AsyncConditions

import javax.inject.Inject

@UseModules([
  io.anaxo.rabbit.events.Module
])
class PubSubSpec extends BaseSpec {

  @Inject
  Publisher publisher

  @Inject
  Consumer consumer

  @Inject
  Config config

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

}
