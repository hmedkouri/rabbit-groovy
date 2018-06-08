package io.anaxo.rabbit

import com.google.inject.Guice
import groovy.util.logging.Slf4j

@Slf4j
class Application {

  static void main(args) {
    Guice.createInjector(new Module()).getInstance(Application).run()
  }

  void run() {

  }
}
