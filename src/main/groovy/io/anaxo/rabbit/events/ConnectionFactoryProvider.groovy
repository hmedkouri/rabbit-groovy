package io.anaxo.rabbit.events

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.util.logging.Slf4j
import io.anaxo.rabbit.Config

import javax.inject.Inject
import javax.inject.Provider

@Slf4j
class ConnectionFactoryProvider implements Provider<ConnectionFactory> {

  @Inject
  Config config

  @Override
  ConnectionFactory get() {
    def broker = config.events.broker

    log.debug "creating connection factory"
    ConnectionFactory factory = new ConnectionFactory()
    factory.setUsername(broker.username)
    factory.setPassword(broker.password)
    factory.setHost(broker.host)
    factory.setPort(broker.port)

    log.debug "creating connection and channel"
    Connection connection = factory.newConnection()
    Channel channel = connection.createChannel()

    log.debug "declaring the exchange and queue"
    channel.exchangeDeclare(config.events.publish.exchange, "topic")
    channel.queueDeclare(config.events.consume.queue, false, false, false, null)
    channel.queueBind(config.events.consume.queue, config.events.publish.exchange, Events.ALL)

    log.debug "closing channel and connection"
    channel.close()
    connection.close()

    return factory
  }
}
