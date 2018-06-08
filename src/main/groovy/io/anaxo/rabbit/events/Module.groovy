package io.anaxo.rabbit.events

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Scopes
import com.rabbitmq.client.ConnectionFactory
import io.anaxo.rabbit.Config
import org.yaml.snakeyaml.Yaml

import java.nio.file.Path
import java.nio.file.Paths

class Module extends AbstractModule {

  @Override
  void configure() {
    bind(ConnectionFactory)
      .toProvider(ConnectionFactoryProvider)
      .in(Scopes.SINGLETON)

    bind(Publisher)
      .in(Scopes.SINGLETON)

    bind(Consumer)
  }

  @Provides
  Config get() {
    return loadAs("config.yaml", Config)
  }

  static <T> T loadAs(String resource, Class<?> clazz) {
    Path path = Paths.get(ClassLoader.getSystemResource(resource).toURI())
    Yaml yaml = new Yaml()
    Map map = yaml.load(path.text) as Map
    return clazz.newInstance(map)
  }

}
