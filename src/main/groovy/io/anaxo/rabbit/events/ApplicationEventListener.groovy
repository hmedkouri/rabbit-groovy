package io.anaxo.rabbit.events

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Context

import javax.inject.Inject

@Slf4j
@Context
class ApplicationEventListener {

  @Inject
  ApplicationEventListener(EventBus eventBus, Publisher publisher) {
    eventBus.register(new Subscriber(publisher))
  }

  class Subscriber {

    private final Publisher publisher

    Subscriber(Publisher publisher) {
      this.publisher = publisher
    }

    @Subscribe
    void applicationEvent(ReconcileEvent event) {
      log.info("Handling event {}", event)
      publisher.publish("event.create", event.id)
    }
  }
}
