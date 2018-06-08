package io.anaxo.rabbit

import com.google.inject.AbstractModule
import com.google.inject.Scopes

import io.anaxo.rabbit.events.Events as EVENTS

class Module extends AbstractModule {

  @Override
  void configure() {
    install(new EVENTS())

    bind(Application).in(Scopes.SINGLETON)
  }
}
