package io.anaxo.rabbit.events

import com.rabbitmq.client.*
import groovy.util.logging.Slf4j
import io.anaxo.rabbit.config.Config

import javax.inject.Inject

@Slf4j
class Consumer {

  Config config
  Connection connection = null
  Channel channel = null

  @Inject
  Consumer(ConnectionFactory factory, Config config){
    this.config = config
    connection = factory.newConnection()
    channel = connection.createChannel()
  }

  String get(){
    def response = channel.basicGet(config.events.consume.queue,true)
    if(response) {
      def body = new String(response.body)
      log.info "Received response {}", body
      return body
    } else {
      log.warn "Nothing to consume"
      return "NO MESSAGE!"
    }
  }

  void consume(Closure handler) {
    channel.basicConsume(config.events.consume.queue, false, "myConsumerTag",
      new DefaultConsumer(channel) {
        @Override
        void handleDelivery(String consumerTag,
                            Envelope envelope,
                            AMQP.BasicProperties properties,
                            byte[] body)
          throws IOException {
          String routingKey = envelope.getRoutingKey()
          String contentType = properties.getContentType()
          long deliveryTag = envelope.getDeliveryTag()
          def content = new String(body)
          log.info "Received routingKey {}, contentType {}, deliveryTag {}, response {}",
            routingKey, contentType, deliveryTag, content
          handler.call content
          channel.basicAck deliveryTag, false
        }
      })
  }

  void destroy(){
    channel.close()
    connection.close()
  }
}
