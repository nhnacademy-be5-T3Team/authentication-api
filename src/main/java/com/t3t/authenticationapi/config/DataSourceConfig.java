package com.t3t.authenticationapi.config;

import com.t3t.authenticationapi.property.DatabaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties){
        return DataSourceBuilder.create()
                .url(databaseProperties.getDatabaseUrl())
                .driverClassName(databaseProperties.getDriverClassName())
                .username(databaseProperties.getUsername())
                .password(databaseProperties.getPassword())
                .build();
    }
}
