package io.anaxo.rabbit.events

import io.anaxo.rabbit.broker.BaseSpec
import spock.lang.Subject

class PublisherSpec extends BaseSpec {

  @Subject
  Publisher subject

  void setup() {
    subject = context.getBean(Publisher)
  }

  void "produce 3 messages"() {
    expect:
    3.times {
      subject.publish "","Hello World!"
    }
  }
}
