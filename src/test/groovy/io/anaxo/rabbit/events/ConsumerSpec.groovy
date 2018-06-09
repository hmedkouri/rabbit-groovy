package io.anaxo.rabbit.events

import groovy.util.logging.Slf4j
import io.anaxo.rabbit.broker.BaseSpec
import spock.lang.Subject


@Slf4j
class ConsumerSpec extends BaseSpec {

  @Subject
  Consumer subject

  void setup() {
    subject = context.getBean(Consumer)
  }

  void "Simple consumer"(){
    expect:
    subject.get() == "NO MESSAGE!"
  }

  void cleanup() {
    subject.destroy()
  }

}
