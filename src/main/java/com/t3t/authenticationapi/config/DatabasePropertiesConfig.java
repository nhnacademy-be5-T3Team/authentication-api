package com.t3t.authenticationapi.config;

import com.t3t.authenticationapi.keymanager.properties.SecretKeyProperties;
import com.t3t.authenticationapi.keymanager.service.SecretKeyManagerService;
import com.t3t.authenticationapi.property.DatabaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class DatabasePropertiesConfig {
    @Bean
    @Profile({"prod", "dev", "test"})
    public DatabaseProperties dataSourceProperties(SecretKeyManagerService secretKeyManagerService,
                                                   SecretKeyProperties secretKeyProperties,
                                                   Environment environment) {

        String activeProfile = environment.getActiveProfiles()[0];
        String activeProfileSuffix = activeProfile.equals("prod") ? "" : "_" + activeProfile;

        return DatabaseProperties.builder()
                .databaseUrl(String.format("jdbc:mysql://%s:%s/%s%s",
                        secretKeyManagerService.getSecretValue(secretKeyProperties.getDatabaseIpAddressKeyId()),
                        secretKeyManagerService.getSecretValue(secretKeyProperties.getDatabasePortKeyId()),
                        secretKeyManagerService.getSecretValue(secretKeyProperties.getDatabaseNameKeyId()),
                        activeProfileSuffix))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .username(secretKeyManagerService.getSecretValue(secretKeyProperties.getDatabaseUsernameKeyId()))
                .password(secretKeyManagerService.getSecretValue(secretKeyProperties.getDatabasePasswordKeyId()))
                .build();
    }
}
