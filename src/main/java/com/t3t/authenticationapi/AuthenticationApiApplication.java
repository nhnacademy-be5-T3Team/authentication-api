package com.t3t.authenticationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}
}
