package org.pj.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RedisClient {
    private static final Logger logger = LogManager.getLogger(RedisClient.class);

    private final JedisPool jedisPool;

    public RedisClient(String host, int port) {
        logger.info("Initializing RedisClient with connection pool");

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("./config/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
        // Đọc giá trị từ file config với giá trị mặc định nếu không có
        int maxTotal = Integer.parseInt(properties.getProperty("redis.pool.max.total", "25"));
        int maxIdle = Integer.parseInt(properties.getProperty("redis.pool.max.idle", "10"));
        int minIdle = Integer.parseInt(properties.getProperty("redis.pool.min.idle", "5"));
        long maxWaitMillis = Long.parseLong(properties.getProperty("redis.pool.max.wait.millis", "2000"));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);

        jedisPool = new JedisPool(poolConfig, host, port);
    }

    public Jedis getJedis() {
        logger.info("Getting Jedis resource from pool");
        return jedisPool.getResource();
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
