package io.anaxo.rabbit.learning

import com.google.common.eventbus.EventBus
import io.micronaut.context.ApplicationContext
import spock.lang.Ignore
import spock.lang.Specification

import javax.inject.Inject
import javax.inject.Singleton

@Ignore
class EventBusSpec extends Specification {

  void "try to reproduce the stack overflow error"(){
    expect:
    ApplicationContext context = ApplicationContext.run()
    context.getBean(Subscriber)
  }


  @Singleton
  static class Subscriber {

    static final EventBus eventBus = new EventBus("")
    final Service service

    @Inject
    Subscriber(Service service) {
      this.service = service
      eventBus.register(this)
    }

    //@Subscribe
    void onEvent(String event){
      println event
    }

  }

  @Singleton
  static class Service {

  }
}
