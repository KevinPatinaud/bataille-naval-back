package com.patinaud.bataillepersistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("!dev")
public class DataSourceConfig {


    @Bean
    public DataSource dataSource(Environment env) {
        String dbDriver = env.getProperty("SPRING_DATASOURCE_DRIVER");
        String dbUrl = env.getProperty("SPRING_DATASOURCE_URL");
        String dbUser = env.getProperty("SPRING_DATASOURCE_USERNAME");
        String dbPassword = env.getProperty("SPRING_DATASOURCE_PASSWORD");


        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if ( dbDriver != null )
        {
            dataSource.setDriverClassName(dbDriver);
        }
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);


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

}