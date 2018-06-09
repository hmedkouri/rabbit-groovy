package io.anaxo.rabbit.config

class Config {

  static Events events

  static class Events {
    Broker broker
    Consume consume
    Publish publish
  }

  static class Broker {
    String username
    String password
    String host
    Integer port
  }

  static class Consume {
    String queue
  }

  static class Publish {
    String exchange
  }

}
