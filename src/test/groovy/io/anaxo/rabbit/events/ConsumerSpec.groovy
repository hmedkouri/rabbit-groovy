package io.anaxo.rabbit.events

import groovy.util.logging.Slf4j
import io.anaxo.rabbit.broker.BaseSpec
import spock.guice.UseModules
import spock.lang.Subject

import javax.inject.Inject

@Slf4j
@UseModules([
  io.anaxo.rabbit.events.Module
])
class ConsumerSpec extends BaseSpec {

  @Subject
  @Inject
  Consumer subject

  void "Simple consumer"(){
    expect:
    log.info "@@@@@@ > ${subject.get()}"

    cleanup:
    subject.destroy()
  }

}
