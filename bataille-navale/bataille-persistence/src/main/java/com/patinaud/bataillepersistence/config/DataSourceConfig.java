package com.patinaud.bataillepersistence.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

@Configuration
@Profile("!dev")
public class DataSourceConfig {


    @Bean
    public DataSource dataSource(Environment env) {
        String db_driver = env.getProperty("SPRING_DATASOURCE_DRIVER");
        String db_url = env.getProperty("SPRING_DATASOURCE_URL");
        String db_user = env.getProperty("SPRING_DATASOURCE_USERNAME");
        String db_password = env.getProperty("SPRING_DATASOURCE_PASSWORD");

        System.out.println(db_driver);
        System.out.println(db_url);
        System.out.println(db_user);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(db_driver);
        dataSource.setUrl(db_url);
        dataSource.setUsername(db_user);
        dataSource.setPassword(db_password);


        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.patinaud.bataillepersistence.entity"); // Remplacez par votre package d'entités

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // configurez selon vos besoins
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        //  properties.setProperty("hibernate.default_schema", "bataille"); // Définissez votre schéma ici

        em.setJpaProperties(properties);


        return em;
    }
/*
    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
    */
}