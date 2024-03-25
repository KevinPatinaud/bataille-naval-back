package com.patinaud.bataillepersistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.patinaud.bataillepersistence.dao")
@EntityScan(basePackages = {"com.patinaud.bataillepersistence.entity"})
public class PersistenceConfig {
}
