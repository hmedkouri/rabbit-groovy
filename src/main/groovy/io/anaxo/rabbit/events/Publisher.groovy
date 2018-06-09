package io.anaxo.rabbit.events

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import io.anaxo.rabbit.config.Config

import javax.inject.Inject

@Slf4j
class Publisher {

  @Inject
  ConnectionFactory factory

  @Inject
  Config config

  void publish(String routingKey, Serializable event) {
    Connection connection = factory.newConnection()
    Channel channel = connection.createChannel()
    try {
      byte[] messageBodyBytes = JsonOutput.toJson(event).bytes
      channel.basicPublish(config.events.publish.exchange, routingKey, null, messageBodyBytes)
      log.info "Published event {}", event
    } catch (Exception e) {
      log.error "Error occured {}", e.getMessage()
      e.printStackTrace()
    } finally {
      channel.close()
      connection.close()
    }
  }
}
