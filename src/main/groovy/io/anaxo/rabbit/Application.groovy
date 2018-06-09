package io.anaxo.rabbit

import groovy.util.logging.Slf4j
import io.micronaut.runtime.Micronaut

@Slf4j
class Application {

  static void main(args) {
    Micronaut.run(getClass())
  }
}
