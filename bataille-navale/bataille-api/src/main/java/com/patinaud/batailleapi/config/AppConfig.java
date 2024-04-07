package com.patinaud.batailleapi.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.patinaud.batailleengine", "com.patinaud.bataillecommunication", "com.patinaud.bataillepersistence", "com.patinaud.batailleplayer", "com.patinaud.batailleservice", "com.patinaud.batailleaspect"})
@EnableAspectJAutoProxy
@PropertySource("classpath:words.properties")
public class AppConfig {


}
