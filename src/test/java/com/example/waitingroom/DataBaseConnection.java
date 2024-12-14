package com.example.waitingroom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DataBaseConnection {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void testRedisConnection() {
        assertThat(redisConnectionFactory.getConnection()).isNotNull();
        System.out.println("Redis is connected!");
    }
}
