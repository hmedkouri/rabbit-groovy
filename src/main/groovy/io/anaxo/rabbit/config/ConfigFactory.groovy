package io.anaxo.rabbit.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import org.yaml.snakeyaml.Yaml

import javax.inject.Singleton
import java.nio.file.Path
import java.nio.file.Paths

@Factory
class ConfigFactory {

  @Bean
  @Singleton
  Config get() {
    return loadAs("config.yaml", Config)
  }

  static <T> T loadAs(String resource, Class<?> clazz) {
    Path path = Paths.get(ClassLoader.getSystemResource(resource).toURI())
    Yaml yaml = new Yaml()
    Map map = yaml.load(path.toFile().text) as Map
    return clazz.newInstance(map) as T
  }
}
