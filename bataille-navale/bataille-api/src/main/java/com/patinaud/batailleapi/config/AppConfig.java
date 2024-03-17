package com.patinaud.batailleapi.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.patinaud.batailleengine", "com.patinaud.bataillecommunication", "com.patinaud.bataillepersistence", "com.patinaud.batailleplayer"})
public class AppConfig {

}
