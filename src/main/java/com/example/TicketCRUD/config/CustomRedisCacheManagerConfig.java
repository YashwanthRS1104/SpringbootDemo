package com.example.TicketCRUD.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomRedisCacheManagerConfig {

  @Value("${spring.custom.url}")
  String url;

  @Bean
  public CacheManager customRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration defaultCacheConfig =
        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30));

    RedisCacheConfiguration customCacheConfig =
        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1));

    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    cacheConfigurations.put("tickets", customCacheConfig);

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(defaultCacheConfig)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    String host = url.split(":")[0];
    int port = Integer.valueOf(url.split(":")[1]);

    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
    return new LettuceConnectionFactory(redisConfig);
  }
}
