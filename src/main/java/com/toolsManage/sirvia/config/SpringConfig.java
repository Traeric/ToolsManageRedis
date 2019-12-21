package com.toolsManage.sirvia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class SpringConfig {

    @Bean("jedis")
    public Jedis getJedis() {
        return new Jedis();
    }
}
