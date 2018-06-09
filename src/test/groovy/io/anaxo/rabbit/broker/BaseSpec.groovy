package io.anaxo.rabbit.broker

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.util.logging.Slf4j
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.apache.qpid.server.SystemLauncher
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Slf4j
abstract class BaseSpec extends Specification {

  ConnectionFactory factory

  @Shared
  @AutoCleanup
  ApplicationContext context = ApplicationContext.run()

  @Shared
  @AutoCleanup
  EmbeddedServer server = context.getBean(EmbeddedServer).start()

  @Shared
  static final String INITIAL_CONFIGURATION = "qpid-embedded-initial.json"

  @Shared
  SystemLauncher systemLauncher

  Connection getConnection() {
    if (!factory) {
      factory = new ConnectionFactory()
      factory.setUri(getConnectionURL())
      factory.useSslProtocol()
    }
    return factory.newConnection()
  }

  String getConnectionURL() {
    "amqp://guest:password@localhost:5672"
  }

  void setupSpec() {
    systemLauncher = new SystemLauncher()
    systemLauncher.startup(createSystemConfig())

    log.info "broker started"

  }

  void cleanupSpec() {
    systemLauncher.shutdown()
    log.info "broker stopped"
  }

  private static Map<String, Object> createSystemConfig() {
    Map<String, Object> attributes = new HashMap<>()
    URL initialConfig = BaseSpec.class.getClassLoader().getResource(INITIAL_CONFIGURATION)
    attributes.put("type", "Memory")
    attributes.put("initialConfigurationLocation", initialConfig.toExternalForm())
    attributes.put("startupLoggedToSystemOut", true)
    return attributes
  }
}
