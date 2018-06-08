package io.anaxo.rabbit.handlers

import com.google.inject.AbstractModule

import io.anaxo.rabbit.events.Module as EVENTS

class Module extends AbstractModule {

  @Override
  void configure() {
    install new EVENTS()
  }

}
