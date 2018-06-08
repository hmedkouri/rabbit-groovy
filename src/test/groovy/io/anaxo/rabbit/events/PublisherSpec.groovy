package io.anaxo.rabbit.events

import io.anaxo.rabbit.broker.BaseSpec
import spock.guice.UseModules
import spock.lang.Subject

import javax.inject.Inject

@UseModules([
        io.anaxo.rabbit.events.Module
])
class PublisherSpec extends BaseSpec {

  @Subject
  @Inject
  Publisher subject

  void "produce 3 messages"() {
    expect:
    3.times {
      subject.publish "","Hello World!"
    }
  }

}
