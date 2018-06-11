package io.anaxo.rabbit.learning;

import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.health.HeartbeatEvent
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.inject.Singleton

/**
 * @author graemerocher
 * @since 1.0
 */
class HeartbeatTaskSpec extends Specification {

    void "test that by default a heartbeat is sent"() {
        when:
        EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, [
                'micronaut.heartbeat.initialDelay':'1ms',
                'micronaut.heartbeat.interval':'10ms',
                'micronaut.application.name':'test'
        ])
        HeartbeatListener listener = embeddedServer.getApplicationContext().getBean(HeartbeatListener)
        PollingConditions conditions = new PollingConditions(timeout: 5, delay: 0.5)

        then:
        conditions.eventually {
            listener.event != null
        }

        cleanup:
        embeddedServer.stop()
    }


    @Singleton
    static class HeartbeatListener implements ApplicationEventListener<HeartbeatEvent> {
        private HeartbeatEvent event
        @Override
        synchronized void onApplicationEvent(HeartbeatEvent event) {
            println "Test Heartbeat listener $this received event $event"
            this.event = event
        }

        synchronized HeartbeatEvent getEvent() {
            println "Test Heartbeat listener $this return event $event"
            return event
        }
    }
}
