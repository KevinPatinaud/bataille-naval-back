package com.patinaud.batailleapi.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"com.patinaud.batailleengine", "com.patinaud.bataillecommunication", "com.patinaud.bataillepersistence", "com.patinaud.batailleplayer"})
public class AppConfig {

}
