package io.anaxo.rabbit

import groovy.util.logging.Slf4j
import io.anaxo.rabbit.events.Consumer
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.Micronaut
import io.micronaut.runtime.server.event.ServerStartupEvent

import javax.inject.Singleton

@Singleton
@Slf4j
class Application implements ApplicationEventListener<ServerStartupEvent> {

  private final Consumer consumer

  Application(Consumer consumer) {
    this.consumer = consumer
  }

  @Override
  void onApplicationEvent(ServerStartupEvent event) {
    //consumer.consume{log.info it}
    log.info("Application has started")
  }

  static void main(args) {
    Micronaut.run(Application, args)
  }
}
