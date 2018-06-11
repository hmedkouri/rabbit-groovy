package io.anaxo.rabbit.events

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.rabbitmq.client.*
import com.rabbitmq.client.impl.DefaultExceptionHandler
import groovy.util.logging.Slf4j
import io.anaxo.rabbit.config.Config
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

@Slf4j
@Factory
class ConnectionFactoryProvider {

  @Inject
  Config config

  ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("rabbitmq-pool-%d").build()
  ExecutorService executor = Executors.newFixedThreadPool(3, threadFactory)

  @Bean
  @Singleton
  ConnectionFactory get() {
    def broker = config.events.broker

    log.debug "creating connection factory"
    ConnectionFactory factory = new ConnectionFactory()
    factory.setExceptionHandler(new ConnectionMonitor())
    /*factory.setTopologyRecoveryEnabled(true)
    factory.setAutomaticRecoveryEnabled(true)
    factory.setNetworkRecoveryInterval(200)
    factory.setRequestedHeartbeat(0)
    factory.setConnectionTimeout(500)
    factory.setHandshakeTimeout(1000)*/
    factory.setSharedExecutor(executor)
    factory.setUsername(broker.username)
    factory.setPassword(broker.password)
    factory.setHost(broker.host)
    factory.setPort(broker.port)

    log.debug "creating connection and channel"
    Connection connection = factory.newConnection()
    Channel channel = connection.createChannel()

    log.debug "declaring the exchange and queue"
    channel.exchangeDeclare(config.events.publish.exchange, "topic")
    channel.queueDeclare(config.events.consume.queue, true, false, false, null)
    channel.queueBind(config.events.consume.queue, config.events.publish.exchange, Events.ALL)

    log.debug "closing channel and connection"
    channel.close()
    connection.close()

    return factory
  }

  private static class ConnectionMonitor extends DefaultExceptionHandler implements RecoveryListener {

    @Override
    void handleUnexpectedConnectionDriverException(Connection connection, Throwable exception) {
      log.error("Unexpected connection exception {}", exception.getMessage())
    }

    @Override
    void handleConnectionRecoveryException(Connection connection, Throwable exception) {
      log.error("Connection recovery exception {}", exception.getMessage())
    }

    @Override
    void handleConsumerException(Channel channel,
                                 Throwable exception,
                                 com.rabbitmq.client.Consumer consumer,
                                 String consumerTag,
                                 String methodName) {
      super.handleConsumerException(channel, exception, consumer, consumerTag, methodName)
      log.error("Channel consume exception. Exception: {}", exception.getMessage())
    }

    @Override
    void handleRecovery(Recoverable recoverable) {
      log.info("Connection recovered.")
    }

    @Override
    void handleRecoveryStarted(Recoverable arg0) {
      log.info("Autorecovery started.")
    }

  }
}
