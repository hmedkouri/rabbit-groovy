package io.anaxo.rabbit.events

import com.google.common.eventbus.EventBus
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

import javax.inject.Singleton

@Factory
class EventBusFactory {

  @Bean
  @Singleton
  EventBus get() {
    return new EventBus("Default EventBus")
  }
}
