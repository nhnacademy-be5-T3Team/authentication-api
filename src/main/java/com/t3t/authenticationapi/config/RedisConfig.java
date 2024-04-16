package com.t3t.authenticationapi.config;

import com.t3t.authenticationapi.keymanager.properties.SecretKeyProperties;
import com.t3t.authenticationapi.keymanager.service.SecretKeyManagerService;
import com.t3t.authenticationapi.property.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 연결을 위한 configuration 클래스
 * @author joohyun1996 (이주현)
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public RedisProperties redisProperties(SecretKeyManagerService secretKeyManagerService,
                                           SecretKeyProperties secretKeyProperties,
                                           Environment environment){

        String activeProfile = environment.getActiveProfiles()[0];
        String activeProfileSuffix = activeProfile.equals("prod") ? "" : "_" + activeProfile;

        return RedisProperties.builder()
                .host(secretKeyManagerService.getSecretValue(secretKeyProperties.getRedisIpAddressKeyId()))
                .port(Integer.valueOf(secretKeyManagerService.getSecretValue(secretKeyProperties.getRedisPortKeyId())))
                .password(secretKeyManagerService.getSecretValue(secretKeyProperties.getRedisPasswordKeyId()))
                .database(20)
                .build();
    }

    /**
     * RedisServer에 연결을 생성하는데 사용되는 클래스
     * getConnection() 호출될 때 마다 새로운 LettuceConnection 생성
     * Thread-safe 하다
     * 동기, 비동기, 리액티브 api 모두 가능
     * @author joohyun1996 (이주현)
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        configuration.setPassword(redisProperties.getPassword());
        configuration.setDatabase(redisProperties.getDatabase());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisProperties redisProperties){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory(redisProperties));
        return redisTemplate;
    }
}
