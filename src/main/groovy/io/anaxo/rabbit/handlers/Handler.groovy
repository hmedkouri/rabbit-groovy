package io.anaxo.rabbit.handlers

import io.anaxo.rabbit.events.Consumer
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

import javax.inject.Inject

class Handler {

  private final Consumer consumer

  @Inject
  Handler(Consumer consumer) {
    this.consumer = consumer
  }

  Flux<String> createPublisher() {
    return Flux.create { FluxSink sink ->
      consumer.consume { event -> sink.next(event) }
      sink.onDispose {
        consumer.destroy()
      }
    }
  }

}
