package io.anaxo.rabbit

import io.anaxo.rabbit.broker.BaseSpec
import io.micronaut.http.client.Client
import io.micronaut.http.client.HttpClient

class HelloControllerSpec extends BaseSpec {

  void "test prototype scope"() {
    expect:
    context.getBean(HelloController) != null
  }

  void "test get"(){
    given:
    HttpClient client = server.applicationContext.createBean HttpClient.class, server.getURL()

    when:
    def body = client.toBlocking().retrieve("/hello/Hicham")

    then:
    body == "Hello Hicham!"
  }

  @Client('/publisher/books')
  static interface HelloClient {
  }
}
